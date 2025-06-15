package com.calemi.sledgehammers.datagen;

import com.calemi.ccore.api.loot.condition.LootTableCheck;
import com.calemi.ccore.api.loot.modifier.BonusItemLootModifier;
import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SledgeGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public SledgeGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, SledgeRef.ID);
    }

    @Override
    protected void start() {
        add("starlight_upgrade_from_ancient_city", new BonusItemLootModifier(new LootItemCondition[]{
                new LootTableCheck(List.of(
                        ResourceLocation.withDefaultNamespace("chests/ancient_city"))),
        }, 0.2F, new ItemStack(SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get(), 2)));

        add("starlight_upgrade_from_end_city", new BonusItemLootModifier(new LootItemCondition[]{
                new LootTableCheck(List.of(
                        ResourceLocation.withDefaultNamespace("chests/end_city_treasure"))),
        }, 0.2F, new ItemStack(SledgeItems.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get(), 2)));
    }
}
