package com.calemi.ccore.api.item;

import com.calemi.ccore.api.location.Location;
import com.calemi.ccore.api.math.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSpawnProfile {

    private Level level;
    private double x = 0;
    private double y = 0;
    private double z = 0;

    private ItemStack stack = ItemStack.EMPTY;
    private int amount = 0;

    public ItemSpawnProfile() {}

    public ItemEntity spawn() {
        ItemStack stackCopy = stack.copy();
        stackCopy.setCount(amount);

        ItemEntity item = new ItemEntity(level, x, y, z, stackCopy);
        item.setNoPickUpDelay();
        item.setDeltaMovement(-0.05F + MathHelper.random.nextFloat() * 0.1F, -0.05F + MathHelper.random.nextFloat() * 0.1F, -0.05F + MathHelper.random.nextFloat() * 0.1F);
        level.addFreshEntity(item);
        return item;
    }

    public ItemSpawnProfile setDestination(Level level, float x, float y, float z) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public ItemSpawnProfile setDestination(Entity entity) {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.level = entity.level();
        return this;
    }

    public ItemSpawnProfile setDestination(Location location) {
        this.x = location.getX() + 0.5F;
        this.y = location.getY() + 0.5F;
        this.z = location.getZ() + 0.5F;
        this.level = location.getLevel();
        return this;
    }

    public ItemSpawnProfile setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemSpawnProfile setItem(Item item) {
        this.stack = new ItemStack(item);
        this.amount = 1;
        return this;
    }

    public ItemSpawnProfile setStack(ItemStack stack) {
        this.stack = stack;
        this.amount = stack.getCount();
        return this;
    }
}