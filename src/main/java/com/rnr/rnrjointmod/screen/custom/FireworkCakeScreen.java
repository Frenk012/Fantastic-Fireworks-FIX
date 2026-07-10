package com.rnr.rnrjointmod.screen.custom;

import com.rnr.rnrjointmod.Payloads.FireworkSettingsPayload;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class FireworkCakeScreen extends Screen {

    private final BlockPos pos;

    private boolean changed;

    public FireworkCakeScreen(BlockPos pos) {
        super(Component.translatable("screen.rnrjointmod.firework_editor"));
        this.pos = pos;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderImGui();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_F7) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void renderImGui() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }
            Level level = Minecraft.getInstance().level;
            if (level == null || !(level.getBlockEntity(pos) instanceof FireworkCakeEntity cake)) {
                ImGui.begin("Firework Editor");
                ImGui.textWrapped("No firework cake at " + pos.toShortString() + " - look at a firework cake and press F7.");
                ImGui.end();
                return;
            }
            cake.setup(level, pos);
            changed = false;

            ImGui.begin("Firework Editor");
            ImGui.textWrapped("Selected Block: " + pos.getX() + " X, " + pos.getY() + " Y, " + pos.getZ() + " Z ");
            if (ImGui.button("Launch")) {
                cake.shootFirework(level, pos.above());
            }

            FireworkBeginning body = cake.fireworkBeginning;
            FireworkBall ball = cake.fireworkBall;
            Trail trail = cake.trail;

            if (ImGui.collapsingHeader("Rocket Body")) {
                if (ImGui.checkbox("Use Trail", body.usetrail)) {
                    body.usetrail = !body.usetrail;
                    changed = true;
                }
                body.col1 = colorPicker("Color1", body.col1);
                body.col2 = colorPicker("Color2", body.col2);
                if (ImGui.checkbox("Random Color In Range (col1 - col2)", body.rancolinrange)) {
                    body.rancolinrange = !body.rancolinrange;
                    changed = true;
                }

                ImGui.textWrapped("Start Transparency : End Transparency");
                float[] trans = {body.transparancy1, body.transparancy2};
                if (ImGui.sliderFloat2("##0", trans, 0f, 1f)) {
                    body.transparancy1 = trans[0];
                    body.transparancy2 = trans[1];
                    changed = true;
                }

                ImGui.textWrapped("Start Scale : End Scale");
                float[] scale = {body.scale1, body.scale2};
                if (ImGui.sliderFloat2("##1", scale, 0f, 10f)) {
                    body.scale1 = scale[0];
                    body.scale2 = scale[1];
                    changed = true;
                }

                body.lifetime = sliderInt("Lifetime", "##2", body.lifetime, 0, 150);
                body.gravity = sliderFloat("Gravity", "##3", body.gravity, 0f, 10f);
                body.ranoffset = sliderFloat("Random Offset", "##4", body.ranoffset, 0f, 2.5f);

                ImGui.textWrapped("Up Velocity : Up Acceleration");
                float[] upspeed = {body.initalUpVelo, body.upwardsAcceleration};
                if (ImGui.sliderFloat2("##5", upspeed, 0f, 5f)) {
                    body.initalUpVelo = upspeed[0];
                    body.upwardsAcceleration = upspeed[1];
                    changed = true;
                }
            }

            if (ImGui.collapsingHeader("Rocket Explosion")) {
                if (ImGui.checkbox("Use Trail##fb", ball.usetrail)) {
                    ball.usetrail = !ball.usetrail;
                    changed = true;
                }
                ball.col1 = colorPicker("Color1##fb", ball.col1);
                ball.col2 = colorPicker("Color2##fb", ball.col2);
                if (ImGui.checkbox("Random Color In Range (col1 - col2)##fb", ball.rancolinrange)) {
                    ball.rancolinrange = !ball.rancolinrange;
                    changed = true;
                }

                ImGui.textWrapped("Start Transparency : End Transparency");
                float[] trans = {ball.transparancy1, ball.transparancy2};
                if (ImGui.sliderFloat2("##0fb", trans, 0f, 1f)) {
                    ball.transparancy1 = trans[0];
                    ball.transparancy2 = trans[1];
                    changed = true;
                }

                ImGui.textWrapped("Start Scale : End Scale");
                float[] scale = {ball.scale1, ball.scale2};
                if (ImGui.sliderFloat2("##1fb", scale, 0f, 10f)) {
                    ball.scale1 = scale[0];
                    ball.scale2 = scale[1];
                    changed = true;
                }

                ball.lifetime = sliderInt("Lifetime", "##2fb", ball.lifetime, 0, 150);
                ball.gravity = sliderFloat("Gravity", "##3fb", ball.gravity, 0f, 10f);
                ball.ranoffset = sliderFloat("Random Offset", "##4fb", ball.ranoffset, 0f, 2.5f);
                ball.range = sliderFloat("Repulsion Range", "##5fb", ball.range, 0f, 10f);
                ball.repulsionlength = sliderInt("Repulsion Lifetime", "##6fb", ball.repulsionlength, 0, 60);
                ball.count = sliderInt("Particle Count", "##7fb", ball.count, 0, 400);
            }

            if (ImGui.collapsingHeader("Rocket Trail")) {
                trail.col1 = colorPicker("Color1##t", trail.col1);
                trail.col2 = colorPicker("Color2##t", trail.col2);
                if (ImGui.checkbox("Random Color In Range (col1 - col2)##t", trail.rancolinrange)) {
                    trail.rancolinrange = !trail.rancolinrange;
                    changed = true;
                }

                ImGui.textWrapped("Start Transparency : End Transparency");
                float[] trans = {trail.transparancy1, trail.transparancy2};
                if (ImGui.sliderFloat2("##0t", trans, 0f, 1f)) {
                    trail.transparancy1 = trans[0];
                    trail.transparancy2 = trans[1];
                    changed = true;
                }

                ImGui.textWrapped("Start Scale : End Scale");
                float[] scale = {trail.scale1, trail.scale2};
                if (ImGui.sliderFloat2("##1t", scale, 0f, 10f)) {
                    trail.scale1 = scale[0];
                    trail.scale2 = scale[1];
                    changed = true;
                }

                trail.lifetime = sliderInt("Lifetime", "##2t", trail.lifetime, 0, 150);
                trail.gravity = sliderFloat("Gravity", "##3t", trail.gravity, 0f, 10f);
                trail.ranoffset = sliderFloat("Random Offset", "##4t", trail.ranoffset, 0f, 2.5f);
            }

            if (changed) {
                PacketDistributor.sendToServer(new FireworkSettingsPayload(
                        level.dimension().location().toString(),
                        pos,
                        body.serializeNBT(level.registryAccess()),
                        ball.serializeNBT(level.registryAccess()),
                        trail.serializeNBT(level.registryAccess())));
            }
            ImGui.end();
        }
    }

    private Color colorPicker(String label, Color color) {
        float[] rgb = color.getColorComponents(new float[3]);
        if (ImGui.colorPicker3(label, rgb)) {
            changed = true;
            return new Color(rgb[0], rgb[1], rgb[2]);
        }
        return color;
    }

    private float sliderFloat(String label, String id, float value, float min, float max) {
        ImGui.textWrapped(label);
        float[] holder = {value};
        if (ImGui.sliderFloat(id, holder, min, max)) {
            changed = true;
        }
        return holder[0];
    }

    private int sliderInt(String label, String id, int value, int min, int max) {
        ImGui.textWrapped(label);
        int[] holder = {value};
        if (ImGui.sliderInt(id, holder, min, max)) {
            changed = true;
        }
        return holder[0];
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
