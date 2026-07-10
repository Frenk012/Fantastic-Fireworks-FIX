package com.rnr.rnrjointmod.particals;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.function.Consumer;

public class FireworkBall implements INBTSerializable<CompoundTag> {

    public Level level;
    public Vec3 pos;
    public Trail trail = null;
    public boolean usetrail = true;
    public Color col1 = new Color(137, 7, 181);
    public Color col2 = new Color(0, 0, 0);
    public boolean rancolinrange = false;
    public int count = 50;
    public float transparancy1 = 0.75f;
    public float transparancy2 = 0.15f;
    public float scale1 = 1.0f;
    public float scale2 = 0.0f;
    public int lifetime = 50;
    public float gravity = 3.5f;
    public float ranoffset = 0.9f;
    public Vec3 middlePos;
    public float range = 10;
    public int repulsionlength = 20;

    public FireworkBall(Level level, Vec3 middlePos) {
        this.middlePos = middlePos;
        this.level = level;
    }

    public void explodeFirework() {
        generateSphere();
    }

    /** Spawns {@code count} particles evenly distributed on a sphere (golden-ratio spiral). */
    public void generateSphere() {
        double goldenRatio = 1 + Math.sqrt(5) / 4;
        double angleIncrement = Math.PI * 2 * goldenRatio;
        double mult = 2;
        for (int i = 0; i < this.count; i++) {
            float dist = (float) i / this.count;
            double incline = Math.acos(1 - 2 * dist);
            double azimuth = angleIncrement * i;

            double x = Math.sin(incline) * Math.cos(azimuth) * mult;
            double y = Math.sin(incline) * Math.sin(azimuth) * mult;
            double z = Math.cos(incline) * mult;
            this.pos = new Vec3(x, y, z).add(this.middlePos);
            this.BallParticles();
        }
    }

    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public Vec3 repulsionField(LodestoneWorldParticle particle) {
        Vec3 particlePos = particle.getParticlePosition();
        Vec3 velo = particle.getParticleSpeed();
        int age = particle.getAge();
        double k = 1.0;

        Vec3 dist = this.middlePos.subtract(particlePos);
        double mag = dist.length();

        if (mag <= this.range && age <= this.repulsionlength) {
            dist = dist.normalize().scale(k * map(mag, 0, this.range, -1, 0));
            velo = velo.add(dist).add(0, 0.2, 0);
        } else {
            velo = velo.scale(0.95);
        }
        return velo;
    }

    public void BallParticles() {
        if (level == null || !level.isClientSide) {
            return;
        }
        Consumer<LodestoneWorldParticle> tickActor = particle -> {
            particle.setParticleSpeed(this.repulsionField(particle));
            if (this.usetrail && this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()) {
                this.trail.pos = particle.getParticlePosition();
                this.trail.spawnTrail();
            }
        };
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(this.transparancy1, this.transparancy2).build())
                .setColorData(ColorParticleData.create(this.col1, this.col2).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(this.lifetime)
                .setScaleData(GenericParticleData.create(this.scale1, this.scale2).build())
                .setRandomOffset(this.ranoffset)
                .enableForcedSpawn()
                .addTickActor(tickActor)
                .setGravity(this.gravity)
                .setFullBrightLighting()
                .repeat(this.level, this.pos, 1);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("usetrail", usetrail);
        tag.putBoolean("rancolinrange", rancolinrange);
        tag.putInt("count", count);
        tag.putInt("col1", col1.getRGB());
        tag.putInt("col2", col2.getRGB());
        tag.putFloat("transparancy1", transparancy1);
        tag.putFloat("transparancy2", transparancy2);
        tag.putFloat("scale1", scale1);
        tag.putFloat("scale2", scale2);
        tag.putInt("lifetime", lifetime);
        tag.putFloat("gravity", gravity);
        tag.putFloat("ranoffset", ranoffset);
        tag.putFloat("range", range);
        tag.putInt("repulsionlength", repulsionlength);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.isEmpty()) {
            return;
        }
        this.usetrail = tag.getBoolean("usetrail");
        this.rancolinrange = tag.getBoolean("rancolinrange");
        this.count = tag.getInt("count");
        this.col1 = new Color(tag.getInt("col1"));
        this.col2 = new Color(tag.getInt("col2"));
        this.transparancy1 = tag.getFloat("transparancy1");
        this.transparancy2 = tag.getFloat("transparancy2");
        this.scale1 = tag.getFloat("scale1");
        this.scale2 = tag.getFloat("scale2");
        this.lifetime = tag.getInt("lifetime");
        this.gravity = tag.getFloat("gravity");
        this.ranoffset = tag.getFloat("ranoffset");
        this.range = tag.getFloat("range");
        this.repulsionlength = tag.getInt("repulsionlength");
    }
}
