package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class SledgehammerModelGen extends ItemModelProvider {

    private final float[] TRIM_MATERIALS_INDEXES = {0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F};
    private final String[] TRIM_MATERIALS = {"quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"};
    private final String[] TRIM_TYPES = {"coast", "dune", "eye", "host", "raiser", "rib", "sentry", "shaper", "silence", "snout", "spire", "tide", "vex", "ward", "wayfinder", "wild"};

    public SledgehammerModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SledgehammersRef.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        genSledgehammerModels("wood");
        genSledgehammerModels("stone");
        genSledgehammerModels("iron");
        genSledgehammerModels("gold");
        genSledgehammerModels("diamond");
        genSledgehammerModels("netherite");
        genSledgehammerModels("starlight");
    }

    private void genSledgehammerModels(String hammerMatName) {
        ResourceLocation location = new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer");

        ItemModelBuilder card = factory.apply(location);
        card.parent(this.factory.apply(new ResourceLocation(SledgehammersRef.MOD_ID, "item/base_sledgehammer")));
        card.texture("base", new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer"));

        for (int m = 0; m < TRIM_MATERIALS.length; m++) {

            for (int t = 0; t < TRIM_TYPES.length; t++) {

                ItemModelBuilder.OverrideBuilder cardOverride = card.override();
                cardOverride.predicate(new ResourceLocation(SledgehammersRef.MOD_ID, "trim_material"), TRIM_MATERIALS_INDEXES[m]);
                cardOverride.predicate(new ResourceLocation(SledgehammersRef.MOD_ID, "trim_type"), t + 1);
                cardOverride.model(genSledgehammerTrim(hammerMatName, TRIM_MATERIALS[m], TRIM_TYPES[t]));
            }
        }

        generatedModels.put(location, card);
    }

    private ModelFile genSledgehammerTrim(String hammerMatName, String trimMatName, String trimTypeName) {

        String darker = hammerMatName.equalsIgnoreCase(trimMatName) ? "_darker" : "";

        ResourceLocation location = new ResourceLocation(SledgehammersRef.MOD_ID, "item/sledgehammers/" + hammerMatName + "/" + hammerMatName + "_sledgehammer_" + trimMatName + "_" + trimTypeName);

        ItemModelBuilder card = factory.apply(location);
        card.parent(factory.apply(new ResourceLocation(SledgehammersRef.MOD_ID, "item/base_sledgehammer")));
        card.texture("base", new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer"));
        card.texture("overlay", SledgehammersRef.MOD_ID + ":trims/items/sledgehammer_trim_" + trimTypeName + "_" + trimMatName + darker);

        generatedModels.put(location, card);
        return card;
    }

    @NotNull
    @Override
    public String getName() {
        return "Sledgehammer Models: " + modid;
    }
}