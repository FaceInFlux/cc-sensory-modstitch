package io.github.faceinflux.ccsensory.util.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.faceinflux.ccsensory.util.blockentities.MultiVersionBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PeripheralHoldingBlockEntity<P extends IPeripheral> extends MultiVersionBlockEntity {
    protected PeripheralHoldingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract P getPeripheral();
}
