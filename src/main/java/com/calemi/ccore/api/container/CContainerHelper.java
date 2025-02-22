package com.calemi.ccore.api.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Use this class for to help with Containers.
 */
public class CContainerHelper {

    /**
     * @param container  The Container to count in.
     * @param item      The type of Item to count.
     * @return The amount of Items in an Inventory.
     */
    public static int countItems(Container container, Item item) {
        return countItems(container, new ItemStack(item), false);
    }

    /**
     * @param container  The Container to count in.
     * @param stack      The type of Item Stack to count.
     * @return The amount of a specified Item Stack in an Inventory. Will compare exact Item Stacks.
     */
    public static int countItems(Container container, ItemStack stack) {
        return countItems(container, stack, true);
    }

    /**
     * @param container  The Container to count in.
     * @param stack      The type of Item Stack to count.
     * @param compareNBT Should it compare the same NBT items when counting?
     * @return The amount of a specified Item Stack in an Inventory.
     */
    private static int countItems(Container container, ItemStack stack, boolean compareNBT) {

        boolean isInventory = container instanceof Inventory;

        int count = 0;

        for (int slotIndex = 0; slotIndex < container.getContainerSize(); slotIndex++) {

            //IGNORE ARMOR SLOTS
            if (isInventory && (slotIndex == 36 || slotIndex == 37 || slotIndex == 38 || slotIndex == 39)) {
                continue;
            }

            ItemStack stackInSlot = container.getItem(slotIndex);

            if (!stackInSlot.is(stack.getItem())) {
                continue;
            }

            if (compareNBT) {

                if (!ItemStack.isSameItemSameComponents(stackInSlot, stack)) {
                    continue;
                }
            }

            count += stackInSlot.getCount();
        }

        return count;
    }

    /**
     * @param container  The Container to count in.
     * @param item       The Item to test.
     * @return The count of Items that can fit in the Container.
     */
    public static int calculateFittingSpace(Container container, Item item) {
        return calculateFittingSpace(container, new ItemStack(item), false);
    }

    /**
     * @param container  The Container to count in.
     * @param stack      The Item Stack to test.
     * @return The count of Items that can fit in the Container. Will compare exact Item Stacks.
     */
    public static int calculateFittingSpace(Container container, ItemStack stack) {
        return calculateFittingSpace(container, stack, true);
    }

    /**
     * @param container  The Container to count in.
     * @param stack      The Item Stack to test.
     * @param compareNBT Should it only use the same NBT items?
     * @return The count of Items that can fit in the Container.
     */
    private static int calculateFittingSpace(Container container, ItemStack stack, boolean compareNBT) {

        boolean isInventory = container instanceof Inventory;

        int fittingSpace = 0;

        for (int slotIndex = 0; slotIndex < container.getContainerSize(); slotIndex++) {

            //IGNORE ARMOR SLOTS
            if (isInventory && (slotIndex == 36 || slotIndex == 37 || slotIndex == 38 || slotIndex == 39)) {
                continue;
            }

            ItemStack stackInSlot = container.getItem(slotIndex);

            if (stackInSlot.isEmpty()) {
                fittingSpace += stack.getMaxStackSize();
            }

            if (!stackInSlot.is(stack.getItem())) {
                continue;
            }

            if (compareNBT) {

                if (!ItemStack.isSameItemSameComponents(stackInSlot, stack)) {
                    continue;
                }
            }

            fittingSpace += stack.getMaxStackSize() - stackInSlot.getCount();
        }

        return fittingSpace;
    }

    /**
     * Removes a specified amount of Items from an Inventory.
     * @param container  The Container to remove from.
     * @param item       The type of Item to remove.
     * @param amount     The amount to remove.
     */
    public static void consumeItems(Container container, Item item, int amount) {
        consumeItems(container, new ItemStack(item), amount, false);
    }

    /**
     * Removes an Item Stack from an Inventory.
     * @param container  The Container to remove from.
     * @param stack      The Item Stack to remove.
     */
    public static void consumeItems(Container container, ItemStack stack) {
        consumeItems(container, stack, stack.getCount(), true);
    }

    /**
     * Removes a specified amount of Items from an Inventory.
     * @param container  The Container to remove from.
     * @param stack      The Item Stack to remove.
     * @param amount     The amount to remove.
     */
    public static void consumeItems(Container container, ItemStack stack, int amount) {
        consumeItems(container, stack, amount, true);
    }

