package com.calemi.sledgehammers.datagen;

import com.calemi.ccore.api.datagen.CRecipeProvider;
import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class SledgeRecipeProvider extends CRecipeProvider {

    public SledgeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(SledgeRef.ID, output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        sledgehammer(
                SledgeItems.WOOD_SLEDGEHAMMER.asItem(),
                SledgeItems.WOOD_KNOB.asItem(),
                ItemTags.PLANKS,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );

        sledgehammer(
                SledgeItems.STONE_SLEDGEHAMMER.asItem(),
                SledgeItems.STONE_KNOB.asItem(),
                ItemTags.STONE_CRAFTING_MATERIALS,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );

        sledgehammer(
                SledgeItems.IRON_SLEDGEHAMMER.asItem(),
                SledgeItems.IRON_KNOB.asItem(),
                Tags.Items.INGOTS_IRON,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );

        sledgehammer(
                SledgeItems.GOLD_SLEDGEHAMMER.asItem(),
                SledgeItems.GOLD_KNOB.asItem(),
                Tags.Items.INGOTS_GOLD,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );


        sledgehammer(
                SledgeItems.DIAMOND_SLEDGEHAMMER.asItem(),
                SledgeItems.DIAMOND_KNOB.asItem(),
                Tags.Items.GEMS_DIAMOND,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );

        sledgehammer(
                SledgeItems.NETHERITE_SLEDGEHAMMER.asItem(),
                SledgeItems.NETHERITE_KNOB.asItem(),
                Tags.Items.INGOTS_NETHERITE,
                Tags.Items.RODS_WOODEN,
                recipeOutput
        );

        sledgehammer(
                SledgeItems.STARLIGHT_SLEDGEHAMMER.asItem(),
                SledgeItems.STARLIGHT_KNOB.asItem(),
                Tags.Items.GEMS_AMETHYST,
                Tags.Items.RODS_BREEZE,
                recipeOutput
        );

        knob(
                SledgeItems.WOOD_KNOB.asItem(),
                ItemTags.PLANKS,
                recipeOutput
        );

        knob(
                SledgeItems.STONE_KNOB.asItem(),
                ItemTags.STONE_CRAFTING_MATERIALS,
                recipeOutput
        );

        knob(
                SledgeItems.IRON_KNOB.asItem(),
                Tags.Items.INGOTS_IRON,
                recipeOutput
        );

        knob(
                SledgeItems.GOLD_KNOB.asItem(),
                Tags.Items.INGOTS_GOLD,
                recipeOutput
        );

        knob(
                SledgeItems.DIAMOND_KNOB.asItem(),
                Tags.Items.GEMS_DIAMOND,
                recipeOutput
        );

        knobSmithing(
                SledgeItems.NETHERITE_KNOB.asItem(),
                SledgeItems.DIAMOND_KNOB.asItem(),
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Tags.Items.INGOTS_NETHERITE,
                recipeOutput
        );

        knobSmithing(
                SledgeItems.STARLIGHT_KNOB.asItem(),
                SledgeItems.NETHERITE_KNOB.asItem(),
                SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.asItem(),
                Tags.Items.NETHER_STARS,
                recipeOutput
        );
    }

    private void sledgehammer(ItemLike result, ItemLike knob, TagKey<Item> headMaterial, TagKey<Item> handleMaterial, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .define('K', knob)
                .define('X', headMaterial)
                .define('H', handleMaterial)
                .pattern("KXK")
                .pattern(" H ")
                .pattern(" H ")
                .unlockedBy("has_knob", has(knob))
                .save(recipeOutput, SledgeRef.rl(getItemName(result)));
    }

    private void knob(ItemLike result, TagKey<Item> material, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result, 1)
                .define('X', material)
                .pattern(" X ")
                .pattern("XXX")
                .pattern(" X ")
                .unlockedBy("has_material", has(material))
                .save(recipeOutput, SledgeRef.rl(getItemName(result)));
    }

    private void knobSmithing(Item result, ItemLike base, ItemLike template, TagKey<Item> addition, RecipeOutput recipeOutput) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        RecipeCategory.TOOLS,
                        result
                )
                .unlocks("has_knob", has(base))
                .save(recipeOutput, SledgeRef.rl(getItemName(result)));
    }
}
