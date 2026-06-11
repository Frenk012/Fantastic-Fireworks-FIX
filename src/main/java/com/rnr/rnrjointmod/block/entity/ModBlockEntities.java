package com.rnr.rnrjointmod.block.entity;

import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RnRJointMod.MOD_ID);

    public static final Supplier<BlockEntityType<FireworkCakeEntity>> FIREWORK_CAKE_BE =
            BLOCK_ENTITIES.register("firework_cake_be", () -> BlockEntityType.Builder.of(
                    FireworkCakeEntity::new, ModBlocks.FIREWORK_CAKE.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
