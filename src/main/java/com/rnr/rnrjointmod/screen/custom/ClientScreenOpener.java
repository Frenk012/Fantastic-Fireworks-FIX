package com.rnr.rnrjointmod.screen.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

/**
 * Client-only bridge so common code (blocks) never loads client classes on a dedicated server.
 * Only call from a {@code level.isClientSide} branch.
 */
public class ClientScreenOpener {

    public static void openFireworkEditor(BlockPos pos) {
        Minecraft.getInstance().setScreen(new FireworkCakeScreen(pos));
    }
}
