package com.calemi.sledgehammers.main;

import com.calemi.sledgehammers.main.config.SledgehammersConfig;
import com.calemi.sledgehammers.main.event.listener.BuildCreativeModeTabContentsEventListener;
import com.calemi.sledgehammers.main.event.listener.RenderGuiOverlayEventListener;
import com.calemi.sledgehammers.main.item.ItemTagLists;
import com.calemi.sledgehammers.main.register.EnchantmentRegistry;
import com.calemi.sledgehammers.main.register.ItemRegistry;
import com.calemi.sledgehammers.main.register.LootModifierRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SledgehammersRef.MOD_ID)
public class Sledgehammers {

    public static final IEventBus FORGE_EVENT_BUS = MinecraftForge.EVENT_BUS;
    public static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public Sledgehammers() {

        MOD_EVENT_BUS.addListener(this::commonSetup);
        MOD_EVENT_BUS.addListener(this::clientSetup);
        FORGE_EVENT_BUS.register(new ItemTagLists());

        SledgehammersConfig.init();
        ItemRegistry.init();
        EnchantmentRegistry.init();
        LootModifierRegistry.init();
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        MOD_EVENT_BUS.register(new BuildCreativeModeTabContentsEventListener());
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        FORGE_EVENT_BUS.register(new RenderGuiOverlayEventListener());
    }
}
