//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.SimpleRegistryEntry;
import io.github.faceinflux.ccsensory.util.blockentities.BlockEntityTypeRegistryEntry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;

public class BlockEntityRegistryHandler {
    public static void registerBlockEntities() {
        for (RegistryEntry<BlockEntityType<?>, ?> entry : ModBlockEntityTypes.register.values()) {
            BlockEntityTypeRegistryEntry castEntry = (BlockEntityTypeRegistryEntry) entry;
            registerBlockEntity(castEntry);
        }
    }

    private static <T extends BlockEntity> void registerBlockEntity(BlockEntityTypeRegistryEntry entry) {
        CCSensory.LOGGER.info("Registering BlockEntity {}", entry.id);

        CCSensory.LOGGER.info(entry.factory.getClass().getName());

        BlockEntityType<T> registeredBlockEntity = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ModBlockEntityTypes.blockEntityKey(entry.id), FabricBlockEntityTypeBuilder.<T>create(
                        (pos, state) -> (T) entry.factory.create(pos, state),
                        Arrays.stream(entry.blocks).map(RegistryEntry::get).toArray(Block[]::new)
                ).build());


        entry.returnSupplier = () -> registeredBlockEntity;
    }
}
//?}