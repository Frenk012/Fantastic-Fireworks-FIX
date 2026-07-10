package com.rnr.rnrjointmod;

import com.mojang.logging.LogUtils;
import com.rnr.rnrjointmod.block.ModBlocks;
import com.rnr.rnrjointmod.block.entity.ModBlockEntities;
import com.rnr.rnrjointmod.item.ModCreativeModeTab;
import com.rnr.rnrjointmod.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(RnRJointMod.MOD_ID)
public class RnRJointMod {
    public static final String MOD_ID = "rnrjointmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RnRJointMod(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
    }
}
