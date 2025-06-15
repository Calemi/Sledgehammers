package com.calemi.sledgehammers.datagen;

import com.calemi.ccore.api.list.ListHelper;
import com.calemi.sledgehammers.item.SledgeItems;
import com.calemi.sledgehammers.main.SledgeRef;
import com.calemi.sledgehammers.tag.SledgeTags;
import com.calemi.sledgehammers.util.SledgeLists;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SledgeItemTagProvider extends ItemTagsProvider {

    public SledgeItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> itemProvider, CompletableFuture<TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, itemProvider, blockProvider, SledgeRef.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(SledgeTags.Items.BREAKABLE_SLEDGEHAMMERS).addAll(ListHelper.toItemResourceKeyList(SledgeLists.BREAKABLE_SLEDGEHAMMERS));
        tag(SledgeTags.Items.SLEDGEHAMMERS).addOptionalTag(SledgeTags.Items.BREAKABLE_SLEDGEHAMMERS);
        tag(SledgeTags.Items.SLEDGEHAMMERS).add(SledgeItems.STARLIGHT_SLEDGEHAMMER.asItem());

        tag(ItemTags.SWORDS).addOptionalTag(SledgeTags.Items.SLEDGEHAMMERS);
        tag(ItemTags.SHOVELS).addOptionalTag(SledgeTags.Items.SLEDGEHAMMERS);
        tag(ItemTags.PICKAXES).addOptionalTag(SledgeTags.Items.SLEDGEHAMMERS);
        tag(ItemTags.AXES).addOptionalTag(SledgeTags.Items.SLEDGEHAMMERS);
        tag(ItemTags.HOES).addTag(SledgeTags.Items.SLEDGEHAMMERS);

        tag(ItemTags.DURABILITY_ENCHANTABLE).remove(SledgeItems.STARLIGHT_SLEDGEHAMMER.asItem());

        tag(ItemTags.TRIMMABLE_ARMOR).addTag(SledgeTags.Items.SLEDGEHAMMERS);
    }
}
