//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.items.ModBlocks;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockRegistryHandler {
    public static void registerBlocks() {
        for (RegistryEntry<Block, ?> entry : ModBlocks.register.values()) {
            registerBlock(entry);
        }
    }

    private static <T extends Block> void registerBlock(RegistryEntry<Block, T> entry) {
        CCSensory.LOGGER.info("Registering block {}", entry.id);
        T registeredBlock = Registry.register(
                BuiltInRegistries.BLOCK,
                ModBlocks.blockKey(entry.id),
                (T) entry.creationSupplier.get());

        entry.returnSupplier = () -> registeredBlock;
    }
}
//?}