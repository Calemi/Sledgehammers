package com.calemi.ccore.api.item;

import com.calemi.ccore.api.location.Location;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class ItemDropCollection {

    private final List<ItemStack> drops;

    public ItemDropCollection() {
        drops = new ArrayList<>();
    }

    public void addDrop(ItemStack stack) {

        //Check for same item and merge stacks.
        for (ItemStack drop : drops) {

            if (ItemStack.isSameItem(drop, stack)) {
                drop.setCount(drop.getCount() + stack.getCount());
                return;
            }
        }

        //New items get appended.
        drops.add(stack);
    }

    public void dropAll(Location location) {

        for (ItemStack drop : drops) {

            ItemSpawnProfile itemSpawnProfile = new ItemSpawnProfile().setStack(drop).setDestination(location);
            itemSpawnProfile.spawn();
        }
    }
}
