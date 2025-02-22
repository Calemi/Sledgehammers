package com.calemi.sledgehammers.main;

import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.event.RenderGuiOverlayEventListener;
import com.calemi.sledgehammers.event.listener.BuildCreativeModeTabContentsEventListener;
import com.calemi.sledgehammers.item.ItemTagLists;
import com.calemi.sledgehammers.register.ItemPropertyRegistry;
import com.calemi.sledgehammers.register.ItemRegistry;
import com.calemi.sledgehammers.register.LootModifierRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(SledgehammersRef.MOD_ID)
public class Sledgehammers {

    public static final IEventBus FORGE_EVENT_BUS = NeoForge.EVENT_BUS;
    public static IEventBus MOD_EVENT_BUS;
    public static ModContainer MOD_CONTAINER;

    public Sledgehammers(IEventBus modEventBus, ModContainer modContainer) {

        MOD_EVENT_BUS = modEventBus;
        MOD_CONTAINER = modContainer;

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        FORGE_EVENT_BUS.register(new ItemTagLists());

        SledgehammersConfig.init();
        ItemRegistry.init();
        LootModifierRegistry.init();
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        MOD_EVENT_BUS.register(new BuildCreativeModeTabContentsEventListener());
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        FORGE_EVENT_BUS.register(new RenderGuiOverlayEventListener());
        ItemPropertyRegistry.init();
    }
}