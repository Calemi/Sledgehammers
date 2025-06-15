package com.calemi.sledgehammers.item;

import com.calemi.sledgehammers.main.SledgeRef;
import com.calemi.sledgehammers.tag.SledgeTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class SledgeEnchantments {

    public static final ResourceKey<Enchantment> CRUSHING = SledgeRef.createKey("crushing", Registries.ENCHANTMENT);

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        register(context, CRUSHING, new Enchantment.Builder(Enchantment.definition(
                items.getOrThrow(SledgeTags.Items.SLEDGEHAMMERS),
                3,
                3,
                Enchantment.dynamicCost(15, 5),
                Enchantment.dynamicCost(65, 9),
                3,
                EquipmentSlotGroup.MAINHAND)
        ));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
