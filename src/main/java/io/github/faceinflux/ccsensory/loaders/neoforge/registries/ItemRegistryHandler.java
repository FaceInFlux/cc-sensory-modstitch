package io.github.faceinflux.ccsensory.loaders.neoforge.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistryHandler {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CCSensory.ID);

    @SuppressWarnings("unchecked")
    public static <T extends Item> void registerItems(IEventBus modBus) {
        CCSensory.LOGGER.info("Registering items.");
        ITEMS.register(modBus);
        for (RegistryEntry<Item, ?> entry : ModItems.register.values()) {
            CCSensory.LOGGER.info("Registering item {}", entry.id);
            // This is really cursed but I'm struggling with generics ;-;
            RegistryEntry<Item, T> castedEntry = (RegistryEntry<Item, T>) entry;
            castedEntry.returnSupplier = ITEMS.registerItem(entry.id,
                    props -> castedEntry.creationSupplier.get());

            ModItems.register.replace(entry.id, castedEntry);
        }
    }
}
