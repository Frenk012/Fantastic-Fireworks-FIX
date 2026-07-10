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
                // launches are played server-side by Cascade; clients only sync settings
                cake.applySyncTag(level.registryAccess(), data.data());
            }
        });
    }
}
