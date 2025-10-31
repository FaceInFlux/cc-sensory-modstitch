//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.SimpleRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockRegistryHandler {
    public static void registerBlocks() {
        for (RegistryEntry<Block, ?> entry : ModBlocks.register.values()) {
            SimpleRegistryEntry<Block, ?> castEntry = (SimpleRegistryEntry<Block, ?>) entry;
            registerBlock(castEntry);
        }
    }

    private static <T extends Block> void registerBlock(SimpleRegistryEntry<Block, T> entry) {
        CCSensory.LOGGER.info("Registering block {}", entry.id);
        T registeredBlock = Registry.register(
                BuiltInRegistries.BLOCK,
                ModBlocks.blockKey(entry.id),
                entry.creationSupplier.get());

        entry.returnSupplier = () -> registeredBlock;
    }
}
//?}