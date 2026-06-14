package com.rnr.rnrjointmod.item.custom;

import com.rnr.rnrjointmod.particals.Trail;
import io.netty.util.AttributeMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.jar.Attributes;

import static com.rnr.rnrjointmod.particals.ExampleParticleEffect.generateShpere;

public class ParticleTester extends Item {

    public ParticleTester(Properties properties) {
        super(properties);
    }

    //    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        Level level = context.getLevel();
//        Vec3 pos = context.getClickLocation();
//        generateShpere(level, 700, pos);
//        System.out.println("used");
//        return super.useOn(context);
//    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        HitResult hitResult = player.pick(100, 0.0f, false);
        Vec3 pos = hitResult.getLocation();
        generateShpere(player.level(), 150, pos, new Trail(level, pos));

        System.out.println("used" + pos);
        return super.use(level, player, usedHand);
    }


}
