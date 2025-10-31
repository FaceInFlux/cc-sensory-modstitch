//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.SimpleRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemRegistryHandler {
    public static void registerItems() {
        CCSensory.LOGGER.info("Registering items.");
        for (RegistryEntry<Item, ?> entry : ModItems.register.values()) {
            SimpleRegistryEntry<Item, ?> castEntry = (SimpleRegistryEntry<Item, ?>) entry;
            registerItem(castEntry); // Needs to be a separate method because of generic funkiness
        }
    }

    private static <T extends Item> void registerItem(SimpleRegistryEntry<Item, T> entry) {
        CCSensory.LOGGER.info("Registering item {}", entry.id);
        T registeredItem = Registry.register(
                BuiltInRegistries.ITEM,
                ModItems.itemKey(entry.id),
                entry.creationSupplier.get());

        entry.returnSupplier = () -> registeredItem;
    }
}
//?}