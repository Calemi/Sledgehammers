package com.calemi.sledgehammers.enchantment;

import com.calemi.sledgehammers.item.SledgehammerItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CrushingEnchantment extends Enchantment {

    private static final EnchantmentCategory HAMMER = EnchantmentCategory.create("weapons", (item) -> (item instanceof SledgehammerItem));

    public CrushingEnchantment() {
        super(Rarity.UNCOMMON, HAMMER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel () {
        return 2;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMaxCost(enchantmentLevel) + 50;
    }
}
