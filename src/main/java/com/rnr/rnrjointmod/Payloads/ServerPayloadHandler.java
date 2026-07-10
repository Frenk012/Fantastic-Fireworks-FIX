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
            cake.applySyncTag(level.registryAccess(), data.data());
            cake.setChanged();
            if (data.launch()) {
                cake.triggerLaunch(level);
            } else {
                // broadcast the updated settings so every client stays in sync
                PacketDistributor.sendToAllPlayers(new FireworkSettingsPayload(
                        data.dimension(), data.pos(), cake.buildSyncTag(level.registryAccess()), false));
            }
        });
    }
}
