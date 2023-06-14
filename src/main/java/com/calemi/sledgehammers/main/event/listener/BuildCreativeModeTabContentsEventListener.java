package com.calemi.sledgehammers.main.event.listener;

import com.calemi.sledgehammers.main.register.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BuildCreativeModeTabContentsEventListener {

    @SubscribeEvent
    public void onBuildCreativeModeTabContentsEvent(final BuildCreativeModeTabContentsEvent event) {

        CreativeModeTab tab = event.getTab();

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ItemRegistry.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get());
            event.accept(ItemRegistry.WOOD_KNOB.get());
            event.accept(ItemRegistry.STONE_KNOB.get());
            event.accept(ItemRegistry.IRON_KNOB.get());
            event.accept(ItemRegistry.GOLD_KNOB.get());
            event.accept(ItemRegistry.DIAMOND_KNOB.get());
            event.accept(ItemRegistry.NETHERITE_KNOB.get());
            event.accept(ItemRegistry.STARLIGHT_KNOB.get());
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES || event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ItemRegistry.WOOD_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.STONE_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.IRON_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.GOLD_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.DIAMOND_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.NETHERITE_SLEDGEHAMMER.get());
            event.accept(ItemRegistry.STARLIGHT_SLEDGEHAMMER.get());
        }
    }
}
