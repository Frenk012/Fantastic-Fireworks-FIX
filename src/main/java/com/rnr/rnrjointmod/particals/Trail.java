package com.rnr.rnrjointmod.particals;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;

public class Trail {

    public Level level;
    public Vec3 pos;
    public Color col1 = new Color(137, 7, 181);
    public Color col2 = new Color(0, 0, 0);
    public boolean rancolinrange = false;
    public int count = 1;
    public float transparancy1 = 0.75f;
    public float transparancy2 = 0.0f;
    public float scale1 = 0.5f;
    public float scale2 = 0.0f;
    public int lifetime = 10;
    public float gravity = 0.5f;
    public float ranoffset = 0.3f;


    public Trail(Level level, Vec3 pos){
        this.level = level;
        this.pos = pos;
    }


    public void spawnTrail() {

        Color startingColor = this.col1;
        Color endingColor = this.col2;
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
            .setTransparencyData(GenericParticleData.create(this.transparancy1, this.transparancy2).build())
            .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
            .setLifetime(this.lifetime)
            .setScaleData(GenericParticleData.create(this.scale1, this.scale2).build())
            .setRandomOffset(this.ranoffset)
            .setGravity(this.gravity)
            .setFullBrightLighting()
            .enableForcedSpawn()
            .repeat(this.level, this.pos, this.count);
    }
}

