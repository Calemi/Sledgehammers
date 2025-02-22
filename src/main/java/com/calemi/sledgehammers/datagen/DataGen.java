package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = SledgehammersRef.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new SimpleItemModelGen(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SledgehammerModelGen(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }
}