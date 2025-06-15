package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class SledgeSledgehammerItemModelProvider extends ItemModelProvider {

    private final float[] TRIM_MATERIALS_INDEXES = {0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F};
    private final String[] TRIM_MATERIALS = {"quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"};
    private final String[] TRIM_TYPES = {"coast", "dune", "eye", "host", "raiser", "rib", "sentry", "shaper", "silence", "snout", "spire", "tide", "vex", "ward", "wayfinder", "wild", "flow", "bolt"};

    public SledgeSledgehammerItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SledgeRef.ID, existingFileHelper);
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
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/" + hammerMatName + "_sledgehammer");

        ItemModelBuilder card = factory.apply(location);
        card.parent(this.factory.apply(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/base_sledgehammer")));
        card.texture("base", ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/" + hammerMatName + "_sledgehammer"));

        for (int m = 0; m < TRIM_MATERIALS.length; m++) {

            for (int t = 0; t < TRIM_TYPES.length; t++) {

                ItemModelBuilder.OverrideBuilder cardOverride = card.override();
                cardOverride.predicate(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "trim_material"), TRIM_MATERIALS_INDEXES[m]);
                cardOverride.predicate(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "trim_type"), t + 1);
                cardOverride.model(genSledgehammerTrim(hammerMatName, TRIM_MATERIALS[m], TRIM_TYPES[t]));
            }
        }

        generatedModels.put(location, card);
    }

    private ModelFile genSledgehammerTrim(String hammerMatName, String trimMatName, String trimTypeName) {

        String darker = hammerMatName.equalsIgnoreCase(trimMatName) ? "_darker" : "";

        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/sledgehammers/" + hammerMatName + "/" + hammerMatName + "_sledgehammer_" + trimMatName + "_" + trimTypeName);

        ItemModelBuilder card = factory.apply(location);
        card.parent(factory.apply(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/base_sledgehammer")));
        card.texture("base", ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "item/" + hammerMatName + "_sledgehammer"));
        card.texture("overlay", SledgeRef.ID + ":trims/items/sledgehammer_trim_" + trimTypeName + "_" + trimMatName + darker);

        generatedModels.put(location, card);
        return card;
    }

    @NotNull
    @Override
    public String getName() {
        return "Sledgehammer Models: " + modid;
    }
}