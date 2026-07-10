package com.rnr.rnrjointmod.block.entity;

import com.rnr.rnrjointmod.Payloads.FireworkSettingsPayload;
import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class FireworkCakeEntity extends BlockEntity {
    public FireworkBeginning fireworkBeginning;
    public FireworkBall fireworkBall;
    public Trail trail;

    public FireworkCakeEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIREWORK_CAKE_BE.get(), pos, blockState);
    }

    /** Lazily creates the particle setting objects and wires them together. */
    public void setup(Level level, BlockPos pos) {
        Vec3 fpos = pos.getCenter();
        if (fireworkBeginning == null) {
            fireworkBeginning = new FireworkBeginning(level, fpos);
        }
        if (fireworkBall == null) {
            fireworkBall = new FireworkBall(level, fpos);
        }
        if (trail == null) {
            trail = new Trail(level, fpos);
        }
        fireworkBeginning.fireworkBall = fireworkBall;
        fireworkBeginning.trail = trail;
        fireworkBall.trail = trail;
    }

    public void shootFirework(Level level, BlockPos pos) {
        setup(level, pos);
        // settings may have been created during world load when the level was still null
        fireworkBeginning.level = level;
        fireworkBall.level = level;
        trail.level = level;
        if (!level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new FireworkSettingsPayload(
                    level.dimension().location().toString(),
                    getBlockPos(),
                    fireworkBeginning.serializeNBT(level.registryAccess()),
                    fireworkBall.serializeNBT(level.registryAccess()),
                    trail.serializeNBT(level.registryAccess())));
        }
        fireworkBeginning.pos = pos.getCenter();
        fireworkBeginning.BodyParticles();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (fireworkBeginning != null && fireworkBall != null && trail != null) {
            tag.put("fireworkbeginning", fireworkBeginning.serializeNBT(registries));
            tag.put("fireworkball", fireworkBall.serializeNBT(registries));
            tag.put("trail", trail.serializeNBT(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        setup(getLevel(), getBlockPos());
        if (tag.contains("fireworkbeginning")) {
            fireworkBeginning.deserializeNBT(registries, tag.getCompound("fireworkbeginning"));
        }
        if (tag.contains("fireworkball")) {
            fireworkBall.deserializeNBT(registries, tag.getCompound("fireworkball"));
        }
        if (tag.contains("trail")) {
            trail.deserializeNBT(registries, tag.getCompound("trail"));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        loadAdditional(tag, lookupProvider);
    }
}
