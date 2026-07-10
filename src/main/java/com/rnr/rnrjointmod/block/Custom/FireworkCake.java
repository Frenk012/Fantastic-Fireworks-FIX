package com.rnr.rnrjointmod.block.Custom;

import com.mojang.serialization.MapCodec;
import com.rnr.rnrjointmod.block.entity.FireworkCakeEntity;
import com.rnr.rnrjointmod.block.entity.ModBlockEntities;
import com.rnr.rnrjointmod.screen.custom.ClientScreenOpener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FireworkCake extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 9, 12);
    public static final MapCodec<FireworkCake> CODEC = simpleCodec(FireworkCake::new);

    public FireworkCake(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            if (level.isClientSide) {
                ClientScreenOpener.openFireworkEditor(pos);
            }
        } else if (!level.isClientSide && level.getBlockEntity(pos) instanceof FireworkCakeEntity cake) {
            cake.triggerLaunch(level);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, ModBlockEntities.FIREWORK_CAKE_BE.get(), FireworkCakeEntity::serverTick);
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
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FireworkCakeEntity(blockPos, blockState);
    }
}
