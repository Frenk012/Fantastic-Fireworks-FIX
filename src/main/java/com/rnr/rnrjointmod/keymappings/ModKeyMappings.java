package com.rnr.rnrjointmod.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.screen.custom.FireworkCakeScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = RnRJointMod.MOD_ID, value = Dist.CLIENT)
public class ModKeyMappings {

    public static final KeyMapping OPEN_IMGUI_MENU = new KeyMapping(
            "key.rnrjointmod.open_imgui_menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "key.categories.misc"
    );

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post evt) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!OPEN_IMGUI_MENU.consumeClick() || !minecraft.isWindowActive() || minecraft.level == null) {
            return;
        }
        // open the editor for the firework cake the player is looking at
        if (minecraft.hitResult instanceof BlockHitResult blockHit && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = blockHit.getBlockPos();
            if (minecraft.level.getBlockEntity(pos) instanceof FireworkCakeEntity) {
                minecraft.setScreen(new FireworkCakeScreen(pos));
            }
        }
    }

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_IMGUI_MENU);
    }
}
