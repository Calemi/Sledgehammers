package com.calemi.sledgehammers.item.property;

import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.level.Level;

public class SledgeItemProperties {

    public static void init() {
        registerSledgehammerProperty(SledgeItems.WOOD_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.STONE_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.IRON_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.GOLD_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.DIAMOND_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.NETHERITE_SLEDGEHAMMER.get());
        registerSledgehammerProperty(SledgeItems.STARLIGHT_SLEDGEHAMMER.get());
    }

    private static void registerSledgehammerProperty(Item item) {
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "trim_material"),
                (stack, level, player, damage) -> getTrimMaterialIndex(level, stack));

        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "trim_type"),
                (stack, level, player, damage) -> getTrimTypeIndex(level, stack));
    }

    private static float getTrimMaterialIndex(Level level, ItemStack stack) {

        if (level == null) {
            return 0;
        }

        ArmorTrim armorTrim = stack.getComponents().get(DataComponents.TRIM);

        if (armorTrim == null) {
            return 0;
        }

        return armorTrim.material().value().itemModelIndex();
    }

    private static int getTrimTypeIndex(Level level, ItemStack stack) {

        if (level == null) {
            return 0;
        }

        ArmorTrim armorTrim = stack.getComponents().get(DataComponents.TRIM);

        if (armorTrim == null) {
            return 0;
        }

        ResourceKey<TrimPattern> templateItem = armorTrim.pattern().getKey();

        if (templateItem == null) {
            return 0;
        }

        if (templateItem.equals(TrimPatterns.COAST)) {
            return 1;
        }

        if (templateItem.equals(TrimPatterns.DUNE)) {
            return 2;
        }

        if (templateItem.equals(TrimPatterns.EYE)) {
            return 3;
        }

        if (templateItem.equals(TrimPatterns.HOST)) {
            return 4;
        }

        if (templateItem.equals(TrimPatterns.RAISER)) {
            return 5;
        }

        if (templateItem.equals(TrimPatterns.RIB)) {
            return 6;
        }

        if (templateItem.equals(TrimPatterns.SENTRY)) {
            return 7;
        }

        if (templateItem.equals(TrimPatterns.SHAPER)) {
            return 8;
        }

        if (templateItem.equals(TrimPatterns.SILENCE)) {
            return 9;
        }

        if (templateItem.equals(TrimPatterns.SNOUT)) {
            return 10;
        }

        if (templateItem.equals(TrimPatterns.SPIRE)) {
            return 11;
        }

        if (templateItem.equals(TrimPatterns.TIDE)) {
            return 12;
        }

        if (templateItem.equals(TrimPatterns.VEX)) {
            return 13;
        }

        if (templateItem.equals(TrimPatterns.WARD)) {
            return 14;
        }

        if (templateItem.equals(TrimPatterns.WAYFINDER)) {
            return 15;
        }

        if (templateItem.equals(TrimPatterns.WILD)) {
            return 16;
        }

        if (templateItem.equals(TrimPatterns.FLOW)) {
            return 17;
        }

        if (templateItem.equals(TrimPatterns.BOLT)) {
            return 18;
        }

        return 0;
    }
}