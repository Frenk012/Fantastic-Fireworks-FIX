package com.rnr.rnrjointmod.particals;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.function.Consumer;

/** Standalone test effect used by the Particle Tester item. */
public class ExampleParticleEffect {

    public static void generateShpere(Level level, int n, Vec3 middlePos, Trail trail) {
        if (!level.isClientSide) {
            return;
        }
        double goldenRatio = 1 + Math.sqrt(5) / 4;
        double angleIncrement = Math.PI * 2 * goldenRatio;
        double mult = 3;
        for (int i = 0; i < n; i++) {
            float dist = (float) i / n;
            double incline = Math.acos(1 - 2 * dist);
            double azimuth = angleIncrement * i;

            double x = Math.sin(incline) * Math.cos(azimuth) * mult;
            double y = Math.sin(incline) * Math.sin(azimuth) * mult;
            double z = Math.cos(incline) * mult;
            Vec3 pos = new Vec3(x, y, z).add(middlePos);
            spawnMainParticles(level, pos, middlePos, trail);
        }
    }

    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    private static Vec3 repulsionField(LodestoneWorldParticle particle, Vec3 middlePos, double range, Trail trail) {
        Vec3 pos = particle.getParticlePosition();
        Vec3 velo = particle.getParticleSpeed();
        double k = 1.0;

        Vec3 dist = middlePos.subtract(pos);
        double mag = dist.length();

        if (mag <= range && particle.getAge() <= 20) {
            dist = dist.normalize().scale(k * map(mag, 0, range, -1, 0));
            velo = velo.add(dist).add(0, 0.2, 0);
        } else {
            velo = velo.scale(0.95);
        }
        if (trail != null && pos != particle.getOldParticlePosition()) {
            trail.col1 = Color.cyan;
            trail.pos = pos;
            trail.spawnTrail();
        }
        return velo;
    }

    public static void spawnMainParticles(Level level, Vec3 pos, Vec3 middlePos, Trail trail) {
        float range = 10;
        Consumer<LodestoneWorldParticle> tickActor = particle ->
                particle.setParticleSpeed(repulsionField(particle, middlePos, range, trail));

        Color startingColor = new Color(112, 251, 5);
        Color endingColor = new Color(31, 57, 10);
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(50)
                .setScaleData(GenericParticleData.create(0.5f, 0.2f).build())
                .setRandomOffset(0.9)
                .enableForcedSpawn()
                .addTickActor(tickActor)
                .setGravity(3.5f)
                .setFullBrightLighting()
                .spawn(level, pos);
    }
}
