package com.rnr.rnrjointmod.block.Custom;

import com.mojang.serialization.MapCodec;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.block.entity.ModBlockEntities;
import com.rnr.rnrjointmod.particals.ExampleParticleEffect;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

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
        shootFirework(level, pos);
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public static void shootFirework(Level level, BlockPos pos){
        ExampleParticleEffect.generateShpere(level, 50, pos.getBottomCenter().add(0, 100, 0));
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
