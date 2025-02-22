package com.calemi.sledgehammers.item;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Supplier;

public enum SledgehammerTiers implements Tier {

    WOOD      (20 * 10, 2F, 6F, 1.2F, 15, 50, () -> Ingredient.of(ItemTags.PLANKS), BlockTags.INCORRECT_FOR_WOODEN_TOOL),
    STONE     (44 * 10, 4F, 7F, 1.2F, 5, 35, () -> Ingredient.of(Items.STONE), BlockTags.INCORRECT_FOR_STONE_TOOL),
    IRON      (83 * 10, 6F, 8F, 1.3F, 14, 25, () -> Ingredient.of(Items.IRON_INGOT), BlockTags.INCORRECT_FOR_IRON_TOOL),
    GOLD      (11 * 10, 12F, 6F, 1.3F, 22, 15, () -> Ingredient.of(Items.GOLD_INGOT), BlockTags.INCORRECT_FOR_GOLD_TOOL),
    DIAMOND   (520 * 10, 8F, 8F, 1.3F, 10, 20, () -> Ingredient.of(Items.DIAMOND), BlockTags.INCORRECT_FOR_DIAMOND_TOOL),
    NETHERITE (677 * 10, 9F, 9F, 1.3F, 15, 15, () -> Ingredient.of(Items.NETHERITE_INGOT), BlockTags.INCORRECT_FOR_NETHERITE_TOOL),
    STARLIGHT (10000000, 20F, 15F, 1.3F, 25, 10, () -> Ingredient.of(Blocks.AIR), BlockTags.INCORRECT_FOR_NETHERITE_TOOL);

    private final int durability;

    private final float efficiency;

    private final float attackDamage;
    private final float attackSpeed;

    private final int enchantability;

    private final int chargeTime;

    private final Supplier<Ingredient> repairIngredient;

    private final TagKey<Block> incorrectBlocksForDrops;

    SledgehammerTiers (int durability, float efficiency, float attackDamage, float attackSpeed, int enchantability, int chargeTime, Supplier<Ingredient> repairIngredient, TagKey<Block> incorrectBlockForDrops) {
        this.durability = durability;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.chargeTime = chargeTime;
        this.attackSpeed = attackSpeed;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
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
        return repairIngredient.get();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    public Tool createToolProperties() {
        return new Tool(List.of(
                Tool.Rule.deniesDrops(getIncorrectBlocksForDrops()),
                Tool.Rule.minesAndDrops(BuiltInRegistries.BLOCK.stream().toList(), getSpeed())), 1.5F, 1);
    }
}