//? if fabric && 1.21.8 {
package io.github.faceinflux.ccsensory.loaders.fabric.datagen;

import io.github.faceinflux.ccsensory.content.items.ModBlocks;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelGenerator extends FabricModelProvider {
    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        generator.createTrivialCube(ModBlocks.testBlock.get()); // CUBE_ALL
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(ModItems.testItem.get(), ModelTemplates.FLAT_ITEM);
    }
}
//?}