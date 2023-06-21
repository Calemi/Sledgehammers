package com.calemi.sledgehammers.register;

import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.loot.StarlightUpgradeLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierRegistry {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SledgehammersRef.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> STARLIGHT_UPGRADE = LOOT_MODIFIERS.register("starlight_upgrade", StarlightUpgradeLootModifier::makeCodec);

    public static void init() {
        LOOT_MODIFIERS.register(Sledgehammers.MOD_EVENT_BUS);
    }
}
