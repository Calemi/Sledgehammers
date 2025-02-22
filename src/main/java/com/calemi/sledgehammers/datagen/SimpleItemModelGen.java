package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class SimpleItemModelGen extends ItemModelProvider {

    public SimpleItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SledgehammersRef.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        genItemModel("wood_knob");
        genItemModel("stone_knob");
        genItemModel("iron_knob");
        genItemModel("gold_knob");
        genItemModel("diamond_knob");
        genItemModel("netherite_knob");
        genItemModel("starlight_knob");
        genItemModel("starlight_upgrade_smithing_template");
    }

    private void genItemModel(String itemName) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, itemName);
        ItemModelBuilder card = basicItem(location);
        generatedModels.put(location, card);
    }

    @NotNull
    @Override
    public String getName() {
        return "Simple Item Models: " + modid;
    }
}