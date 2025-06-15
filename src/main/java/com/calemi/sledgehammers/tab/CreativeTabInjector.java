package com.calemi.sledgehammers.tab;

import com.calemi.sledgehammers.item.SledgeItems;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class CreativeTabInjector {

    @SubscribeEvent
    public void onBuildCreativeModeTabContentsEvent(final BuildCreativeModeTabContentsEvent event) {

        CreativeModeTab tab = event.getTab();

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            add(event, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get());
            add(event, Items.NETHERITE_INGOT, SledgeItems.WOOD_KNOB.get(), SledgeItems.STONE_KNOB.get(), SledgeItems.IRON_KNOB.get(), SledgeItems.GOLD_KNOB.get(), SledgeItems.DIAMOND_KNOB.get(), SledgeItems.NETHERITE_KNOB.get(), SledgeItems.STARLIGHT_KNOB.get());
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            add(event, Items.NETHERITE_HOE, SledgeItems.WOOD_SLEDGEHAMMER.get(), SledgeItems.STONE_SLEDGEHAMMER.get(), SledgeItems.IRON_SLEDGEHAMMER.get(), SledgeItems.GOLD_SLEDGEHAMMER.get(), SledgeItems.DIAMOND_SLEDGEHAMMER.get(), SledgeItems.NETHERITE_SLEDGEHAMMER.get(), SledgeItems.STARLIGHT_SLEDGEHAMMER.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            add(event, Items.NETHERITE_SWORD, SledgeItems.WOOD_SLEDGEHAMMER.get(), SledgeItems.STONE_SLEDGEHAMMER.get(), SledgeItems.IRON_SLEDGEHAMMER.get(), SledgeItems.GOLD_SLEDGEHAMMER.get(), SledgeItems.DIAMOND_SLEDGEHAMMER.get(), SledgeItems.NETHERITE_SLEDGEHAMMER.get(), SledgeItems.STARLIGHT_SLEDGEHAMMER.get());
        }
    }

    private void add(BuildCreativeModeTabContentsEvent event, Item item, Item... itemsToAdd) {

        for (int i = 0; i < itemsToAdd.length; i++) {
            Item itemToAdd = itemsToAdd[i];
            event.insertAfter(new ItemStack(i == 0 ? item : itemsToAdd[i - 1]), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}