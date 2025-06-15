package com.calemi.sledgehammers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KnobItem extends Item {

    public KnobItem() {
        super(new Properties());
    }

    public KnobItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return this == SledgeItems.STARLIGHT_KNOB.get() || super.isFoil(stack);
    }
}