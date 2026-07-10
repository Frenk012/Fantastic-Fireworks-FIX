package com.rnr.rnrjointmod.firework;

import dev.chocoboy.cascade.Vfx;
import dev.chocoboy.cascade.VfxEmitter;
import dev.chocoboy.cascade.engine.effect.BlendMode;
import dev.chocoboy.cascade.engine.effect.SpriteId;
import dev.chocoboy.cascade.engine.emitter.ShapeSpec;
import dev.chocoboy.cascade.engine.tween.Easings;
import net.minecraft.nbt.CompoundTag;

/**
 * Options for the ascending rocket. {@link #build} produces a single-particle
 * jet emitter whose death (at the configured height) triggers the explosion.
 */
public class RocketSettings {

    public float speed = 0.8f;          // blocks per tick upward
    public float explosionHeight = 30f; // blocks above the cake
    public float spread = 0.06f;        // jet cone half-angle (radians) - slight wobble
    public int trailLength = 8;         // ribbon behind the rocket
    public int colorStart = 0xFFE9A8;
    public int colorEnd = 0xFF8C00;
    public int sprite = 1;              // SPARK
    public boolean glow = true;
    public float size = 0.35f;

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

    /** Ticks the rocket needs to reach the explosion height. */
    public int flightTicks() {
        return Math.max((int) Math.ceil(explosionHeight / Math.max(speed, 0.05f)), 1);
    }

    public VfxEmitter build(VfxEmitter explosion) {
        VfxEmitter emitter = Vfx.emitter()
                .shape(ShapeSpec.point())
                .count(1)
                .jet(0f, 1f, 0f, spread)
                .speed(speed)
                .lifetime(flightTicks())
                .size(size, size, Easings.LINEAR)
                .alpha(1f, 1f, Easings.LINEAR)
                .gradient(Easings.LINEAR, colorStart, colorEnd)
                .sprite(spriteId())
                .blend(glow ? BlendMode.ADDITIVE : BlendMode.ALPHA)
                .stretch(1.5f)
                .burstOnDeath(explosion);
        if (trailLength > 0) {
            emitter.trail(trailLength);
        }
        return emitter;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("speed", speed);
        tag.putFloat("explosionheight", explosionHeight);
        tag.putFloat("spread", spread);
        tag.putInt("traillength", trailLength);
        tag.putInt("colorstart", colorStart);
        tag.putInt("colorend", colorEnd);
        tag.putInt("sprite", sprite);
        tag.putBoolean("glow", glow);
        tag.putFloat("size", size);
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.isEmpty()) {
            return;
        }
        speed = tag.getFloat("speed");
        explosionHeight = tag.getFloat("explosionheight");
        spread = tag.getFloat("spread");
        trailLength = tag.getInt("traillength");
        colorStart = tag.getInt("colorstart");
        colorEnd = tag.getInt("colorend");
        sprite = tag.getInt("sprite");
        glow = tag.getBoolean("glow");
        size = tag.getFloat("size");
    }
}
