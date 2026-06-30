package com.rnr.rnrjointmod.block.entity;

import com.rnr.rnrjointmod.particals.FireworkBall;
import com.rnr.rnrjointmod.particals.FireworkBeginning;
import com.rnr.rnrjointmod.particals.Trail;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
        this.trail = new Trail(level, fpos);
        this.trail.col1 = Color.cyan;
        this.trail.col2 = Color.green;

        this.fireworkBall = new FireworkBall(level, fpos);
        this.fireworkBall.count = 50;
        this.fireworkBall.trail = trail;
        this.fireworkBall.lifetime = 20;

        this.fireworkBeginning = new FireworkBeginning(level, fpos);
        this.fireworkBeginning.trail = trail;
        this.fireworkBeginning.fireworkBall = fireworkBall;
        this.fireworkBeginning.lifetime = 25;
    }

    public void shootFirework(Level level, BlockPos pos){
        if(fireworkBeginning == null) {
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
        this.fireworkBeginning.BodyParticles();
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        //tag.put("settings", settings.serializeNBT(registries));  // just temporary comments for saveing and loading untill i know the structure ill use for saveing and loading
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        //settings.deserializeNBT(registries, tag.getCompound(settings))
    }


}
