package io.github.faceinflux.ccsensory.content.items;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.util.Register;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final Register<Item> register = new Register<Item>();

    public static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(
                Registries.ITEM,
                //? if >=1.20.6 {
                ResourceLocation.fromNamespaceAndPath(CCSensory.ID, name)
                //?} else {
                /*new ResourceLocation(CCSensory.ID, name)
                *///?}
        );
    }

    private static <T extends Item> RegistryEntry<Item, T> registerItem(
            ResourceKey<Item> key,
            Function<Item.Properties, T> factory,
            Item.Properties properties) {
        //? if >=1.21.2 {
        Supplier<T> supplier = () -> factory.apply(properties.setId(key));
        //?} else {
        /*Supplier<T> supplier = () -> factory.apply(properties);
        *///?}
        return register.register(key.location().getPath(), supplier);
    }

    public static final RegistryEntry<Item, Item> testItem = registerItem(
            itemKey("testitem"),
            Item::new,
            new Item.Properties());
}
