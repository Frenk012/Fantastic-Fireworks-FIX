package com.rnr.rnrjointmod.item.custom;

import com.rnr.rnrjointmod.particals.Trail;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static com.rnr.rnrjointmod.particals.ExampleParticleEffect.generateShpere;

public class ParticleTester extends Item {

    public ParticleTester(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        HitResult hitResult = player.pick(100, 0.0f, false);
        Vec3 pos = hitResult.getLocation();
        generateShpere(level, 3, pos, new Trail(level, pos));
        return super.use(level, player, usedHand);
    }
}
