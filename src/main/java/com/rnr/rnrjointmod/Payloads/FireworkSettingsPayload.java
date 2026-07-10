package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.RnRJointMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Syncs the full state of one firework cake block.
 * {@code dimension} is the dimension id (e.g. "minecraft:overworld").
 * {@code data} is the sync tag built by {@code FireworkCakeEntity#buildSyncTag}.
 * When {@code launch} is true the receiving side also fires the rocket.
 */
public record FireworkSettingsPayload(String dimension, BlockPos pos, CompoundTag data,
                                      boolean launch) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<FireworkSettingsPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RnRJointMod.MOD_ID, "firework_settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FireworkSettingsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, FireworkSettingsPayload::dimension,
            BlockPos.STREAM_CODEC, FireworkSettingsPayload::pos,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG, FireworkSettingsPayload::data,
            ByteBufCodecs.BOOL, FireworkSettingsPayload::launch,
            FireworkSettingsPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
