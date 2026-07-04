package com.rnr.rnrjointmod.particals;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Random;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.function.Consumer;

public class FireworkBall implements INBTSerializable<CompoundTag>{
    public static Consumer<LodestoneWorldParticle> consumer = null;

    public Level level;
    public Vec3 pos;
    public boolean usetrail = true;
    public Trail trail = null;                                 // In Gui
    public Color col1 = new Color(137, 7, 181);       // In Gui
    public Color col2 = new Color(0, 0, 0);           // In Gui
    public boolean rancolinrange = false;                      // In Gui
    public int count = 50;                                     // In Gui
    public float transparancy1 = 0.75f;                        // In Gui
    public float transparancy2 = 0.15f;                        // In Gui
    public float scale1 = 1.0f;                                 // In Gui
    public float scale2 = 0.0f;                                 // In Gui
    public int lifetime = 50;                                   // In Gui
    public float gravity = 3.5f;                                 // In Gui
    public float ranoffset = 0.9f;                               // In Gui
    public Vec3 middlePos;                                      // No Need X
    public float range = 10;                                     // In Gui
    public int repulsionlength = 20;                              // In Gui


    public FireworkBall(Level level, Vec3 middlePos){
        this.middlePos = middlePos;
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
            this.pos = new Vec3(x, y, z);
            this.pos = this.pos.add(this.middlePos);
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
        if (level.isClientSide) {
            consumer = particle -> {
                particle.setParticleSpeed(this.repulsionFeild(particle));
                System.out.println("trail fbe: " + this.trail);
                if (this.usetrail && this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()) {
                    this.trail.pos = particle.getParticlePosition();
                    this.trail.spawnTrail();
                }
            };
            Color startingColor = col1;
            Color endingColor = col2;
            if (rancolinrange) {  // ill leave this for later
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
                    .repeat(this.level, this.pos, 1);
        }
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        System.out.println("saved fball");
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("usetrail", usetrail);
        tag.putBoolean("rancolinrange", rancolinrange);
        tag.putInt("count", count);
        tag.putString("col1", col1.toString());
        tag.putString("col2", col2.toString());
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
        this.usetrail = tag.getBoolean("usetrail");
        this.rancolinrange = tag.getBoolean("rancolinrange");
        this.count = tag.getInt("count");
        this.transparancy1 = tag.getFloat("transparancy1");
        this.transparancy2 = tag.getFloat("transparancy2");
        this.scale1 = tag.getFloat("scale1");
        this.scale2 = tag.getFloat("scale2");
        this.lifetime = tag.getInt("lifetime");
        this.gravity = tag.getFloat("gravity");
        this.ranoffset = tag.getFloat("ranoffset");
        this.range = tag.getFloat("range");
        this.repulsionlength = tag.getInt("repulsionlength");
        if(tag.getString("col1") != "") {
            String col1str = tag.getString("col1");
            col1str = col1str.replace("java.awt.Color[r=", "")
                    .replace("g=", "")
                    .replace("b=", "")
                    .replace("]", "");
            String[] col1strarray = col1str.split(",");
            System.out.println("num1 " + Integer.valueOf(col1strarray[0]) + col1strarray[1] + col1strarray[2]);
            this.col1 = new Color(Integer.parseInt(col1strarray[0].trim()), Integer.parseInt(col1strarray[1].trim()), Integer.parseInt(col1strarray[2].trim()));
        }if(tag.getString("col2") != "") {
            String col2str = tag.getString("col2");
            col2str = col2str.replace("java.awt.Color[r=", "")
                    .replace("g=", "")
                    .replace("b=", "")
                    .replace("]", "");
            String[] col2strarray = col2str.split(",");
            System.out.println("num2 " + col2strarray[0] + col2strarray[1] + col2strarray[2]);
            this.col2 = new Color(Integer.parseInt(col2strarray[0].trim()), Integer.parseInt(col2strarray[1].trim()), Integer.parseInt(col2strarray[2].trim()));
        }
    }


}
