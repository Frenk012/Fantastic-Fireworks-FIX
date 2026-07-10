package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleDataOnMain(final FireworkSettingsPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(data.dimension()));
            ServerLevel level = context.player().getServer().getLevel(dimension);
            if (level == null || !(level.getBlockEntity(data.pos()) instanceof FireworkCakeEntity cake)) {
                return;
            }
            cake.setup(level, data.pos());
            cake.fireworkBeginning.deserializeNBT(level.registryAccess(), data.fireworkBeginning());
            cake.fireworkBall.deserializeNBT(level.registryAccess(), data.fireworkBall());
            cake.trail.deserializeNBT(level.registryAccess(), data.trail());
            cake.setChanged();
            // broadcast the updated settings so every client stays in sync
            PacketDistributor.sendToAllPlayers(new FireworkSettingsPayload(
                    data.dimension(), data.pos(),
                    cake.fireworkBeginning.serializeNBT(level.registryAccess()),
                    cake.fireworkBall.serializeNBT(level.registryAccess()),
                    cake.trail.serializeNBT(level.registryAccess())));
        });
    }
}