    /**
     * Removes a specified amount of Items from an Inventory.
     * @param container  The Container to remove from.
     * @param stack      The type of Item Stack to remove.
     * @param amount     The amount to remove.
     * @param compareNBT Should it compare the same NBT items when consuming?
     */
    private static void consumeItems(Container container, ItemStack stack, int amount, boolean compareNBT) {

        boolean isInventory = container instanceof Inventory;

        int amountLeft = amount;

        if (countItems(container, stack, compareNBT) < amount) {
            return;
        }

        for (int slotIndex = 0; slotIndex < container.getContainerSize(); slotIndex++) {

            //IGNORE ARMOR SLOTS
            if (isInventory && (slotIndex == 36 || slotIndex == 37 || slotIndex == 38 || slotIndex == 39)) {
                continue;
            }

            if (amountLeft <= 0) break;

            ItemStack stackInSlot = container.getItem(slotIndex);

            if (!stackInSlot.is(stack.getItem())) {
                continue;
            }

            if (compareNBT) {

                if (!ItemStack.isSameItemSameComponents(stackInSlot, stack)) {
                    continue;
                }
            }

            if (amountLeft >= stackInSlot.getCount()) {
                amountLeft -= stackInSlot.getCount();
                container.setItem(slotIndex, ItemStack.EMPTY);
            }

            else {
                stackInSlot.shrink(amountLeft);
                return;
            }
        }
    }

    /**
     * @param container The Container to test.
     * @param item     The Item to test.
     * @param amount    The amount to check for.
     * @return true, if the given Item can be inserted in the Container.
     */
    public static boolean canInsertItem(Container container, Item item, int amount) {
        return calculateFittingSpace(container, item) >= amount;
    }

    /**
     * @param container The Container to test.
     * @param stack     The Item Stack to test.
     * @return true, if the given Item Stack can be inserted in the Container.
     */
    public static boolean canInsertItem(Container container, ItemStack stack) {
        return canInsertItem(container, stack, stack.getCount());
    }

    /**
     * @param container The Container to test.
     * @param stack     The Item Stack to test.
     * @param amount    The amount to check for.
     * @return true, if the given Item Stack can be inserted in the Container.
     */
    public static boolean canInsertItem(Container container, ItemStack stack, int amount) {
        return calculateFittingSpace(container, stack) >= amount;
    }

    /**
     * Inserts the given ItemStack into the Container.
     * @param container The Container to insert in.
     * @param item     The Item to insert.
     * @param amount    The amount to insert.
     */
    public static void insertItem(Container container, Item item, int amount) {
        insertItem(container, new ItemStack(item), amount);
    }

    /**
     * Inserts the given ItemStack into the Container.
     * @param container The Container to insert in.
     * @param stack     The ItemStack to insert.
     */
    public static void insertItem(Container container, ItemStack stack) {
        insertItem(container, stack, stack.getCount());
    }

    /**
     * Inserts the given ItemStack into the Container.
     * @param container The Container to insert in.
     * @param stack     The ItemStack to insert.
     * @param amount    The amount to insert.
     */
    public static void insertItem(Container container, ItemStack stack, int amount) {

        boolean isInventory = container instanceof Inventory;

        if (!canInsertItem(container, stack, amount)) {
            return;
        }

        int amountLeft = amount;
        int maxStackSize = stack.getMaxStackSize();

        for (int slotIndex = 0; slotIndex < container.getContainerSize(); slotIndex++) {

            //IGNORE ARMOR SLOTS
            if (isInventory && (slotIndex == 36 || slotIndex == 37 || slotIndex == 38 || slotIndex == 39)) {
                continue;
            }

            if (amountLeft <= 0) break;

            ItemStack stackInSlot = container.getItem(slotIndex);

            if (stackInSlot.isEmpty()) {

                int cappedAmount = Math.min(maxStackSize, amountLeft);

                amountLeft -= cappedAmount;

                ItemStack stackCopy = stack.copy();
                stackCopy.setCount(cappedAmount);
                container.setItem(slotIndex, stackCopy);

                continue;
            }

            if (!ItemStack.isSameItemSameComponents(stackInSlot, stack)) {
                continue;
            }

            int spaceToMax = maxStackSize - stackInSlot.getCount();

            if (spaceToMax >= amountLeft) {

                stackInSlot.setCount(stackInSlot.getCount() + amountLeft);
                break;
            }

            amountLeft -= spaceToMax;
            stackInSlot.setCount(maxStackSize);
        }
    }
}