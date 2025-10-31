package io.github.faceinflux.ccsensory.util.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Interface for creation of a BlockEntity. Basically just Fabric's FabricBlockEntityTypeBuilder.Factory but
 * loader agnostic*/
@FunctionalInterface
public interface BlockEntityFactory<T extends BlockEntity> {
    T create(BlockPos blockPos, BlockState blockState);
}
