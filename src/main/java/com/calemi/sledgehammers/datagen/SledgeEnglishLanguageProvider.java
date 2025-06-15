package com.calemi.sledgehammers.datagen;

import com.calemi.ccore.api.datagen.CLanguageProvider;
import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.main.SledgeRef;
import com.calemi.sledgehammers.util.SledgeLists;
import net.minecraft.data.PackOutput;

public class SledgeEnglishLanguageProvider extends CLanguageProvider {

    private final String STARLIGHT_UPGRADE = getPrefixedKey("item", "smithing_template.starlight_upgrade.");

    private final String HOVER_TEXT = getPrefixedKey("hover_text", "");
    private final String HOVER_TEXT_SLEDGEHAMMER = HOVER_TEXT + "sledgehammer.";

    private final String ENCHANTMENT = getPrefixedKey("enchantment", "");

    private final String CONFIG = getPrefixedKey("config", "");

    public SledgeEnglishLanguageProvider(PackOutput output) {
        super(SledgeRef.ID, output, "en_us");
    }

    @Override
    protected void addTranslations() {

        /*
            BLOCKS & ITEMS
         */

        SledgeLists.ALL_ITEMS.forEach(item -> {

            if (item.equals(SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.asItem())) {
                add(item, "Smithing Template");
                return;
            }

            addAutoItem(item);
        });

        /*
            TOOLTIPS
         */

        add(getPrefixedKey("upgrade", "smithing_upgrade"), "Starlight Upgrade");
        add(STARLIGHT_UPGRADE + "applies_to", "Netherite Knob");
        add(STARLIGHT_UPGRADE + "ingredients", "Nether Star");
        add(STARLIGHT_UPGRADE + "base_slot_description", "Add Netherite Knob");
        add(STARLIGHT_UPGRADE + "additions_slot_description", "Add Nether Star");

        add(HOVER_TEXT_SLEDGEHAMMER + "1", "Need a pickaxe, axe, shovel and sword in one single tool?");
        add(HOVER_TEXT_SLEDGEHAMMER + "2", "This is your best bet.");
        add(HOVER_TEXT_SLEDGEHAMMER + "use", "Charge");
        add(HOVER_TEXT_SLEDGEHAMMER + "sneak-use", "Force Charge");
        add(HOVER_TEXT_SLEDGEHAMMER + "release-use", "Excavates, Mines Ore Veins & Fells Trees");

        /*
            ENCHANTMENTS
         */

        add(ENCHANTMENT + "crushing", "Crushing");
        add(ENCHANTMENT + "crushing.desc", "Increases the size of a Sledgehammer's normal charge ability.");
    }
}