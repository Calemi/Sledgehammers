package com.calemi.sledgehammers.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemTagLists {

    public static final List<Block> logTags = new ArrayList<>();
    public static final List<Block> oreTags = new ArrayList<>();

    public static boolean isLog(Block block) {
        return Objects.requireNonNull(logTags).contains(block);
    }

    public static boolean isOre(Block block) {
        return Objects.requireNonNull(oreTags).contains(block);
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {

        logTags.clear();
        oreTags.clear();

        addItemsFromTag(logTags, BlockTags.LOGS);
        addItemsFromTag(oreTags, Tags.Blocks.ORES);
    }

    private static void addItemsFromTag(List<Block> list, TagKey<Block> tag) {

        for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
            list.add(holder.value());
        }
    }
}