//? if forge {
/*package io.github.faceinflux.ccsensory.loaders.forge.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.SimpleRegistryEntry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistryHandler {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CCSensory.ID);

    @SuppressWarnings("unchecked")
    public static <T extends Block> void registerBlocks(IEventBus eventBus) {
        CCSensory.LOGGER.info("Registering blocks");
        BLOCKS.register(eventBus);
        for (RegistryEntry<Block, ?> entry : ModBlocks.register.values()) {
            CCSensory.LOGGER.info("Registering block {}", entry.id);
            // This is really cursed but I'm struggling with generics ;-;
            SimpleRegistryEntry<Block, T> castedEntry = (SimpleRegistryEntry<Block, T>) entry;
            castedEntry.returnSupplier = BLOCKS.register(entry.id,
                    castedEntry.creationSupplier);

            ModBlocks.register.replace(entry.id, castedEntry);
        }
    }
}
*///?}