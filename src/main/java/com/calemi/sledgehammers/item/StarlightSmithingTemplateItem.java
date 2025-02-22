package com.calemi.sledgehammers.item;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class StarlightSmithingTemplateItem extends SmithingTemplateItem {

    private static final Component STARLIGHT_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "starlight_upgrade"))).withStyle(ChatFormatting.GRAY);
    private static final Component STARLIGHT_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "smithing_template.starlight_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
    private static final Component STARLIGHT_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "smithing_template.starlight_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
    private static final Component STARLIGHT_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "smithing_template.starlight_upgrade.base_slot_description")));
    private static final Component STARLIGHT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "smithing_template.starlight_upgrade.additions_slot_description")));

    private static final ResourceLocation EMPTY_SLOT_KNOB = ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "item/empty_slot_knob");
    private static final ResourceLocation EMPTY_SLOT_NETHER_STAR = ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "item/empty_slot_nether_star");

    public StarlightSmithingTemplateItem() {
        super(STARLIGHT_UPGRADE_APPLIES_TO, STARLIGHT_UPGRADE_INGREDIENTS, STARLIGHT_UPGRADE, STARLIGHT_UPGRADE_BASE_SLOT_DESCRIPTION, STARLIGHT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, createStarlightUpgradeIconList(), createStarlightUpgradeMaterialList());
    }

    private static List<ResourceLocation> createStarlightUpgradeIconList() {
        return List.of(EMPTY_SLOT_KNOB);
    }

    private static List<ResourceLocation> createStarlightUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_NETHER_STAR);
    }
}