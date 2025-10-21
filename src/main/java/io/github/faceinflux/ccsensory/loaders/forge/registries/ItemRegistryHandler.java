//? if forge {
/*package io.github.faceinflux.ccsensory.loaders.forge.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistryHandler {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CCSensory.ID);

    @SuppressWarnings("unchecked")
    public static <T extends Item> void registerItems(IEventBus modBus) {
        CCSensory.LOGGER.info("Registering items.");
        ITEMS.register(modBus);
        for (RegistryEntry<Item, ?> entry : ModItems.register.values()) {
            CCSensory.LOGGER.info("Registering item {}", entry.id);
            // This is really cursed but I'm struggling with generics ;-;
            RegistryEntry<Item, T> castedEntry = (RegistryEntry<Item, T>) entry;
            castedEntry.returnSupplier = ITEMS.register(entry.id,
                    castedEntry.creationSupplier);

            ModItems.register.replace(entry.id, castedEntry);
        }
    }
}
*///?}