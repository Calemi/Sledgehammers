package com.calemi.sledgehammers.item;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public enum SledgehammerTiers implements Tier {

    WOOD      (0, 20 * 10, 2F, 6F, 1.2F, 15, 50, () -> {return Ingredient.of(ItemTags.PLANKS);}),
    STONE     (1, 44 * 10, 4F, 7F, 1.2F, 5, 35, () -> {return Ingredient.of(Items.STONE);}),
    IRON      (2, 83 * 10, 6F, 8F, 1.3F, 14, 25, () -> {return Ingredient.of(Items.IRON_INGOT);}),
    GOLD      (0, 11 * 10, 12F, 6F, 1.3F, 22, 15, () -> {return Ingredient.of(Items.GOLD_INGOT);}),
    DIAMOND   (3, 520 * 10, 8F, 8F, 1.3F, 10, 20, () -> {return Ingredient.of(Items.DIAMOND);}),
    NETHERITE (4, 677 * 10, 9F, 9F, 1.3F, 15, 15, () -> {return Ingredient.of(Items.NETHERITE_INGOT);}),
    STARLIGHT (5, 10000000, 20F, 15F, 1.3F, 25, 10, () -> {return Ingredient.of(Blocks.AIR);});

    private final int harvestLevel;
    private final int durability;

    private final float efficiency;

    private final float attackDamage;
    private final float attackSpeed;

    private final int enchantability;

    private final int chargeTime;

    private final LazyLoadedValue<Ingredient> repairMaterial;

    SledgehammerTiers(int harvestLevel, int durability, float efficiency, float attackDamage, float attackSpeed, int enchantability, int chargeTime, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.durability = durability;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.chargeTime = chargeTime;
        this.attackSpeed = attackSpeed;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getUses() {
        return durability;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}
