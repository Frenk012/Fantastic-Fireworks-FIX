package com.rnr.rnrjointmod.dataattatchments;

import com.mojang.serialization.Codec;
import com.rnr.rnrjointmod.RnRJointMod;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModDataAttatchments {
    // Create the DeferredRegister for attachment types
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RnRJointMod.MOD_ID);


    // Serialization via codec
    public static final Supplier<AttachmentType<BlockPos>> FBLOCK = ATTACHMENT_TYPES.register(
            "fblock", () -> AttachmentType.builder(() -> new BlockPos(0,0,0)).serialize(BlockPos.CODEC).build()
    );


    public static void register(IEventBus eventBus){ATTACHMENT_TYPES.register(eventBus);}
}


