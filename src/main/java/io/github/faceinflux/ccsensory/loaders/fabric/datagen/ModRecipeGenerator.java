//? if fabric {
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
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;

//? if >1.21.0 {
import net.minecraft.data.recipes.RecipeOutput;
//?} else {
/*import net.minecraft.data.recipes.FinishedRecipe;
*///?}
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output /*? if >1.20.1 {*/ , CompletableFuture<HolderLookup.Provider> registriesFuture /*?}*/) {
        super(output/*? if >1.20.1 {*/ , registriesFuture /*?}*/);
    }

    //? if >=1.21.2 {
    // This is a bit different from docs, which use getRecipeGenerator. I think this is smth to do with mappings?
    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                commonBuildRecipes(exporter, this);
            }
        };
    }
    //?} else {
    /*@Override
    public void buildRecipes(/^? if <1.21.0 {^//^Consumer<FinishedRecipe> ^//^?} else {^/ RecipeOutput /^?}^/ exporter) {
        commonBuildRecipes(exporter);
    }
    *///?}

    // This is really cursed but I feel like it's about as clean as I can be using Stonecutter
    private void commonBuildRecipes(
            /*? if <1.21.0 {*//*Consumer<FinishedRecipe> *//*?} else {*/ RecipeOutput /*?}*/ exporter
            /*? if >=1.21.2 {*/,RecipeProvider provider /*?}*/) {
        //? if <1.21.2 {
        /*ShapelessRecipeBuilder.
        *///?} else
        provider.
                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.testBlock.get().asItem())
                .requires(ModItems.testItem.get(), 9)
                // Recipe book unlock. This is required to prevent crashing I think
                .unlockedBy("has_test_item", /*? if >=1.21.2 {*/provider./*?}*/has(ModItems.testItem.get()))
                .save(exporter); // Equivalent to offerTo
    }


    @Override
    public String getName() {
        return "CC Sensory Recipes";
    }
}
//?}