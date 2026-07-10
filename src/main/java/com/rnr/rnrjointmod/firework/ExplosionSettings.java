package com.rnr.rnrjointmod.firework;

import dev.chocoboy.cascade.Vfx;
import dev.chocoboy.cascade.VfxEmitter;
import dev.chocoboy.cascade.engine.effect.BlendMode;
import dev.chocoboy.cascade.engine.effect.SpriteId;
import dev.chocoboy.cascade.engine.emitter.ShapeSpec;
import dev.chocoboy.cascade.engine.tween.Easings;
import net.minecraft.nbt.CompoundTag;

/**
 * All options for the explosion burst of a firework. Pure data + NBT;
 * {@link #build()} turns it into a Cascade emitter.
 */
public class ExplosionSettings {

    public static final String[] SHAPE_NAMES = {"Sphere (Peony)", "Ring", "Disc", "Hemisphere", "Point"};
    public static final String[] SPRITE_NAMES = {"Glow", "Spark", "Star", "Smoke", "Ring", "Shard"};
    public static final String[] MOTION_NAMES = {"Outward burst", "Implode (inward)", "Orbit (swirl)"};

    // shape / scale
    public int shape = 0;
    public float radius = 2.0f;
    public int count = 250;
    public float speed = 0.35f;
    public int lifetime = 55;

    // colors: 2-4 gradient stops, RGB ints
    public int colorCount = 2;
    public int[] colors = {0xFFD740, 0xFF3300, 0xFFFFFF, 0xFFFFFF};

    // style
    public int sprite = 1;          // SPARK
    public boolean glow = true;     // additive blending
    public boolean lit = false;     // use world lighting instead of full-bright
    public boolean twinkle = false; // animate sprite frames (strobe/flicker)
    public float stretch = 0f;      // streak along velocity
    public float spin = 0f;
    public float sizeStart = 0.35f;
    public float sizeEnd = 0.05f;
    public float alphaStart = 1.0f;
    public float alphaEnd = 0.0f;

    // motion
    public int motionMode = 0;      // outward / implode / orbit
    public float gravity = 0.02f;   // downward acceleration per tick
    public float drag = 0.04f;
    public float turbulence = 0f;
    public float curlNoise = 0f;
    public float vortex = 0f;

    // extras
    public int trailLength = 0;     // ribbon through last N positions
    public boolean crackle = false; // secondary spark pops where particles die
    public int crackleCount = 8;
    public int crackleColor = 0xFFFFFF;

    public ShapeSpec shapeSpec() {
        return switch (shape) {
            case 1 -> ShapeSpec.ring(radius);
            case 2 -> ShapeSpec.disc(radius);
            case 3 -> ShapeSpec.hemisphere(radius);
            case 4 -> ShapeSpec.point();
            default -> ShapeSpec.sphere(radius);
        };
    }

    public SpriteId spriteId() {
        return switch (sprite) {
            case 1 -> SpriteId.SPARK;
            case 2 -> SpriteId.STAR;
            case 3 -> SpriteId.SMOKE;
            case 4 -> SpriteId.RING;
            case 5 -> SpriteId.SHARD;
            default -> SpriteId.GLOW;
        };
    }

