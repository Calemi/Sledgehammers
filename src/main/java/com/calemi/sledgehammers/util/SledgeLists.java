package com.calemi.sledgehammers.util;

import com.calemi.sledgehammers.item.SledgeItems;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SledgeLists {

    public static final List<Item> BREAKABLE_SLEDGEHAMMERS = new ArrayList<>();

    static {
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.WOOD_SLEDGEHAMMER.get());
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.STONE_SLEDGEHAMMER.get());
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.IRON_SLEDGEHAMMER.get());
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.GOLD_SLEDGEHAMMER.get());
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.DIAMOND_SLEDGEHAMMER.get());
        BREAKABLE_SLEDGEHAMMERS.add(SledgeItems.NETHERITE_SLEDGEHAMMER.get());
    }

    public static final List<Item> SLEDGEHAMMERS = new ArrayList<>();

    static {
        SLEDGEHAMMERS.addAll(BREAKABLE_SLEDGEHAMMERS);
        SLEDGEHAMMERS.add(SledgeItems.STARLIGHT_SLEDGEHAMMER.get());
    }

    public static final List<Item> KNOBS = new ArrayList<>();

    static {
        KNOBS.add(SledgeItems.WOOD_KNOB.get());
        KNOBS.add(SledgeItems.STONE_KNOB.get());
        KNOBS.add(SledgeItems.IRON_KNOB.get());
        KNOBS.add(SledgeItems.GOLD_KNOB.get());
        KNOBS.add(SledgeItems.DIAMOND_KNOB.get());
        KNOBS.add(SledgeItems.NETHERITE_KNOB.get());
        KNOBS.add(SledgeItems.STARLIGHT_KNOB.get());
    }

    public static final List<Item> ALL_ITEMS = new ArrayList<>();

    static {
        ALL_ITEMS.addAll(SLEDGEHAMMERS);
        ALL_ITEMS.addAll(KNOBS);
        ALL_ITEMS.add(SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get());
    }
}

