package com.calemi.ccore.api.math;

import net.minecraft.client.gui.screens.Screen;

import java.util.Random;

/**
 * Use this class for complicated math functions.
 */
public class MathHelper {

    public static final Random random = new Random();

    /**
     * @param startingValue The number to start the array with.
     * @param endingValue The number to end the array with.
     * @return An array with filled with counting ints (1, 2, 3...)
     */
    public static int[] getCountingArray (int startingValue, int endingValue) {

        if (endingValue < startingValue) {
            return new int[]{};
        }

        int length = endingValue - startingValue;

        int[] array = new int[length + 1];

        for (int i = 0; i < array.length; i++) {
            array[i] = startingValue + i;
        }

        return array;
    }

    /**
     * Used to determine if a specified value can be added.
     * @param startingValue The starting value.
     * @param amountToAdd The amount to add to the starting value.
     * @param maxAmount The max amount that can fit.
     * @return amountToAdd if it can fit. Otherwise, will return 0.
     */
    public static int getAmountToAdd (int startingValue, int amountToAdd, int maxAmount) {

        if (startingValue + amountToAdd > maxAmount) {
            return 0;
        }

        return amountToAdd;
    }

    /**
     * Used to determine how much is left.
     * @param startingValue The starting value.
     * @param amountToAdd The amount to add to the starting value.
     * @param maxAmount The max amount that can fit.
     * @return the amount left to fill if it has space. Otherwise, will return 0.
     */
    public static int getAmountToFill (int startingValue, int amountToAdd, int maxAmount) {

        if (startingValue + amountToAdd > maxAmount) {
            return maxAmount - startingValue;
        }

        return 0;
    }

    /**
     * @param value The old value you want to scale.
     * @param maxValue The maximum the value can be.
     * @param desiredMaxScale The desired new maximum scale.
     * @return An int scaled from 0 - desiredMaxScale.
     */
    public static int scaleInt (int value, int maxValue, int desiredMaxScale) {
        float f = value * (float) desiredMaxScale / maxValue;
        return (int) f;
    }

    /**
     * @param chance The chance of rolling true. Range 0-1.
     * @return true, if rolled.
     */
    public static boolean rollChance(double chance) {
        return (random.nextDouble()) <= chance;
    }

    /**
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A random float value between min and max.
     */
    public static float randomRange(float min, float max) {
        return min + (float)Math.random() * (max - min);
    }

    /**
     * @param value The value to clamp.
     * @param min The minimum number the value can be.
     * @param max The maximum number the value can be.
     * @return Ensures the value stay between min and max.
     */
    public static long clamp(long value, long min, long max) {
        return value < min ? min : Math.min(value, max);
    }

    /**
     * @param defaultValue The default value.
     * @param shiftValue The new value when the shift key is down.
     * @param ctrlValue The new value when the ctrl key is down.
     * @param bothValue  The new value when the both keys are down.
     * @return An int based on what keys are down.
     */
    public static int getShiftCtrlInt (int defaultValue, int shiftValue, int ctrlValue, int bothValue) {

        int i = defaultValue;

        boolean s = Screen.hasShiftDown();
        boolean c = Screen.hasControlDown();

        if (s) i = shiftValue;
        if (c) i = ctrlValue;
        if (s && c) i = bothValue;

        return i;
    }
}