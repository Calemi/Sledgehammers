package com.calemi.sledgehammers.loot;

import com.calemi.ccore.api.math.MathHelper;
import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.register.ItemRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class StarlightUpgradeLootModifier extends LootModifier {

    public StarlightUpgradeLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        if (context.getQueriedLootTableId().getPath().equals("chests/ancient_city") || context.getQueriedLootTableId().getPath().equals("chests/end_city_treasure")) {

            if (MathHelper.rollChance(SledgehammersConfig.server.starlightUpgradeSpawnChance.get())) {
                generatedLoot.add(new ItemStack(ItemRegistry.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get(), SledgehammersConfig.server.starlightUpgradeSpawnAmount.get()));
            }
        }

        return generatedLoot;
    }

    public static final MapCodec<StarlightUpgradeLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, StarlightUpgradeLootModifier::new));

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}