package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgehammersRef;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SledgehammersRef.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new SledgehammerModelGen(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }
}