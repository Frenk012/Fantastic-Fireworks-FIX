package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleDataOnMain(final FireworkSettingsPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (!level.dimension().location().toString().equals(data.dimension())) {
                return;
            }
            if (level.getBlockEntity(data.pos()) instanceof FireworkCakeEntity cake) {
                cake.setup(level, data.pos());
                cake.fireworkBeginning.deserializeNBT(level.registryAccess(), data.fireworkBeginning());
                cake.fireworkBall.deserializeNBT(level.registryAccess(), data.fireworkBall());
                cake.trail.deserializeNBT(level.registryAccess(), data.trail());
            }
        });
    }
}
