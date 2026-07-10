package com.rnr.rnrjointmod.firework;

import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-side scheduler for firework shows: launches cakes (by id) at the
 * requested tick. Ids are resolved at fire time, so a show survives edits to
 * the cakes while it runs.
 */
@EventBusSubscriber(modid = RnRJointMod.MOD_ID)
public class FireworkShowScheduler {

    private record Step(long fireAtTick, String cakeId) {
    }

    private static final List<Step> QUEUE = new CopyOnWriteArrayList<>();

    /** @param delayTicks ticks from now until this launch */
    public static void schedule(MinecraftServer server, String cakeId, int delayTicks) {
        QUEUE.add(new Step(server.getTickCount() + Math.max(delayTicks, 0), cakeId));
    }

    /** @return number of cancelled launches */
    public static int cancelAll() {
        int size = QUEUE.size();
        QUEUE.clear();
        return size;
    }

    public static int pending() {
        return QUEUE.size();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (QUEUE.isEmpty()) {
            return;
        }
        long now = event.getServer().getTickCount();
        for (Step step : QUEUE) {
            if (step.fireAtTick() > now) {
                continue;
            }
            QUEUE.remove(step);
            GlobalPos globalPos = FireworkCakeRegistry.get(step.cakeId());
            if (globalPos == null) {
                RnRJointMod.LOGGER.warn("Firework show: no loaded cake with id '{}', skipping", step.cakeId());
                continue;
            }
            ServerLevel level = event.getServer().getLevel(globalPos.dimension());
            if (level != null && level.getBlockEntity(globalPos.pos()) instanceof FireworkCakeEntity cake) {
                cake.triggerLaunch(level);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        QUEUE.clear();
    }
}
