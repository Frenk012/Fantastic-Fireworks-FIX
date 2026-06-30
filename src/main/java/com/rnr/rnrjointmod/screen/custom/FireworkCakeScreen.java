package com.rnr.rnrjointmod.screen.custom;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.dataattatchments.ModDataAttatchments;
import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.ImGuiMCImpl;
import imgui.ImGui;
import imgui.internal.ImGuiWindow;
import imgui.lwjgl3.glfw.ImGuiImplGlfwNative;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.awt.*;

public class FireworkCakeScreen extends Screen {
    public FireworkCakeScreen() {
        super(Component.literal("hi"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        renderImGui();
        //super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 296){
            this.onClose();
            return true;
        }// else if (keyCode == 83 || keyCode == 65 || keyCode == 87 || keyCode == 68 || keyCode == 32 ) {
//            return true;
//        }
        return super.keyPressed(keyCode, scanCode, modifiers);

    }

    private void renderImGui(){
        try(ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx != null) {
                Player player = Minecraft.getInstance().player;
                //System.out.println(player.getName().toString());
                BlockPos pos = player.getData(ModDataAttatchments.FBLOCK); // im not getting the data for some reason
                Level level = player.level();
                FireworkCakeEntity fireworkCakeEntity = (FireworkCakeEntity) level.getBlockEntity(pos);
                System.out.println( player.getData(ModDataAttatchments.FBLOCK));
                if (fireworkCakeEntity != null) {
                    if(fireworkCakeEntity.fireworkBeginning == null) {
                        fireworkCakeEntity.setup(level, pos);
                    }
                    FireworkBeginning fireworkBeginning = fireworkCakeEntity.getFBegining();
                    FireworkBall fireworkBall = fireworkCakeEntity.getFBall();
                    Trail trail = fireworkCakeEntity.getFTrail();


                    //ImGui.showDemoWindow();
                    //imgui stuff
                    ImGui.begin("Firework Editor");
                    //if(fireworkCakeEntity != null){
                    ImGui.text(pos.toString());
                    //}
                    if (ImGui.button("click me")) {
                        System.out.println("clicked");
                        fireworkBall.col1 = new Color(230, 10, 10); // im overwriting this inside the fire rocket meathod i can spam the button as it fires and it works tho
                        fireworkBall.scale1 = 100f;
                        fireworkCakeEntity.setFBall(fireworkBall);
                        fireworkCakeEntity.setFBegining(fireworkBeginning);
                        fireworkCakeEntity.setFTrail(trail);
                    }
                    if (ImGui.collapsingHeader("Help")) {
                        ImGui.text("wassupv2");
                    }
                    ImGui.end();
                }
            }
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
