package com.rnr.rnrjointmod.Payloads;

import com.rnr.rnrjointmod.RnRJointMod;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class FireworkBeginingPayload {

    public record FireworkBeginingPayloadRec(String level, String blockpos, CompoundTag fireworkbeginning, CompoundTag fireworkball, CompoundTag trail) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<FireworkBeginingPayloadRec> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RnRJointMod.MOD_ID, "fireworkbeginingpayload"));

        // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
        // 'name' will be encoded and decoded as a string
        // 'age' will be encoded and decoded as an integer
        // The final parameter takes in the previous parameters in the order they are provided to construct the payload object

        public static final StreamCodec<ByteBuf, FireworkBeginingPayloadRec> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                FireworkBeginingPayloadRec::level,
                ByteBufCodecs.STRING_UTF8,
                FireworkBeginingPayloadRec::blockpos,
                ByteBufCodecs.TRUSTED_COMPOUND_TAG,
                FireworkBeginingPayloadRec::fireworkbeginning,
                ByteBufCodecs.TRUSTED_COMPOUND_TAG,
                FireworkBeginingPayloadRec::fireworkball,
                ByteBufCodecs.TRUSTED_COMPOUND_TAG,
                FireworkBeginingPayloadRec::trail,
                FireworkBeginingPayloadRec::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }}
//ByteBufCodecs.BOOL,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::rancolinrange,
//ByteBufCodecs.VAR_INT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::count,
//ByteBufCodecs.STRING_UTF8,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::col1,
//ByteBufCodecs.STRING_UTF8,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::col2,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::transparancy1,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::transparancy2,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::scale1,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::scale2,
//ByteBufCodecs.VAR_INT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::lifetime,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::gravity,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::rancolinrange,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::initalupvelo,
//ByteBufCodecs.FLOAT,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::upwardsacceleration,
//ByteBufCodecs.BOOL,
//FireworkBeginingPayload.FireworkBeginingPayloadRec::trailoff,