package io.github.faceinflux.ccsensory.util.blockentities;

import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.SimpleRegistryEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/** RegistryEntry for BlockEntityTypes, containing attributes required for BlockEntityType registration.*/
public class BlockEntityTypeRegistryEntry extends RegistryEntry<BlockEntityType<?>, BlockEntityType<?>> {
    public final RegistryEntry<Block, ? extends Block>[] blocks;
    public final BlockEntityFactory<?> factory;

    public BlockEntityTypeRegistryEntry(String id, BlockEntityFactory<?> factory, RegistryEntry<Block, ? extends Block>... blocks) {
        super(id);
        this.blocks = blocks;
        this.factory = factory;
    }
}
