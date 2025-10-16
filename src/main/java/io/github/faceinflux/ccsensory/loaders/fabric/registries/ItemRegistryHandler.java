//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemRegistryHandler {
    public static void registerItems() {
        CCSensory.LOGGER.info("Registering items.");
        for (RegistryEntry<Item, ?> entry : ModItems.register.values()) {
            CCSensory.LOGGER.info("Registering item {}", entry.id);
            Registry.register(
                    BuiltInRegistries.ITEM,
                    ModItems.itemKey(entry.id),
                    (Item) entry.creationSupplier.get());
        }
    }
}
//?}