package com.rnr.rnrjointmod.screen.custom;

import foundry.imgui.api.ImGuiMC;
import foundry.imgui.impl.ImGuiMCImpl;
import imgui.ImGui;
import imgui.internal.ImGuiWindow;
import imgui.lwjgl3.glfw.ImGuiImplGlfwNative;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

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
                ImGui.showDemoWindow();
                //imgui stuff
                ImGui.begin("Firework Editor");
                ImGui.text("wassup");
                if (ImGui.button("click me")){
                    System.out.println("clicked");
                }
                if (ImGui.collapsingHeader("Help"))
                {
                    ImGui.text("wassupv2");
                }
                ImGui.end();
            }
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
