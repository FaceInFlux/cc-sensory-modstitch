//? if fabric && 1.21.8 {
package io.github.faceinflux.ccsensory.loaders.fabric.datagen;

import io.github.faceinflux.ccsensory.content.items.ModBlocks;
import io.github.faceinflux.ccsensory.content.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    // This is a bit different from docs, which use getRecipeGenerator. I think this is smth to do with mappings?
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.testBlock.get().asItem())
                        .requires(ModItems.testItem.get(), 9)
                        // Recipe book unlock. This is required to prevent crashing I think
                        .unlockedBy("has_test_item", this.has(ModItems.testItem.get()))
                        .save(exporter); // Equivalent to fabric offerTo
            }
        };
    }


    @Override
    public String getName() {
        return "CC Sensory Recipes";
    }
}
//?}