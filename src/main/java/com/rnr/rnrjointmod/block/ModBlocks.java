package com.rnr.rnrjointmod.block;



import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.Custom.FireworkCake;
import com.rnr.rnrjointmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(RnRJointMod.MOD_ID);


    public static final  DeferredBlock<Block> BLOCK = registerBlock("block",
            () -> new Block(BlockBehaviour.Properties.of().noLootTable()));

    public static final  DeferredBlock<Block> FIREWORK_CAKE = registerBlock("firework_cake",
            () -> new FireworkCake(BlockBehaviour.Properties.of().noOcclusion()));



    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static <T extends  Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    private static <T extends Block> void  registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
