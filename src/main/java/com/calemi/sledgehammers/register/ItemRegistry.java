package com.calemi.sledgehammers.register;

import com.calemi.sledgehammers.item.KnobItem;
import com.calemi.sledgehammers.item.SledgehammerItem;
import com.calemi.sledgehammers.item.SledgehammerTiers;
import com.calemi.sledgehammers.item.StarlightSmithingTemplateItem;
import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SledgehammersRef.MOD_ID);

    public static final Supplier<Item> STARLIGHT_UPGRADE_SMITHING_TEMPLATE = regItem("starlight_upgrade_smithing_template", StarlightSmithingTemplateItem::new);

    public static final Supplier<Item> WOOD_KNOB =                           regItem("wood_knob", KnobItem::new);
    public static final Supplier<Item> STONE_KNOB =                          regItem("stone_knob", KnobItem::new);
    public static final Supplier<Item> IRON_KNOB =                           regItem("iron_knob", KnobItem::new);
    public static final Supplier<Item> GOLD_KNOB =                           regItem("gold_knob", KnobItem::new);
    public static final Supplier<Item> DIAMOND_KNOB =                        regItem("diamond_knob", KnobItem::new);
    public static final Supplier<Item> NETHERITE_KNOB =                      regItem("netherite_knob", () -> new KnobItem(new Item.Properties().fireResistant()));
    public static final Supplier<Item> STARLIGHT_KNOB =                      regItem("starlight_knob", () -> new KnobItem(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static final Supplier<Item> WOOD_SLEDGEHAMMER =                   regItem("wood_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.WOOD, new Item.Properties()));
    public static final Supplier<Item> STONE_SLEDGEHAMMER =                  regItem("stone_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STONE, new Item.Properties()));
    public static final Supplier<Item> IRON_SLEDGEHAMMER =                   regItem("iron_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.IRON, new Item.Properties()));
    public static final Supplier<Item> GOLD_SLEDGEHAMMER =                   regItem("gold_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.GOLD, new Item.Properties()));
    public static final Supplier<Item> DIAMOND_SLEDGEHAMMER =                regItem("diamond_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.DIAMOND, new Item.Properties()));
    public static final Supplier<Item> NETHERITE_SLEDGEHAMMER =              regItem("netherite_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.NETHERITE, new Item.Properties().fireResistant()));
    public static final Supplier<Item> STARLIGHT_SLEDGEHAMMER =              regItem("starlight_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STARLIGHT, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static void init() {
        ITEMS.register(Sledgehammers.MOD_EVENT_BUS);
    }

    public static Supplier<Item> regItem(String name, final Supplier<? extends Item> sup) {
        return ITEMS.register(name, sup);
    }
}