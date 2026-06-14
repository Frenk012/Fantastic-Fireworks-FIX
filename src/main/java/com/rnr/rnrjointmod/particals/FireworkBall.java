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

public class FireworkBall {
    public static Consumer<LodestoneWorldParticle> consumer = null;

    public Level level;
    public Vec3 pos;
    public Trail trail = null;
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


    public FireworkBall(Level level, Vec3 pos){
        this.pos = pos;
        this.level = level;
    }

    public void explodeFirework(){
        this.generateSphere();
    }

    public void generateSphere(){
        double goldenRatio = 1 + Math.sqrt(5) / 4;
        double angleIncrement = 3.141592653 * 2 * goldenRatio;
        double mult = 2;
        for(int i = 0; i < this.count; i++){
            float dist = (float) i /this.count;
            double incline = Math.acos(1 - 2 * dist);
            double azimuth = angleIncrement * i;

            double x = Math.sin(incline)*Math.cos(azimuth)*mult;
            double y = Math.sin(incline)*Math.sin(azimuth)*mult;
            double z = Math.cos(incline)*mult;
            Vec3 pos = new Vec3(x, y, z);
            System.out.println(this.middlePos);
            pos = pos.add(this.middlePos);
            this.BallParticles();
        }
    }
    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }


    public Vec3 repulsionFeild(LodestoneWorldParticle particle){
        Vec3 pos = particle.getParticlePosition();
        Vec3 velo = particle.getParticleSpeed();
        int age = particle.getAge();
        double k = 1.0;


        Vec3 vec3dist = this.middlePos.subtract(pos);

        double mag = Math.sqrt(vec3dist.x * vec3dist.x + vec3dist.y * vec3dist.y + vec3dist.z * vec3dist.z);

        if(mag <= this.range && age <= this.repulsionlength ){
            vec3dist = vec3dist.normalize();
            vec3dist = vec3dist.scale(k * map(mag, 0, this.range, -1, 0));
            velo = velo.add(vec3dist).add(0, 0.2, 0);

            //System.out.println("hi" + vec3dist);
        }else {
            velo = velo.scale(0.95);
        }
        return velo;
    }


    public void BallParticles() {
        consumer = particle -> {
            particle.setParticleSpeed(this.repulsionFeild(particle));
            if(this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()){
                this.trail.pos = particle.getParticlePosition();
                this.trail.spawnTrail();
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
                .addTickActor(this.consumer)
                .setGravity(this.gravity)
                .setFullBrightLighting()
                .repeat(this.level, this.pos, this.count);
    }


}
