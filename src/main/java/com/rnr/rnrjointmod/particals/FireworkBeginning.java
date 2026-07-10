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

public class FireworkBeginning implements INBTSerializable<CompoundTag> {

    public Level level;
    public Vec3 pos;
    public FireworkBall fireworkBall = null;
    public Trail trail = null;
    public boolean usetrail = true;
    public Color col1 = new Color(137, 7, 181);
    public Color col2 = new Color(0, 0, 0);
    public boolean rancolinrange = false;
    public int count = 1;
    public float transparancy1 = 0.75f;
    public float transparancy2 = 0.15f;
    public float scale1 = 1.0f;
    public float scale2 = 0.0f;
    public int lifetime = 50;
    public float gravity = 3.5f;
    public float ranoffset = 0.9f;
    public float initalUpVelo = 3.0f;
    public float upwardsAcceleration = 0.2f;

    public FireworkBeginning(Level level, Vec3 pos) {
        this.pos = pos;
        this.level = level;
    }

    public void BodyParticles() {
        if (level == null || !level.isClientSide) {
            return;
        }
        Consumer<LodestoneWorldParticle> tickActor = particle -> {
            particle.setParticleSpeed(particle.getParticleSpeed().add(0, this.upwardsAcceleration, 0));
            if (this.usetrail && this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()) {
                this.trail.pos = particle.getParticlePosition();
                this.trail.spawnTrail();
            }
            if (particle.getAge() == particle.getLifetime() && this.fireworkBall != null) {
                this.fireworkBall.middlePos = particle.getParticlePosition();
                this.fireworkBall.explodeFirework();
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
                .setMotion(0, this.initalUpVelo, 0)
                .repeat(this.level, this.pos, this.count);
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
        tag.putFloat("initalupvelo", initalUpVelo);
        tag.putFloat("upwardsacceleration", upwardsAcceleration);
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
        this.initalUpVelo = tag.getFloat("initalupvelo");
        this.upwardsAcceleration = tag.getFloat("upwardsacceleration");
    }
}
