package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SledgehammerModelGen extends ItemModelProvider {

    private final float[] TRIM_MATERIALS_INDEXES = {0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F};
    private final String[] TRIM_MATERIALS = {"quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"};
    private final String[] TRIM_TYPES = {"coast", "dune", "eye", "host", "raiser", "rib", "sentry", "shaper", "silence", "snout", "spire", "tide", "vex", "ward", "wayfinder", "wild"};

    public SledgehammerModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SledgehammersRef.MOD_ID, existingFileHelper);
    }

    protected void registerModels() {
        genSledgehammerModels("wood");
        genSledgehammerModels("stone");
        genSledgehammerModels("iron");
        genSledgehammerModels("gold");
        genSledgehammerModels("diamond");
        genSledgehammerModels("netherite");
        genSledgehammerModels("starlight");
    }

    void genSledgehammerModels(String hammerMatName) {
        ResourceLocation location = new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer");

        ItemModelBuilder card = factory.apply(location);
        card.parent(this.factory.apply(new ResourceLocation(SledgehammersRef.MOD_ID, "item/base_sledgehammer")));
        card.texture("base", new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer"));

        for (int m = 0; m < TRIM_MATERIALS.length; m++) {

            for (int t = 0; t < TRIM_TYPES.length; t++) {

                card.override().predicate(new ResourceLocation(SledgehammersRef.MOD_ID, "trim_material"), TRIM_MATERIALS_INDEXES[m]).predicate(new ResourceLocation(SledgehammersRef.MOD_ID, "trim_type"), t + 1).model(genSledgehammerTrim(hammerMatName, TRIM_MATERIALS[m], TRIM_TYPES[t]));
            }
        }

        this.generatedModels.put(location, card);
    }

    ModelFile genSledgehammerTrim(String hammerMatName, String trimMatName, String trimTypeName) {

        String darker = hammerMatName.equalsIgnoreCase(trimMatName) ? "_darker" : "";

        ResourceLocation location = new ResourceLocation(SledgehammersRef.MOD_ID, "item/sledgehammers/" + hammerMatName + "/" + hammerMatName + "_sledgehammer_" + trimMatName + "_" + trimTypeName);

        ItemModelBuilder card = factory.apply(location);
        card.parent(factory.apply(new ResourceLocation(SledgehammersRef.MOD_ID, "item/base_sledgehammer")));
        card.texture("base", new ResourceLocation(SledgehammersRef.MOD_ID, "item/" + hammerMatName + "_sledgehammer"));
        card.texture("overlay", new ResourceLocation(SledgehammersRef.MOD_ID, "trims/items/sledgehammer_trim_" + trimTypeName + "_" + trimMatName + darker));

        this.generatedModels.put(location, card);
        return card;
    }
}