    public VfxEmitter build() {
        VfxEmitter emitter = Vfx.emitter()
                .shape(shapeSpec())
                .count(count)
                .lifetime(lifetime)
                .speed(speed)
                .size(sizeStart, sizeEnd, Easings.EASE_OUT_QUAD)
                .alpha(alphaStart, alphaEnd, Easings.LINEAR)
                .gradient(Easings.LINEAR, activeColors())
                .sprite(spriteId())
                .blend(glow ? BlendMode.ADDITIVE : BlendMode.ALPHA)
                .gravity(0f, -gravity, 0f);
        if (drag > 0) {
            emitter.drag(drag);
        }
        if (turbulence > 0) {
            emitter.turbulence(turbulence, 1.5f);
        }
        if (curlNoise > 0) {
            emitter.curl(curlNoise, 1.5f);
        }
        if (vortex != 0) {
            emitter.vortex(0f, 0f, 0f, vortex);
        }
        if (motionMode == 1) {
            emitter.implode();
        } else if (motionMode == 2) {
            emitter.orbit();
        }
        if (lit) {
            emitter.lit();
        }
        if (twinkle) {
            emitter.animate();
        }
        if (stretch > 0) {
            emitter.stretch(stretch);
        }
        if (spin > 0) {
            emitter.spin(spin);
        }
        if (trailLength > 0) {
            emitter.trail(trailLength);
        }
        if (crackle) {
            emitter.burstOnDeath(Vfx.emitter()
                    .shape(ShapeSpec.point())
                    .count(crackleCount)
                    .lifetime(10)
                    .speed(0.18f)
                    .size(0.15f, 0f, Easings.EASE_OUT_QUAD)
                    .alpha(1f, 0f, Easings.LINEAR)
                    .gradient(Easings.LINEAR, 0xFFFFFF, crackleColor)
                    .sprite(SpriteId.SPARK)
                    .blend(BlendMode.ADDITIVE)
                    .gravity(0f, -0.03f, 0f));
        }
        return emitter;
    }

    public int[] activeColors() {
        int[] stops = new int[Math.max(2, Math.min(colorCount, colors.length))];
        System.arraycopy(colors, 0, stops, 0, stops.length);
        return stops;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("shape", shape);
        tag.putFloat("radius", radius);
        tag.putInt("count", count);
        tag.putFloat("speed", speed);
        tag.putInt("lifetime", lifetime);
        tag.putInt("colorcount", colorCount);
        tag.putIntArray("colors", colors.clone());
        tag.putInt("sprite", sprite);
        tag.putBoolean("glow", glow);
        tag.putBoolean("lit", lit);
        tag.putBoolean("twinkle", twinkle);
        tag.putFloat("stretch", stretch);
        tag.putFloat("spin", spin);
        tag.putFloat("sizestart", sizeStart);
        tag.putFloat("sizeend", sizeEnd);
        tag.putFloat("alphastart", alphaStart);
        tag.putFloat("alphaend", alphaEnd);
        tag.putInt("motionmode", motionMode);
        tag.putFloat("gravity", gravity);
        tag.putFloat("drag", drag);
        tag.putFloat("turbulence", turbulence);
        tag.putFloat("curl", curlNoise);
        tag.putFloat("vortex", vortex);
        tag.putInt("traillength", trailLength);
        tag.putBoolean("crackle", crackle);
        tag.putInt("cracklecount", crackleCount);
        tag.putInt("cracklecolor", crackleColor);
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.isEmpty()) {
            return;
        }
        shape = tag.getInt("shape");
        radius = tag.getFloat("radius");
        count = tag.getInt("count");
        speed = tag.getFloat("speed");
        lifetime = tag.getInt("lifetime");
        colorCount = Math.max(2, Math.min(tag.getInt("colorcount"), 4));
        int[] loaded = tag.getIntArray("colors");
        System.arraycopy(loaded, 0, colors, 0, Math.min(loaded.length, colors.length));
        sprite = tag.getInt("sprite");
        glow = tag.getBoolean("glow");
        lit = tag.getBoolean("lit");
        twinkle = tag.getBoolean("twinkle");
        stretch = tag.getFloat("stretch");
        spin = tag.getFloat("spin");
        sizeStart = tag.getFloat("sizestart");
        sizeEnd = tag.getFloat("sizeend");
        alphaStart = tag.getFloat("alphastart");
        alphaEnd = tag.getFloat("alphaend");
        motionMode = tag.getInt("motionmode");
        gravity = tag.getFloat("gravity");
        drag = tag.getFloat("drag");
        turbulence = tag.getFloat("turbulence");
        curlNoise = tag.getFloat("curl");
        vortex = tag.getFloat("vortex");
        trailLength = tag.getInt("traillength");
        crackle = tag.getBoolean("crackle");
        crackleCount = tag.getInt("cracklecount");
        crackleColor = tag.getInt("cracklecolor");
    }
}
