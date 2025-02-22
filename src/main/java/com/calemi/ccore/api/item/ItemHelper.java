package com.calemi.ccore.api.item;

import com.calemi.ccore.api.container.CContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Use this class to help with Items.
 */
public class ItemHelper {

    /**
     * Gives a player an Item Stack. First it adds it to their inventory, then spawns any remaining stacks at the player.
     * @param player The Player to give the item to.
     * @param stack  The Item Stack to give.
     * @param amount The amount of the Item Stack to give.
     */
    public static void giveItem(Player player, ItemStack stack, int amount) {

        int fittingSpace = CContainerHelper.calculateFittingSpace(player.getInventory(), stack);

        if (fittingSpace < amount) {
            CContainerHelper.insertItem(player.getInventory(), stack, fittingSpace);

            amount -= fittingSpace;

            ItemSpawnProfile spawnProfile = new ItemSpawnProfile().setStack(stack).setAmount(amount).setDestination(player);
            spawnProfile.spawn();

            return;
        }

        CContainerHelper.insertItem(player.getInventory(), stack, amount);
    }

    /**
     * Gives a player an Item Stack. First it adds it to their inventory, then spawns any remaining stacks at the player.
     * @param player The Player to give the item to.
     * @param stack  The Item Stack to spawn.
     */
    public static void giveItem(Player player, ItemStack stack) {
        giveItem(player, stack, stack.getCount());
    }
}