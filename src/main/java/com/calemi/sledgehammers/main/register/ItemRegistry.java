package com.calemi.sledgehammers.main.register;

import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.main.item.KnobItem;
import com.calemi.sledgehammers.main.item.SledgehammerItem;
import com.calemi.sledgehammers.main.item.SledgehammerTiers;
import com.calemi.sledgehammers.main.item.StarlightSmithingTemplateItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SledgehammersRef.MOD_ID);

    public static final RegistryObject<Item> STARLIGHT_UPGRADE_SMITHING_TEMPLATE = regItem("starlight_upgrade_smithing_template", StarlightSmithingTemplateItem::new);

    public static final RegistryObject<Item> WOOD_KNOB =                           regItem("wood_knob", KnobItem::new);
    public static final RegistryObject<Item> STONE_KNOB =                          regItem("stone_knob", KnobItem::new);
    public static final RegistryObject<Item> IRON_KNOB =                           regItem("iron_knob", KnobItem::new);
    public static final RegistryObject<Item> GOLD_KNOB =                           regItem("gold_knob", KnobItem::new);
    public static final RegistryObject<Item> DIAMOND_KNOB =                        regItem("diamond_knob", KnobItem::new);
    public static final RegistryObject<Item> NETHERITE_KNOB =                      regItem("netherite_knob", () -> new KnobItem(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> STARLIGHT_KNOB =                      regItem("starlight_knob", () -> new KnobItem(new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> WOOD_SLEDGEHAMMER =                   regItem("wood_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.WOOD));
    public static final RegistryObject<Item> STONE_SLEDGEHAMMER =                  regItem("stone_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STONE));
    public static final RegistryObject<Item> IRON_SLEDGEHAMMER =                   regItem("iron_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.IRON));
    public static final RegistryObject<Item> GOLD_SLEDGEHAMMER =                   regItem("gold_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.GOLD));
    public static final RegistryObject<Item> DIAMOND_SLEDGEHAMMER =                regItem("diamond_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_SLEDGEHAMMER =              regItem("netherite_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.NETHERITE, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> STARLIGHT_SLEDGEHAMMER =              regItem("starlight_sledgehammer", () -> new SledgehammerItem(SledgehammerTiers.STARLIGHT, new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static void init() {
        ITEMS.register(Sledgehammers.MOD_EVENT_BUS);
    }

    public static RegistryObject<Item> regItem(String name, final Supplier<? extends Item> sup) {
        return ITEMS.register(name, sup);
    }
}
