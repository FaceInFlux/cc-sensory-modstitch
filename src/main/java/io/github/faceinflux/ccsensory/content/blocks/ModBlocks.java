package io.github.faceinflux.ccsensory.content.blocks;

import io.github.faceinflux.ccsensory.content.items.ModItems;
import io.github.faceinflux.ccsensory.util.Register;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final Register<Block> register = new Register<>();

    public static ResourceKey<Block> blockKey(String name) {
        return Register.makeResourceKey(Registries.BLOCK, name);
    }

    public static <T extends Block> RegistryEntry<Block, T> registerBlock(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, T> factory,
            BlockBehaviour.Properties properties,
            Boolean makeBlockItem
    ) {
        //? if >=1.21.2 {
        Supplier<T> supplier = () -> factory.apply(properties.setId(key));
        //?} else {
        /*Supplier<T> supplier = () -> factory.apply(properties);
         *///?}
        RegistryEntry<Block, T> entry = register.register(key.location().getPath(), supplier);

        if (makeBlockItem) {
            registerBlockItem(entry);
        }

        return entry;
    }

    public static <T extends Block> RegistryEntry<Block, T> registerBlock(
            ResourceKey<Block> key,
            Function<BlockBehaviour.Properties, T> factory,
            BlockBehaviour.Properties properties
    ) {
        return registerBlock(key, factory, properties, true);
    }

    public static RegistryEntry<Item, BlockItem> registerBlockItem(RegistryEntry<Block, ?> block) {
        ResourceKey<Item> key = ModItems.itemKey(block.id);
        return ModItems.registerItem(
                key,
                (Item.Properties properties) -> new BlockItem(block.get(), properties),
                //? if >=1.21.2 {
                new Item.Properties().useBlockDescriptionPrefix()
                //?} else {
                /*new Item.Properties()
                *///?}
        );
    }

    public static final RegistryEntry<Block, Block> testBlock = registerBlock(
            blockKey("testblock"),
            Block::new,
            BlockBehaviour.Properties.of()
    );
}
