package com.rnr.rnrjointmod.block.entity;

import com.rnr.rnrjointmod.Payloads.FireworkSettingsPayload;
import com.rnr.rnrjointmod.firework.ExplosionSettings;
import com.rnr.rnrjointmod.firework.FireworkCakeRegistry;
import com.rnr.rnrjointmod.firework.RocketSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class FireworkCakeEntity extends BlockEntity {
    public final RocketSettings rocket = new RocketSettings();
    public final ExplosionSettings explosion = new ExplosionSettings();

    /** Human-readable id shown in the GUI and usable with the /firework command. */
    public String cakeId = "";
    public boolean autoLaunch = false;
    /** Ticks between automatic launches (20 ticks = 1 second). */
    public int intervalTicks = 100;
    private int timer = 0;

    public FireworkCakeEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIREWORK_CAKE_BE.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FireworkCakeEntity cake) {
        if (!cake.autoLaunch) {
            cake.timer = 0;
            return;
        }
        if (++cake.timer >= Math.max(cake.intervalTicks, 5)) {
            cake.timer = 0;
            cake.triggerLaunch(level);
        }
    }

    /** Server side: plays the firework - Cascade streams it to every nearby client. */
    public void triggerLaunch(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            rocket.build(explosion.build())
                    .play(serverLevel, getBlockPos().above().getCenter());
        }
    }

    /** Full state as one tag - used for NBT persistence and network sync. */
    public CompoundTag buildSyncTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("rocket", rocket.save());
        tag.put("explosion", explosion.save());
        tag.putString("cakeid", cakeId);
        tag.putBoolean("autolaunch", autoLaunch);
        tag.putInt("intervalticks", intervalTicks);
        return tag;
    }

    public void applySyncTag(HolderLookup.Provider registries, CompoundTag tag) {
        rocket.load(tag.getCompound("rocket"));
        explosion.load(tag.getCompound("explosion"));
        if (tag.contains("autolaunch")) {
            autoLaunch = tag.getBoolean("autolaunch");
        }
        if (tag.contains("intervalticks")) {
            intervalTicks = Math.max(tag.getInt("intervalticks"), 5);
        }
        if (tag.contains("cakeid")) {
            setCakeId(tag.getString("cakeid"));
        }
    }

    public void setCakeId(String newId) {
        newId = newId.trim();
        if (newId.equals(cakeId)) {
            return;
        }
        if (getLevel() instanceof ServerLevel serverLevel) {
            FireworkCakeRegistry.unregister(cakeId, globalPos(serverLevel));
            cakeId = newId;
            FireworkCakeRegistry.register(cakeId, globalPos(serverLevel));
        } else {
            cakeId = newId;
        }
    }

    private GlobalPos globalPos(Level level) {
        return GlobalPos.of(level.dimension(), getBlockPos());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            if (cakeId.isEmpty()) {
                cakeId = "cake_" + getBlockPos().getX() + "_" + getBlockPos().getY() + "_" + getBlockPos().getZ();
            }
            FireworkCakeRegistry.register(cakeId, globalPos(serverLevel));
        }
    }

    @Override
    public void setRemoved() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            FireworkCakeRegistry.unregister(cakeId, globalPos(serverLevel));
        }
        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            FireworkCakeRegistry.unregister(cakeId, globalPos(serverLevel));
        }
        super.onChunkUnloaded();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("fireworkcake", buildSyncTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("fireworkcake")) {
            applySyncTag(registries, tag.getCompound("fireworkcake"));
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
