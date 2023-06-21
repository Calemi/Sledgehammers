package com.calemi.sledgehammers.main;

import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.event.listener.BuildCreativeModeTabContentsEventListener;
import com.calemi.sledgehammers.event.listener.RenderGuiOverlayEventListener;
import com.calemi.sledgehammers.item.ItemTagLists;
import com.calemi.sledgehammers.register.EnchantmentRegistry;
import com.calemi.sledgehammers.register.ItemRegistry;
import com.calemi.sledgehammers.register.LootModifierRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

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

        registerSledgehammerProperty(ItemRegistry.WOOD_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.STONE_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.IRON_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.GOLD_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.DIAMOND_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.NETHERITE_SLEDGEHAMMER.get());
        registerSledgehammerProperty(ItemRegistry.STARLIGHT_SLEDGEHAMMER.get());
    }

    private void registerSledgehammerProperty(Item item) {
        ItemProperties.register(item, new ResourceLocation(SledgehammersRef.MOD_ID, "trim_material"),
                (stack, level, player, damage) -> getTrimMaterialIndex(level, stack));

        ItemProperties.register(item, new ResourceLocation(SledgehammersRef.MOD_ID, "trim_type"),
                (stack, level, player, damage) -> getTrimTypeIndex(level, stack));
    }

    private float getTrimMaterialIndex(Level level, ItemStack stack) {

        if (level == null) {
            return 0;
        }

        Optional<ArmorTrim> optionalTrim = ArmorTrim.getTrim(level.registryAccess(), stack);
        return optionalTrim.map(armorTrim -> armorTrim.material().get().itemModelIndex()).orElse(0F);

    }

    private int getTrimTypeIndex(Level level, ItemStack stack) {

        if (level == null) {
            return 0;
        }

        Optional<ArmorTrim> optionalTrim = ArmorTrim.getTrim(level.registryAccess(), stack);

        if (optionalTrim.isPresent()) {

            Item templateItem = optionalTrim.get().pattern().get().templateItem().get();

            if (templateItem.equals(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 1;
            }

            if (templateItem.equals(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 2;
            }

            if (templateItem.equals(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 3;
            }

            if (templateItem.equals(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 4;
            }

            if (templateItem.equals(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 5;
            }

            if (templateItem.equals(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 6;
            }

            if (templateItem.equals(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 7;
            }

            if (templateItem.equals(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 8;
            }

            if (templateItem.equals(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 9;
            }

            if (templateItem.equals(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 10;
            }

            if (templateItem.equals(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 11;
            }

            if (templateItem.equals(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 12;
            }

            if (templateItem.equals(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 13;
            }

            if (templateItem.equals(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 14;
            }

            if (templateItem.equals(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 15;
            }

            if (templateItem.equals(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)) {
                return 16;
            }
        }

        return 0;
    }
}
