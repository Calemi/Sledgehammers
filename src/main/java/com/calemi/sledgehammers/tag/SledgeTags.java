package com.calemi.sledgehammers.tag;

import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SledgeTags {

    public static class Items {

        public static final TagKey<Item> BREAKABLE_SLEDGEHAMMERS = TagKey.create(Registries.ITEM, SledgeRef.rl("breakable_sledgehammers"));
        public static final TagKey<Item> SLEDGEHAMMERS = TagKey.create(Registries.ITEM, SledgeRef.rl("sledgehammers"));
    }
}
