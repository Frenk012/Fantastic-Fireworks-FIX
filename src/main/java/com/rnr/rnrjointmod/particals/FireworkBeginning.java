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

public class FireworkBeginning implements INBTSerializable<CompoundTag> {
    public static Consumer<LodestoneWorldParticle> consumer = null;

    public Level level;
    public Vec3 pos;
    public FireworkBall fireworkBall = null;
    public boolean usetrail = true;
    public Trail trail = null;                             // In Gui
    public Color col1 = new Color(137, 7, 181);   // In Gui
    public Color col2 = new Color(0, 0, 0);       // In Gui
    public boolean rancolinrange = false;                  // In Gui
    public int count = 1;                                  // No Need X
    public float transparancy1 = 0.75f;                    // In Gui
    public float transparancy2 = 0.15f;                    // In Gui
    public float scale1 = 1.0f;                            // In Gui
    public float scale2 = 0.0f;                            // In Gui
    public int lifetime = 50;                              // In Gui
    public float gravity = 3.5f;                           // In Gui
    public float ranoffset = 0.9f;                         // In Gui
    public float initalUpVelo = 3.0f;                      // In Gui
    public float upwardsAcceleration = 0.2f;               // In Gui


    public FireworkBeginning(Level level, Vec3 pos){
        this.pos = pos;
        this.level = level;
    }

    public void BodyParticles() {
        if (this.level.isClientSide) {
            float range = 10;

            consumer = particle -> {
                particle.setParticleSpeed(particle.getParticleSpeed().add(0, this.upwardsAcceleration, 0));
                //System.out.println("trail fbe: " + this.trail);
                if (this.usetrail && this.trail != null && particle.getParticlePosition() != particle.getOldParticlePosition()) {
                    this.trail.pos = particle.getParticlePosition();
                    this.trail.spawnTrail();
                }
                //System.out.println(particle.getLifetime() - particle.getAge());
                if (particle.getAge() == particle.getLifetime() && this.fireworkBall != null) {
                    this.fireworkBall.middlePos = particle.getParticlePosition();
                    this.fireworkBall.explodeFirework();
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
                    .addTickActor(consumer)
                    .setGravity(this.gravity)
                    .setFullBrightLighting()
                    .setMotion(0, this.initalUpVelo, 0)
                    .repeat(this.level, this.pos, this.count);
        }
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        System.out.println("saved");
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
        tag.putFloat("initalupvelo", initalUpVelo);
        tag.putFloat("upwardsacceleration", upwardsAcceleration);
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
        this.initalUpVelo = tag.getFloat("initalupvelo");
        this.upwardsAcceleration = tag.getFloat("upwardsacceleration");
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
//public float scale1 = 1.0f;                            // In Gui
//public float scale2 = 0.0f;                            // In Gui
//public int lifetime = 50;                              // In Gui
//public float gravity = 3.5f;                           // In Gui
//public float ranoffset = 0.9f;                         // In Gui
//public float initalUpVelo = 3.0f;                      // In Gui
//public float upwardsAcceleration = 0.2f;