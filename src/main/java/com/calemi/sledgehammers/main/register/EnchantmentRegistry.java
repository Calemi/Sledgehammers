package com.calemi.sledgehammers.main.register;

import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.main.enchantment.CrushingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SledgehammersRef.MOD_ID);

    public static final RegistryObject<Enchantment> CRUSHING = ENCHANTMENTS.register("crushing", CrushingEnchantment::new);

    public static void init() {
        ENCHANTMENTS.register(Sledgehammers.MOD_EVENT_BUS);
    }
}