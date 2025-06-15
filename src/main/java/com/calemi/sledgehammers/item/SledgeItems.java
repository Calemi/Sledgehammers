package com.calemi.sledgehammers.item;

import com.calemi.ccore.api.item.SmithingTemplateHelper;
import com.calemi.sledgehammers.item.tier.SledgehammerTiers;
import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class SledgeItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SledgeRef.ID);

    public static final DeferredItem<Item> STARLIGHT_UPGRADE_SMITHING_TEMPLATE = regItem("starlight_upgrade_smithing_template",
            () -> SmithingTemplateHelper.createCustomUpgradeTemplate(SledgeRef.ID, "starlight", List.of("knob"), List.of("nether_star")));

    public static final DeferredItem<Item> WOOD_KNOB =                           regItem("wood_knob", KnobItem::new);
    public static final DeferredItem<Item> STONE_KNOB =                          regItem("stone_knob", KnobItem::new);
    public static final DeferredItem<Item> IRON_KNOB =                           regItem("iron_knob", KnobItem::new);
    public static final DeferredItem<Item> GOLD_KNOB =                           regItem("gold_knob", KnobItem::new);
    public static final DeferredItem<Item> DIAMOND_KNOB =                        regItem("diamond_knob", KnobItem::new);
    public static final DeferredItem<Item> NETHERITE_KNOB =                      regItem("netherite_knob", () -> new KnobItem(new Item.Properties().fireResistant()));
    public static final DeferredItem<Item> STARLIGHT_KNOB =                      regItem("starlight_knob", () -> new KnobItem(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> WOOD_SLEDGEHAMMER =                   regItem("wood_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.WOOD, new Item.Properties()));
    public static final DeferredItem<Item> STONE_SLEDGEHAMMER =                  regItem("stone_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STONE, new Item.Properties()));
    public static final DeferredItem<Item> IRON_SLEDGEHAMMER =                   regItem("iron_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.IRON, new Item.Properties()));
    public static final DeferredItem<Item> GOLD_SLEDGEHAMMER =                   regItem("gold_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.GOLD, new Item.Properties()));
    public static final DeferredItem<Item> DIAMOND_SLEDGEHAMMER =                regItem("diamond_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.DIAMOND, new Item.Properties()));
    public static final DeferredItem<Item> NETHERITE_SLEDGEHAMMER =              regItem("netherite_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.NETHERITE, new Item.Properties().fireResistant()));
    public static final DeferredItem<Item> STARLIGHT_SLEDGEHAMMER =              regItem("starlight_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STARLIGHT, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static void init() {
        Sledgehammers.LOGGER.info("Registering: Items - Start");
        ITEMS.register(Sledgehammers.MOD_EVENT_BUS);
        Sledgehammers.LOGGER.info("Registering: Items - End");
    }

    public static DeferredItem<Item> regItem(String name, Supplier<Item> sup) {
        return ITEMS.register(name, sup);
    }
}