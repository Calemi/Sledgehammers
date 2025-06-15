package com.calemi.sledgehammers.main;

import com.calemi.sledgehammers.config.SledgeConfig;
import com.calemi.sledgehammers.tab.CreativeTabInjector;
import com.calemi.sledgehammers.client.render.RenderSledgehammerBlockOverlay;
import com.calemi.sledgehammers.client.render.RenderSledgehammerHUDOverlay;
import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.item.property.SledgeItemProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SledgeRef.ID)
public class Sledgehammers {

    public static final IEventBus FORGE_EVENT_BUS = NeoForge.EVENT_BUS;
    public static IEventBus MOD_EVENT_BUS;
    public static ModContainer MOD_CONTAINER;

    public static final Logger LOGGER = LogManager.getLogger(SledgeRef.NAME);

    public Sledgehammers(IEventBus modEventBus, ModContainer modContainer) {

        MOD_EVENT_BUS = modEventBus;
        MOD_CONTAINER = modContainer;

        SledgeConfig.init();
        SledgeItems.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        MOD_EVENT_BUS.register(new CreativeTabInjector());
    }

    public void clientSetup(final FMLClientSetupEvent event) {

        SledgeItemProperties.init();

        MOD_CONTAINER.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        FORGE_EVENT_BUS.register(new RenderSledgehammerHUDOverlay());
        FORGE_EVENT_BUS.register(new RenderSledgehammerBlockOverlay());
    }
}