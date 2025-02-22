package com.calemi.sledgehammers.event.listener;

import com.calemi.sledgehammers.register.ItemRegistry;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class BuildCreativeModeTabContentsEventListener {

    @SubscribeEvent
    public void onBuildCreativeModeTabContentsEvent(final BuildCreativeModeTabContentsEvent event) {

        CreativeModeTab tab = event.getTab();


        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            add(event, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ItemRegistry.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get());
            add(event, Items.NETHERITE_INGOT, ItemRegistry.WOOD_KNOB.get(), ItemRegistry.STONE_KNOB.get(), ItemRegistry.IRON_KNOB.get(), ItemRegistry.GOLD_KNOB.get(), ItemRegistry.DIAMOND_KNOB.get(), ItemRegistry.NETHERITE_KNOB.get(), ItemRegistry.STARLIGHT_KNOB.get());
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            add(event, Items.NETHERITE_HOE, ItemRegistry.WOOD_SLEDGEHAMMER.get(), ItemRegistry.STONE_SLEDGEHAMMER.get(), ItemRegistry.IRON_SLEDGEHAMMER.get(), ItemRegistry.GOLD_SLEDGEHAMMER.get(), ItemRegistry.DIAMOND_SLEDGEHAMMER.get(), ItemRegistry.NETHERITE_SLEDGEHAMMER.get(), ItemRegistry.STARLIGHT_SLEDGEHAMMER.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            add(event, Items.NETHERITE_SWORD, ItemRegistry.WOOD_SLEDGEHAMMER.get(), ItemRegistry.STONE_SLEDGEHAMMER.get(), ItemRegistry.IRON_SLEDGEHAMMER.get(), ItemRegistry.GOLD_SLEDGEHAMMER.get(), ItemRegistry.DIAMOND_SLEDGEHAMMER.get(), ItemRegistry.NETHERITE_SLEDGEHAMMER.get(), ItemRegistry.STARLIGHT_SLEDGEHAMMER.get());
        }
    }

    private void add(BuildCreativeModeTabContentsEvent event, Item item, Item... itemsToAdd) {

        for (int i = 0; i < itemsToAdd.length; i++) {
            Item itemToAdd = itemsToAdd[i];
            event.insertAfter(new ItemStack(i == 0 ? item : itemsToAdd[i - 1]), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}