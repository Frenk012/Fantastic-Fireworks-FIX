package com.rnr.rnrjointmod.particals;

import com.rnr.rnrjointmod.RnRJointMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.function.Consumer;

@EventBusSubscriber(value = Dist.CLIENT, modid = RnRJointMod.MOD_ID)
public class ExampleParticleEffect {

    public static Consumer<LodestoneWorldParticle> consumer = null;
    public static int tick = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            tick += 1;
            if(tick % 60 == 0) {
                //generateShpere(player.level(), 700, player.position());
                //spawnMainParticles(player.level(), player.position());
                //System.out.println("shpere");
            }
            //System.out.println("called");
        }

    }
    public static void generateShpere(Level level, int n, Vec3 middlePos, Trail trailobj){
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
            spawnMainParticles(level, pos, middlePos, true,  trailobj);
        }
    }
    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }


    public static Vec3 repulsionFeild (Level level, LodestoneWorldParticle particle, Vec3 middlePos, double range, boolean trail, Trail trailobj){
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
        if (trail && pos != particle.getOldParticlePosition()) {
            trailobj.col1 = Color.cyan;
            trailobj.pos = pos;
            trailobj.spawnTrail();
        }
        return velo;
    }


    public static void spawnMainParticles(Level level, Vec3 pos, Vec3 middlePos, boolean trail, Trail trailobj) {
        float range = 10;

        consumer = particle ->  particle.setParticleSpeed(repulsionFeild(level, particle, middlePos, range, trail, trailobj) ); //Math.abs(finalPos.distanceTo(middlePos))

        //System.out.println("particle made");
        //pos = pos.add(0, -1,0);
       // if(level.getBlockState(BlockPos.containing(pos)) != Blocks.AIR.defaultBlockState()) {
            Color startingColor = new Color(112, 251, 5);
            Color endingColor = new Color(31, 57, 10);
            WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setLifetime(50)
                    .setScaleData(GenericParticleData.create(0.5f, 0.2f).build())
                    //.setRandomMotion(0.001)
                    //.enableNoClip()
                    //.createBlockOutline(level, BlockPos.containing(pos), level.getBlockState(BlockPos.containing(pos)))
                    .setRandomOffset(0.9)
                    .enableForcedSpawn()
                    .addTickActor(consumer)
                    .setGravity(3.5f)
                    .setFullBrightLighting()
                    //.setFriction(0)

                    .spawn(level, pos);

        //}
    }

    public static void spawnTrail(Level level, Vec3 pos, Vec3 middlePos, boolean trail) {
        Vec3 finalPos = pos;
        Color startingColor = new Color(73, 69, 69);
        Color endingColor = new Color(149, 149, 143);
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(0.50f, 0.10f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(10)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                //.enableNoClip()
                .setRandomOffset(0.1)
                .setGravity(0.1f)
                .setFullBrightLighting()
                .enableForcedSpawn()
                .spawn(level, pos);

        //}
    }
}
