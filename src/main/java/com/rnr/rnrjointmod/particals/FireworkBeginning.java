package com.rnr.rnrjointmod.particals;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Random;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.function.Consumer;

public class FireworkBeginning {
    public static Consumer<LodestoneWorldParticle> consumer = null;

    public Level level;
    public Vec3 pos;
    public FireworkBall fireworkBall = null;
    public Trail trail = null;
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


    public FireworkBeginning(Level level, Vec3 pos){
        this.pos = pos;
        this.level = level;
    }


    public void BodyParticles() {
        float range = 10;

        consumer = particle -> {
            particle.setParticleSpeed(particle.getParticleSpeed().add(0,this.upwardsAcceleration,0));
            if(this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()){
                this.trail.pos = particle.getParticlePosition();
                this.trail.spawnTrail();
            }
            System.out.println(particle.getLifetime() - particle.getAge());
            if(particle.getAge() == particle.getLifetime() && this.fireworkBall != null){
                this.fireworkBall.middlePos = particle.getParticlePosition();
                this.fireworkBall.explodeFirework();
            }
        };
        Color startingColor = col1;
        Color endingColor = col2;
        if(rancolinrange){  // ill leave this for later
            long ranseed = Random.newSeed();
        }
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(this.transparancy1, this.transparancy2).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(this.lifetime)
                .setScaleData(GenericParticleData.create(this.scale1, this.scale2).build())
                .setRandomOffset(this.ranoffset)
                .enableForcedSpawn()
                .addTickActor(consumer)
                .setGravity(this.gravity)
                .setFullBrightLighting()
                .setMotion(0, this.initalUpVelo ,0)
                .repeat(this.level, this.pos, this.count);
    }

}
