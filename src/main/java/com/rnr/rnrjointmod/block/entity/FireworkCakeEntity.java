
package com.rnr.rnrjointmod.block.entity;

import com.rnr.rnrjointmod.Payloads.FireworkBeginingPayload;
import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class FireworkCakeEntity extends BlockEntity implements MenuProvider {
    public Trail trail;
    public FireworkBeginning fireworkBeginning;
    public FireworkBall fireworkBall;
    public Trail trail1;
    public FireworkBeginning fireworkBeginning1;
    public FireworkBall fireworkBall1;

    public FireworkCakeEntity( BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FIREWORK_CAKE_BE.get(), pos, blockState);
    }

    public FireworkBeginning getFBegining(){
        //if(!this.level.isClientSide) {
        //    PacketDistributor.sendToAllPlayers(new FireworkBeginingPayload.FireworkBeginingPayloadRec(level.getDescriptionKey(), this.getBlockPos().toShortString(), fireworkBeginning.rancolinrange, fireworkBeginning.count, fireworkBeginning.transparancy1, fireworkBeginning.transparancy2));
        //}
        return this.fireworkBeginning;
    }

    public FireworkBall getFBall(){
        return this.fireworkBall;
    }

    public Trail getFTrail(){
        return this.trail;
    }
    public void setFBegining(FireworkBeginning fireworkBeginning){
        this.fireworkBeginning1 = fireworkBeginning;
    }
    public void setFBall(FireworkBall fireworkBall){
        this.fireworkBall1 = fireworkBall;
    }
    public void setFTrail(Trail trail){
        this.trail1 = trail;

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Firework Cake");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    public void setup(Level level, BlockPos pos){
        Vec3 fpos = pos.getCenter();
        if(fireworkBeginning == null || fireworkBall == null || trail == null) {
            this.fireworkBeginning = new FireworkBeginning(level, fpos);
        }
        if(fireworkBall == null) {
            this.fireworkBall = new FireworkBall(level, fpos);
            this.fireworkBeginning.fireworkBall = fireworkBall;
        }
            if(trail == null) {
            this.trail = new Trail(level, fpos);
            this.fireworkBeginning.trail = trail;
            this.fireworkBall.trail = trail;
            }
    }

    public void shootFirework(Level level, BlockPos pos){
        //System.out.println("Level: " + this.level.getDescriptionKey());
        //System.out.println("pos: " + this.getBlockPos().toShortString());
        if(fireworkBeginning == null || fireworkBall == null || trail == null) {
            setup(level, pos);
        }
        if (this.fireworkBall1 !=null && this.fireworkBeginning1 !=null && this.trail1 !=null ){
            System.out.println("121212");
            this.fireworkBeginning = fireworkBeginning1;
            this.fireworkBall = fireworkBall1;
            System.out.println(this.fireworkBall.scale1);
            this.trail = trail1;
            this.fireworkBeginning.trail = trail;
            this.fireworkBeginning.fireworkBall = fireworkBall;
        }
//        this.setChanged();
        //if(ti)
        if(this.level.isClientSide){
            System.out.println("clientside "  + this.fireworkBeginning.transparancy1);
        }else {
            System.out.println("serverside " + this.fireworkBeginning.transparancy1);
            PacketDistributor.sendToAllPlayers(new FireworkBeginingPayload.FireworkBeginingPayloadRec(level.getDescriptionKey(), pos.toShortString(), fireworkBeginning.serializeNBT(level.registryAccess()), fireworkBall.serializeNBT(level.registryAccess()), trail.serializeNBT(level.registryAccess())));
        }      //fireworkBeginning.level = level;
        this.fireworkBeginning.BodyParticles();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (fireworkBeginning != null) {
            if(!this.level.isClientSide){
                System.out.println("serverside " + this.fireworkBeginning.transparancy1);
            }
            tag.put("fireworkbeginning", this.fireworkBeginning.serializeNBT(registries));
            tag.put("fireworkball", this.fireworkBall.serializeNBT(registries));
            tag.put("trail", this.trail.serializeNBT(registries));
            System.out.println("saved: "+ this + "  : " + this.fireworkBeginning.transparancy1 );
        }else{
            System.out.println("null: "+ this);
        }
        //tag.put("FireworkBall", (Tag) this.fireworkBall);
        //tag.put("FireworkTrail", (Tag) this.trail);
        //tag.put("settings", settings.serializeNBT(registries));  // just temporary comments for saveing and loading untill i know the structure ill use for saveing and loading
    }
//nullpointer exeptions when fireworks are launched prob from my changes to the f begining class
    //loading just loads defaults prob cause i make a new object each time
    //saveing used to work when i wanted it to now its not idk why


//    @Override
//    public void onLoad() {
//        super.onLoad();
//        if(!this.level.isClientSide) {
//            System.out.println("functions");
//
//        }
//    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag holdertag = new CompoundTag();
        holdertag.put("fireworkbeginning", this.fireworkBeginning.serializeNBT(registries));
        holdertag.put("fireworkball", this.fireworkBall.serializeNBT(registries));
        holdertag.put("trail", this.trail.serializeNBT(registries));
        return holdertag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        System.out.println("packagesent");
        ClientboundBlockEntityDataPacket.create(this);
//        PacketDistributor.sendToAllPlayers(new FireworkBeginingPayload.FireworkBeginingPayloadRec(level.getDescriptionKey(), this.getBlockPos().toShortString(), fireworkBeginning.rancolinrange, fireworkBeginning.count, fireworkBeginning.transparancy1, fireworkBeginning.transparancy2));
        return super.getUpdatePacket();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        System.out.println("updatedclientBE");
        this.fireworkBeginning.deserializeNBT(lookupProvider, tag.getCompound("fireworkbeginning"));
        this.fireworkBall.deserializeNBT(lookupProvider, tag.getCompound("fireworkball"));
        this.trail.deserializeNBT(lookupProvider, tag.getCompound("trail"));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (fireworkBeginning == null || fireworkBall == null || trail == null) {
            this.setup(this.getLevel(), this.getBlockPos());
        }
        if (fireworkBeginning != null) {
            this.fireworkBeginning.deserializeNBT(registries, tag.getCompound("fireworkbeginning"));
            this.fireworkBall.deserializeNBT(registries, tag.getCompound("fireworkball"));
            this.trail.deserializeNBT(registries, tag.getCompound("trail"));
            System.out.println("loaded: "+ this + "  : " + this.fireworkBeginning.transparancy1 );
        }else{
            System.out.println("null Load: "+ this);
        }

        //this.fireworkBeginning = (FireworkBeginning) tag.get("FireworkBall");
        //this.fireworkBeginning = (FireworkBeginning) tag.get("FireworkTrail");

        //settings.deserializeNBT(registries, tag.getCompound(settings))
        //if(level != null){
        //}
    }


}
