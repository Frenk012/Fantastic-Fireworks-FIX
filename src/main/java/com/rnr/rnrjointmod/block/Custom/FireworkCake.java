package com.rnr.rnrjointmod.block.Custom;

import com.mojang.serialization.MapCodec;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.block.entity.ModBlockEntities;
import com.rnr.rnrjointmod.particals.*;
import com.rnr.rnrjointmod.screen.custom.FireworkCakeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class FireworkCake extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 9, 12);
    public static final MapCodec<FireworkCake> CODEC = simpleCodec(FireworkCake::new);
    //public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public FireworkCake(Properties properties) {
        super(properties);
//        this.registerDefaultState(
//                this.stateDefinition.any().setValue(LIT, false)
//        );
    }

//    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
//        if (!level.isClientSide) {
//            boolean flag = (Boolean) state.getValue(LIT);
//            if (flag != level.hasNeighborSignal(pos)) {
//                if (flag) {
//                    level.scheduleTick(pos, this, 4);
//                } else {
//                    shootFirework(level, pos, block);
//                }
//            }
//        }
//    }
//    @javax.annotation.Nullable
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return (BlockState)this.defaultBlockState().setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
//    }
//
//    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        if ((Boolean)state.getValue(LIT) && !level.hasNeighborSignal(pos)) {
//            level.setBlock(pos, (BlockState)state.cycle(LIT), 2);
//        }
//
//    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching() && level.isClientSide) {
            //add to their imgui window
        } else{
            shootFirework(level, pos);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public static boolean defaultTrail = false;
    public static Color defaultCol1 = new Color(137, 7, 181);
    public static Color defaultCol2 = new Color(0, 0, 0);
    public static boolean defaultRancolinrange = false;
    public static int defaultCount = 1;
    public static float defaultTransparancy1 = 0.75f;
    public static float defaultTransparancy2 = 0.15f;
    public static float defaultScale1 = 1.0f;
    public static float defaultScale2 = 0.0f;
    public static int defaultLifetime = 50;
    public static float defaultGravity = 3.5f;
    public static float defaultRanoffset = 0.9f;


    public static void shootFirework(Level level, BlockPos pos){
        Vec3 fpos = pos.getCenter();
        Trail trail = new Trail(level, fpos);
        FireworkBeginning fireworkBeginning = new FireworkBeginning(level, fpos);
        FireworkBall fireworkBall = new FireworkBall(level, fpos);
        fireworkBeginning.trail = trail;
        fireworkBeginning.fireworkBall = fireworkBall;
        trail.col1 = Color.cyan;
        trail.col2 = Color.green;
        fireworkBeginning.lifetime = 20;
        fireworkBeginning.BodyParticles();
    }


        @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FireworkCakeEntity(blockPos, blockState);
    }


}
