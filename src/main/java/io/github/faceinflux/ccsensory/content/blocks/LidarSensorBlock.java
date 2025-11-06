package io.github.faceinflux.ccsensory.content.blocks;

import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LidarSensorBlock extends Block implements EntityBlock {
    public LidarSensorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LidarSensorBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType == ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get()
                ? (Level lev, BlockPos pos, BlockState state, T e) ->
                    LidarSensorBlockEntity.tick(lev, pos, state, (LidarSensorBlockEntity) e)
                : null;
    }
}
