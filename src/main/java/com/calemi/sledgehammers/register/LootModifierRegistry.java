package com.calemi.sledgehammers.register;

import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.loot.StarlightUpgradeLootModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class LootModifierRegistry {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SledgehammersRef.MOD_ID);

    public static final Supplier<MapCodec<StarlightUpgradeLootModifier>> STARLIGHT_UPGRADE  =
            LOOT_MODIFIERS.register("starlight_upgrade", () -> StarlightUpgradeLootModifier.CODEC);

    public static void init() {
        LOOT_MODIFIERS.register(Sledgehammers.MOD_EVENT_BUS);
    }
}