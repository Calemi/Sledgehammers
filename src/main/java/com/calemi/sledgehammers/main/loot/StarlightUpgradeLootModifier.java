package com.calemi.sledgehammers.main.loot;

import com.calemi.ccore.api.general.helper.MathHelper;
import com.calemi.sledgehammers.main.config.SledgehammersConfig;
import com.calemi.sledgehammers.main.register.ItemRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class StarlightUpgradeLootModifier extends LootModifier {

    public StarlightUpgradeLootModifier() {
        super(new LootItemCondition[0]);
    }

    public StarlightUpgradeLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        if (context.getQueriedLootTableId().getPath().equals("chests/ancient_city") || context.getQueriedLootTableId().getPath().equals("chests/end_city_treasure")) {

            if (MathHelper.rollChance(SledgehammersConfig.server.starlightUpgradeSpawnChance.get())) {
                generatedLoot.add(new ItemStack(ItemRegistry.STARLIGHT_UPGRADE_SMITHING_TEMPLATE.get(), 2));
            }
        }

        return generatedLoot;
    }

    private static final Codec<StarlightUpgradeLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, StarlightUpgradeLootModifier::new));

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }


    public static Codec<StarlightUpgradeLootModifier> makeCodec() {
        return Codec.unit(StarlightUpgradeLootModifier::new);
    }
}
