package com.rnr.rnrjointmod.item;


import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RnRJointMod.MOD_ID);

    public static final Supplier<CreativeModeTab> RNR_TAB = CREATIVE_MODE_TAB.register("rnr_tab",
            ()-> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(Blocks.BLACK_CONCRETE))
                    .title(Component.translatable("creativetab.examplemod.rnr_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.BLOCK);
                        output.accept(ModItems.ITEM);

                    })

                    .build());

    public static void register(IEventBus iEventBus){
        CREATIVE_MODE_TAB.register(iEventBus);
    }
}
