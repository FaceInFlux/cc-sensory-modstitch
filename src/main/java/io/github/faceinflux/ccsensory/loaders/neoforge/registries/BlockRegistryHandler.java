//? if neoforge {
/*package io.github.faceinflux.ccsensory.loaders.neoforge.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistryHandler {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CCSensory.ID);

    @SuppressWarnings("unchecked")
    public static <T extends Block> void registerBlocks(IEventBus eventBus) {
        CCSensory.LOGGER.info("Registering blocks");
        BLOCKS.register(eventBus);
        for (RegistryEntry<Block, ?> entry : ModBlocks.register.values()) {
            CCSensory.LOGGER.info("Registering block {}", entry.id);
            // This is really cursed but I'm struggling with generics ;-;
            RegistryEntry<Block, T> castedEntry = (RegistryEntry<Block, T>) entry;
            castedEntry.returnSupplier = BLOCKS.registerBlock(entry.id,
                    props -> castedEntry.creationSupplier.get());

            ModBlocks.register.replace(entry.id, castedEntry);
        }
    }
}
*///?}