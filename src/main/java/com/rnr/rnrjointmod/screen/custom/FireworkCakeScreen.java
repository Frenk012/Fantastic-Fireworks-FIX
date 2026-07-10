package com.rnr.rnrjointmod.screen.custom;

import com.rnr.rnrjointmod.Payloads.FireworkSettingsPayload;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.firework.ExplosionSettings;
import com.rnr.rnrjointmod.firework.FireworkPresets;
import com.rnr.rnrjointmod.firework.PresetStorage;
import com.rnr.rnrjointmod.firework.RocketSettings;
import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public class FireworkCakeScreen extends Screen {

    private final BlockPos pos;

    private final ImString cakeIdField = new ImString(64);
    private final ImString presetNameField = new ImString(64);
    private final ImInt builtinIndex = new ImInt(0);
    private final ImInt customIndex = new ImInt(0);
    private final ImInt shapeIndex = new ImInt(0);
    private final ImInt spriteIndex = new ImInt(0);
    private final ImInt rocketSpriteIndex = new ImInt(0);
    private final ImInt motionIndex = new ImInt(0);
    private List<String> customPresets = PresetStorage.names();
    private String statusMessage = "";

    private boolean changed;
    private boolean launchRequested;

    public FireworkCakeScreen(BlockPos pos) {
        super(Component.translatable("screen.rnrjointmod.firework_editor"));
        this.pos = pos;
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(pos) instanceof FireworkCakeEntity cake) {
            cakeIdField.set(cake.cakeId);
        }
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
            changed = false;
            launchRequested = false;

            ImGui.begin("Firework Editor");
            ImGui.textWrapped("Block: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());

            renderIdSection(cake);
            ImGui.separator();

            if (ImGui.button("Launch!")) {
                launchRequested = true;
            }
            ImGui.separator();

            renderPresetSection(cake);
            ImGui.separator();
            renderAutomationSection(cake);
            renderRocketSection(cake.rocket);
            renderExplosionSection(cake.explosion);

            if (!statusMessage.isEmpty()) {
                ImGui.separator();
                ImGui.textWrapped(statusMessage);
            }

            if (changed || launchRequested) {
                PacketDistributor.sendToServer(new FireworkSettingsPayload(
                        level.dimension().location().toString(), pos,
                        cake.buildSyncTag(level.registryAccess()), launchRequested));
            }
            ImGui.end();
        }
    }

    private void renderRocketSection(RocketSettings rocket) {
        if (!ImGui.collapsingHeader("Rocket (ascent)")) {
            return;
        }
        rocket.explosionHeight = dragFloat("Explosion Height (blocks)", "##rh", rocket.explosionHeight, 0.5f, 1f, 320f);
        rocket.speed = dragFloat("Climb Speed (blocks/tick)", "##rs", rocket.speed, 0.02f, 0.05f, 5f);
        rocket.spread = dragFloat("Wobble (0 = straight up)", "##rw", rocket.spread, 0.005f, 0f, 0.8f);
        rocket.trailLength = dragInt("Trail Length", "##rt", rocket.trailLength, 0, 40);
        rocket.size = dragFloat("Rocket Size", "##rz", rocket.size, 0.02f, 0.05f, 3f);

        rocketSpriteIndex.set(rocket.sprite);
        ImGui.textWrapped("Rocket Style");
        if (ImGui.combo("##rsp", rocketSpriteIndex, ExplosionSettings.SPRITE_NAMES)) {
            rocket.sprite = rocketSpriteIndex.get();
            changed = true;
        }
        if (ImGui.checkbox("Glow (additive)##r", rocket.glow)) {
            rocket.glow = !rocket.glow;
            changed = true;
        }
        rocket.colorStart = colorPicker("Rocket Color (start)", "##rc1", rocket.colorStart);
        rocket.colorEnd = colorPicker("Rocket Color (end)", "##rc2", rocket.colorEnd);
    }

    private void renderExplosionSection(ExplosionSettings ex) {
        if (!ImGui.collapsingHeader("Explosion")) {
            return;
        }
        shapeIndex.set(ex.shape);
        ImGui.textWrapped("Burst Shape");
        if (ImGui.combo("##esh", shapeIndex, ExplosionSettings.SHAPE_NAMES)) {
            ex.shape = shapeIndex.get();
            changed = true;
        }
        ex.radius = dragFloat("Burst Radius (blocks)", "##er", ex.radius, 0.05f, 0.1f, 32f);
        ex.count = dragInt("Particle Count", "##ec", ex.count, 1, 3000);
        ex.speed = dragFloat("Expansion Speed", "##es", ex.speed, 0.01f, 0f, 3f);
        ex.lifetime = dragInt("Particle Lifetime (ticks)", "##el", ex.lifetime, 1, 400);

        ImGui.separator();
        ImGui.textWrapped("Colors (gradient over particle life):");
        int[] stops = {ex.colorCount};
        if (ImGui.sliderInt("Color Stops##ecs", stops, 2, 4)) {
            ex.colorCount = stops[0];
            changed = true;
        }
        for (int i = 0; i < ex.colorCount; i++) {
            ex.colors[i] = colorPicker("Color " + (i + 1), "##ecol" + i, ex.colors[i]);
        }

        ImGui.separator();
        ImGui.textWrapped("Style:");
        spriteIndex.set(ex.sprite);
        if (ImGui.combo("Particle Look##esp", spriteIndex, ExplosionSettings.SPRITE_NAMES)) {
            ex.sprite = spriteIndex.get();
            changed = true;
        }
        if (ImGui.checkbox("Glow (additive, brighter)##e", ex.glow)) {
            ex.glow = !ex.glow;
            changed = true;
        }
        ImGui.sameLine();
        if (ImGui.checkbox("World-lit (dimmer)##e", ex.lit)) {
            ex.lit = !ex.lit;
            changed = true;
        }
        if (ImGui.checkbox("Twinkle / strobe##e", ex.twinkle)) {
            ex.twinkle = !ex.twinkle;
            changed = true;
        }
        ImGui.sameLine();
        if (ImGui.checkbox("Crackle (spark pops)##e", ex.crackle)) {
            ex.crackle = !ex.crackle;
            changed = true;
        }
        if (ex.crackle) {
            ex.crackleCount = dragInt("Crackle Sparks per Particle", "##ecc", ex.crackleCount, 1, 40);
            ex.crackleColor = colorPicker("Crackle Color", "##eccol", ex.crackleColor);
        }
        ex.stretch = dragFloat("Streaking (stretch along motion)", "##est", ex.stretch, 0.02f, 0f, 5f);
        ex.spin = dragFloat("Spin", "##esn", ex.spin, 0.01f, 0f, 2f);
        ex.trailLength = dragInt("Trail Length (0 = none)", "##etl", ex.trailLength, 0, 40);
        ex.sizeStart = dragFloat("Size at Birth", "##ez1", ex.sizeStart, 0.01f, 0f, 5f);
        ex.sizeEnd = dragFloat("Size at Death", "##ez2", ex.sizeEnd, 0.01f, 0f, 5f);
        ex.alphaStart = dragFloat("Opacity at Birth", "##ea1", ex.alphaStart, 0.01f, 0f, 1f);
        ex.alphaEnd = dragFloat("Opacity at Death", "##ea2", ex.alphaEnd, 0.01f, 0f, 1f);

        ImGui.separator();
        ImGui.textWrapped("Motion:");
        motionIndex.set(ex.motionMode);
        if (ImGui.combo("Burst Direction##em", motionIndex, ExplosionSettings.MOTION_NAMES)) {
            ex.motionMode = motionIndex.get();
            changed = true;
        }
        ex.gravity = dragFloat("Gravity (negative = rises)", "##eg", ex.gravity, 0.001f, -0.2f, 0.2f);
        ex.drag = dragFloat("Air Drag", "##ed", ex.drag, 0.002f, 0f, 0.5f);
        ex.turbulence = dragFloat("Turbulence (chaotic jitter)", "##etb", ex.turbulence, 0.002f, 0f, 0.5f);
        ex.curlNoise = dragFloat("Curl Noise (fluid swirl)", "##ecn", ex.curlNoise, 0.002f, 0f, 0.5f);
        ex.vortex = dragFloat("Vortex (spiral around center)", "##evx", ex.vortex, 0.002f, -0.5f, 0.5f);
    }

    private void renderIdSection(FireworkCakeEntity cake) {
        ImGui.textWrapped("Block ID (for /firework launchid):");
        ImGui.inputText("##cakeid", cakeIdField);
        ImGui.sameLine();
        if (ImGui.button("Apply ID")) {
            String newId = cakeIdField.get().trim();
            if (!newId.isEmpty()) {
                cake.cakeId = newId;
                changed = true;
                statusMessage = "ID set to '" + newId + "'";
            }
        }
    }

    private void renderPresetSection(FireworkCakeEntity cake) {
        ImGui.textWrapped("Built-in Presets:");
        String[] builtinNames = FireworkPresets.BUILT_IN.stream().map(FireworkPresets.Preset::name).toArray(String[]::new);
        ImGui.combo("##builtin", builtinIndex, builtinNames);
        ImGui.sameLine();
        if (ImGui.button("Apply##builtin")) {
            FireworkPresets.BUILT_IN.get(builtinIndex.get()).apply().accept(cake);
            changed = true;
            statusMessage = "Applied preset '" + builtinNames[builtinIndex.get()] + "'";
        }

        ImGui.textWrapped("My Presets:");
        if (customPresets.isEmpty()) {
            ImGui.textDisabled("(none saved yet)");
        } else {
            if (customIndex.get() >= customPresets.size()) {
                customIndex.set(0);
            }
            ImGui.combo("##custom", customIndex, customPresets.toArray(new String[0]));
            ImGui.sameLine();
            if (ImGui.button("Apply##custom")) {
                String name = customPresets.get(customIndex.get());
                if (PresetStorage.apply(name, cake)) {
                    changed = true;
                    statusMessage = "Applied preset '" + name + "'";
                }
            }
            ImGui.sameLine();
            if (ImGui.button("Delete##custom")) {
                PresetStorage.delete(customPresets.get(customIndex.get()));
                customPresets = PresetStorage.names();
                statusMessage = "Preset deleted";
            }
        }

        ImGui.inputTextWithHint("##presetname", "new preset name", presetNameField);
        ImGui.sameLine();
        if (ImGui.button("Save Current")) {
            String name = presetNameField.get().trim();
            if (!name.isEmpty()) {
                PresetStorage.save(name, cake);
                customPresets = PresetStorage.names();
                statusMessage = "Saved preset '" + name + "'";
            } else {
                statusMessage = "Enter a preset name first";
            }
        }
    }

    private void renderAutomationSection(FireworkCakeEntity cake) {
        if (!ImGui.collapsingHeader("Automation (timer)")) {
            return;
        }
        if (ImGui.checkbox("Launch automatically", cake.autoLaunch)) {
            cake.autoLaunch = !cake.autoLaunch;
            changed = true;
        }
        float[] seconds = {cake.intervalTicks / 20f};
        ImGui.textWrapped("Every N seconds:");
        if (ImGui.dragFloat("##interval", seconds, 0.1f, 0.25f, 3600f)) {
            cake.intervalTicks = Math.max((int) (seconds[0] * 20), 5);
            changed = true;
        }
    }

    /** Color stored as 0xRRGGBB int. */
    private int colorPicker(String label, String id, int rgbInt) {
        ImGui.textWrapped(label);
        Color color = new Color(rgbInt);
        float[] rgb = color.getColorComponents(new float[3]);
        if (ImGui.colorEdit3(id, rgb)) {
            changed = true;
            return new Color(rgb[0], rgb[1], rgb[2]).getRGB() & 0xFFFFFF;
        }
        return rgbInt;
    }

    private float dragFloat(String label, String id, float value, float speed, float min, float max) {
        ImGui.textWrapped(label);
        float[] holder = {value};
        if (ImGui.dragFloat(id, holder, speed, min, max)) {
            changed = true;
        }
        return holder[0];
    }

    private int dragInt(String label, String id, int value, int min, int max) {
        ImGui.textWrapped(label);
        int[] holder = {value};
        if (ImGui.dragInt(id, holder, 1, min, max)) {
            changed = true;
        }
        return holder[0];
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
