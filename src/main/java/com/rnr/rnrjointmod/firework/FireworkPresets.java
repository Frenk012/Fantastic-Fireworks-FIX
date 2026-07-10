package com.rnr.rnrjointmod.firework;

import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;

import java.util.List;
import java.util.function.Consumer;

/** Built-in firework designs, modeled on real firework types. */
public class FireworkPresets {

    public record Preset(String name, Consumer<FireworkCakeEntity> apply) {
    }

    public static final List<Preset> BUILT_IN = List.of(
            new Preset("Golden Peony", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0xFFE9A8;
                cake.explosion.colors[1] = 0xFF8C00;
                cake.explosion.count = 350;
                cake.explosion.gravity = 0.015f;
            }),
            new Preset("Willow", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0xFFD700;
                cake.explosion.colors[1] = 0xB8860B;
                cake.explosion.count = 280;
                cake.explosion.lifetime = 110;
                cake.explosion.gravity = 0.03f;
                cake.explosion.drag = 0.08f;
                cake.explosion.trailLength = 6;
                cake.explosion.stretch = 1.2f;
            }),
            new Preset("Chrysanthemum", cake -> {
                base(cake);
                cake.explosion.colorCount = 3;
                cake.explosion.colors[0] = 0xFFFFFF;
                cake.explosion.colors[1] = 0xFF66CC;
                cake.explosion.colors[2] = 0x9900CC;
                cake.explosion.count = 400;
                cake.explosion.trailLength = 4;
            }),
            new Preset("Ring Shell", cake -> {
                base(cake);
                cake.explosion.shape = 1; // ring
                cake.explosion.colors[0] = 0x33CCFF;
                cake.explosion.colors[1] = 0x0033AA;
                cake.explosion.count = 200;
                cake.explosion.speed = 0.25f;
            }),
            new Preset("Crackling Palm", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0xFFB347;
                cake.explosion.colors[1] = 0xCC5500;
                cake.explosion.count = 60;
                cake.explosion.sizeStart = 0.6f;
                cake.explosion.trailLength = 10;
                cake.explosion.stretch = 2f;
                cake.explosion.crackle = true;
                cake.explosion.crackleCount = 12;
                cake.explosion.crackleColor = 0xFFF2B0;
            }),
            new Preset("Strobe Shell", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0xFFFFFF;
                cake.explosion.colors[1] = 0xCCE5FF;
                cake.explosion.count = 300;
                cake.explosion.twinkle = true;
                cake.explosion.sprite = 2; // star
                cake.explosion.lifetime = 80;
                cake.explosion.gravity = 0.025f;
            }),
            new Preset("Aurora Swirl", cake -> {
                base(cake);
                cake.explosion.colorCount = 4;
                cake.explosion.colors[0] = 0x50FFC0;
                cake.explosion.colors[1] = 0x40C0FF;
                cake.explosion.colors[2] = 0x8060FF;
                cake.explosion.colors[3] = 0xFF60D0;
                cake.explosion.count = 450;
                cake.explosion.motionMode = 2; // orbit
                cake.explosion.vortex = 0.06f;
                cake.explosion.curlNoise = 0.03f;
                cake.explosion.lifetime = 100;
                cake.explosion.gravity = 0.005f;
                cake.rocket.explosionHeight = 45f;
            }),
            new Preset("Crimson Dome", cake -> {
                base(cake);
                cake.explosion.shape = 3; // hemisphere
                cake.explosion.colors[0] = 0xFF3030;
                cake.explosion.colors[1] = 0x600000;
                cake.explosion.count = 320;
                cake.explosion.speed = 0.3f;
            }),
            new Preset("Ghost Smoke", cake -> {
                base(cake);
                cake.explosion.sprite = 3; // smoke
                cake.explosion.glow = false;
                cake.explosion.lit = true;
                cake.explosion.colors[0] = 0xE8F0F0;
                cake.explosion.colors[1] = 0x90A8B0;
                cake.explosion.alphaStart = 0.5f;
                cake.explosion.count = 150;
                cake.explosion.sizeStart = 0.9f;
                cake.explosion.sizeEnd = 1.6f;
                cake.explosion.gravity = -0.004f; // rises
                cake.explosion.drag = 0.06f;
            }),
            new Preset("Emerald Rain", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0x40FF80;
                cake.explosion.colors[1] = 0x006030;
                cake.explosion.count = 350;
                cake.explosion.gravity = 0.05f;
                cake.explosion.lifetime = 90;
                cake.explosion.stretch = 1.5f;
            }),
            new Preset("Violet Nova", cake -> {
                base(cake);
                cake.explosion.colorCount = 3;
                cake.explosion.colors[0] = 0xFFFFFF;
                cake.explosion.colors[1] = 0xC060FF;
                cake.explosion.colors[2] = 0x400090;
                cake.explosion.count = 500;
                cake.explosion.radius = 3f;
                cake.explosion.speed = 0.5f;
                cake.explosion.sizeStart = 0.45f;
            }),
            new Preset("Firefly Swarm", cake -> {
                base(cake);
                cake.explosion.colors[0] = 0xFFFF80;
                cake.explosion.colors[1] = 0x80FF40;
                cake.explosion.count = 220;
                cake.explosion.speed = 0.15f;
                cake.explosion.turbulence = 0.05f;
                cake.explosion.twinkle = true;
                cake.explosion.gravity = 0f;
                cake.explosion.drag = 0.03f;
                cake.explosion.lifetime = 120;
                cake.explosion.sizeStart = 0.2f;
            })
    );

    /** Shared defaults every preset starts from. */
    private static void base(FireworkCakeEntity cake) {
        RocketSettings rocket = cake.rocket;
        rocket.speed = 0.8f;
        rocket.explosionHeight = 30f;
        rocket.spread = 0.06f;
        rocket.trailLength = 8;
        rocket.colorStart = 0xFFE9A8;
        rocket.colorEnd = 0xFF8C00;
        rocket.sprite = 1;
        rocket.glow = true;
        rocket.size = 0.35f;

        ExplosionSettings ex = cake.explosion;
        ex.shape = 0;
        ex.radius = 2f;
        ex.count = 250;
        ex.speed = 0.35f;
        ex.lifetime = 55;
        ex.colorCount = 2;
        ex.colors = new int[]{0xFFD740, 0xFF3300, 0xFFFFFF, 0xFFFFFF};
        ex.sprite = 1;
        ex.glow = true;
        ex.lit = false;
        ex.twinkle = false;
        ex.stretch = 0f;
        ex.spin = 0f;
        ex.sizeStart = 0.35f;
        ex.sizeEnd = 0.05f;
        ex.alphaStart = 1f;
        ex.alphaEnd = 0f;
        ex.motionMode = 0;
        ex.gravity = 0.02f;
        ex.drag = 0.04f;
        ex.turbulence = 0f;
        ex.curlNoise = 0f;
        ex.vortex = 0f;
        ex.trailLength = 0;
        ex.crackle = false;
        ex.crackleCount = 8;
        ex.crackleColor = 0xFFFFFF;
    }
}
