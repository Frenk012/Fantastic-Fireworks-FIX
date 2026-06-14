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

public class FireworkParticles {
    public static Consumer<LodestoneWorldParticle> consumer = null;

    //defaults
    public static boolean defaultTrail = false;
    public static Color defaultCol1 = new Color(137, 7, 181);
    public static Color defaultCol2 = new Color(0, 0, 0);
    public static boolean defaultRancolinrange = false;
    public static int defaultCount = 1;
    public static float defaultTransparancy1 = 0.75f;
    public static float defaultTransparancy2 = 0.15f;
    public static float defaultScale1 = 1.0f;
    public static float defaultScale2 = 0.0f;
    public static int defaultLifetime = 50;
    public static float defaultGravity = 3.5f;
    public static float defaultRanoffset = 0.9f;

    public static void generateSphere(Level level, int n, Vec3 middlePos,  boolean trail, Color col1, Color col2, boolean rancolinrange, int count, float transparancy1, float transparancy2, float scale1, float scale2, int lifetime, float gravity, float ranoffset){
        double goldenRatio = 1 + Math.sqrt(5) / 4;
        double angleIncrement = 3.141592653 * 2 * goldenRatio;
        double mult = 3;
        for(int i = 0; i < n; i++){
            float dist = (float) i /n;
            double incline = Math.acos(1 - 2 * dist);
            double azimuth = angleIncrement * i;

            double x = Math.sin(incline)*Math.cos(azimuth)*mult;
            double y = Math.sin(incline)*Math.sin(azimuth)*mult;
            double z = Math.cos(incline)*mult;
            Vec3 pos = new Vec3(x, y, z);
            System.out.println(middlePos);
            pos = pos.add(middlePos);
            spawnMainParticles(level, pos, middlePos, trail, col1, col2, rancolinrange, count, transparancy1, transparancy2, scale1, scale2, lifetime, gravity, ranoffset);
        }
    }
    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }


    public static Vec3 repulsionFeild (Level level, LodestoneWorldParticle particle, Vec3 middlePos, double range, boolean trail){
        Vec3 pos = particle.getParticlePosition();
        Vec3 velo = particle.getParticleSpeed();
        int age = particle.getAge();
        double k = 1.0;


        Vec3 vec3dist = middlePos.subtract(pos);

        double mag = Math.sqrt(vec3dist.x * vec3dist.x + vec3dist.y * vec3dist.y + vec3dist.z * vec3dist.z);

        if(mag <= range && age <= 20 ){
            vec3dist = vec3dist.normalize();
            vec3dist = vec3dist.scale(k * map(mag, 0, range, -1, 0));
            velo = velo.add(vec3dist).add(0, 0.2, 0);

            //System.out.println("hi" + vec3dist);
        }else {
            velo = velo.scale(0.95);
        }
        return velo;
    }

    public static void addTrail(Level level, LodestoneWorldParticle particle, Color col1, Color col2, boolean rancolinrange, int count, float transparancy1, float transparancy2, float scale1, float scale2, int lifetime, float gravity, float ranoffset){
        Vec3 pos = particle.getParticlePosition();
        if (pos != particle.getOldParticlePosition()) {
            spawnTrail(level, pos, col1, col2, rancolinrange, count, transparancy1, transparancy2, scale1, scale2, lifetime, gravity, ranoffset);

        }
    }


    public static void spawnMainParticles(Level level, Vec3 pos, Vec3 middlePos, boolean trail, Color col1, Color col2, boolean rancolinrange, int count, float transparancy1, float transparancy2, float scale1, float scale2, int lifetime, float gravity, float ranoffset) {
        float range = 10;

        consumer = particle -> {
            particle.setParticleSpeed(repulsionFeild(level, particle, middlePos, range, trail));
            if(trail){
                addTrail(level, particle, col1, col2, rancolinrange, count, transparancy1, transparancy2, scale1, scale2, lifetime, gravity, ranoffset);
            }
        };
        Color startingColor = col1;
        Color endingColor = col2;
        if(rancolinrange){  // ill leave this for later
            long ranseed = Random.newSeed();
        }
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(transparancy1, transparancy2).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(lifetime)
                .setScaleData(GenericParticleData.create(scale1, scale2).build())
                .setRandomOffset(ranoffset)
                .enableForcedSpawn()
                .addTickActor(consumer)
                .setGravity(gravity)
                .setFullBrightLighting()
                .repeat(level, pos, count);
    }

    public static void spawnTrail(Level level, Vec3 pos, Color col1, Color col2, boolean rancolinrange, int count, float transparancy1, float transparancy2, float scale1, float scale2, int lifetime, float gravity, float ranoffset) {
        Vec3 finalPos = pos;
        Color startingColor = col1;
        Color endingColor = col2;
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(transparancy1, transparancy2).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(lifetime)
                .setScaleData(GenericParticleData.create(scale1, scale2).build())
                //.enableNoClip()
                .setRandomOffset(ranoffset)
                .setGravity(gravity)
                .setFullBrightLighting()
                .enableForcedSpawn()
                .repeat(level, pos, count);

        //}
    }

        public static void BodyParticles(Level level, Vec3 pos, Trail trail, Color col1, Color col2, boolean rancolinrange, int count, float transparancy1, float transparancy2, float scale1, float scale2, int lifetime, float gravity, float ranoffset, float upwardsAcceleration, float initalUpVelo) {
        float range = 10;

        consumer = particle -> {
            particle.setParticleSpeed(particle.getParticleSpeed().add(0,upwardsAcceleration,0));
            if(trail != null){

            }
        };
        Color startingColor = col1;
        Color endingColor = col2;
        if(rancolinrange){  // ill leave this for later
            long ranseed = Random.newSeed();
        }
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(transparancy1, transparancy2).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(lifetime)
                .setScaleData(GenericParticleData.create(scale1, scale2).build())
                .setRandomOffset(ranoffset)
                .enableForcedSpawn()
                .addTickActor(consumer)
                .setGravity(gravity)
                .setFullBrightLighting()
                .setMotion(0, initalUpVelo ,0)
                .repeat(level, pos, count);
    }


//    public static void createFirework(Level level, Vec3 startPos, ){
//
//    }




}
