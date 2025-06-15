package com.calemi.sledgehammers.datagen;

import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SledgeRef.ID, bus = EventBusSubscriber.Bus.MOD)
public class SledgeDataGen {

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        /*
            CLIENT
         */

        //LANGUAGE
        generator.addProvider(event.includeClient(), new SledgeEnglishLanguageProvider(output));

        //ITEM MODELS
        generator.addProvider(event.includeClient(), new SledgeItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new SledgeSledgehammerItemModelProvider(output, existingFileHelper));

        /*
            SERVER
         */

        DatapackBuiltinEntriesProvider datapackProvider = new SledgeDatapackProvider(output, event.getLookupProvider());
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
        SledgeBlockTagProvider blockTagProvider = new SledgeBlockTagProvider(output, lookupProvider, existingFileHelper);

        //DATA PACK
        generator.addProvider(event.includeServer(), datapackProvider);

        //BLOCK TAGS
        generator.addProvider(event.includeServer(), blockTagProvider);

        //ITEM TAGS
        generator.addProvider(event.includeServer(), new SledgeItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));

        //ENCHANTMENT TAGS
        generator.addProvider(event.includeServer(), new SledgeEnchantmentTagsProvider(output, lookupProvider, existingFileHelper));

        //GLOBAL LOOT MODIFIERS
        generator.addProvider(event.includeServer(), new SledgeGlobalLootModifierProvider(output, lookupProvider));

        //RECIPES
        generator.addProvider(event.includeServer(), new SledgeRecipeProvider(output, lookupProvider));
    }
}