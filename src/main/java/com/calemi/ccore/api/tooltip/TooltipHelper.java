package com.calemi.ccore.api.tooltip;

import com.calemi.ccore.main.CCoreRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

/**
 * Use this class for adding lore to Items.
 */
public class TooltipHelper {

    /**
     * Adds the first information lore to an Item.
     * This method generates the [Shift] label.
     * @param tooltipList   The tooltip list of the Item.
     * @param loreComponent The translatable lore component to add.
     */
    public static void addInformationLoreFirst(List<Component> tooltipList, MutableComponent loreComponent) {

        if (!addInformationLore(tooltipList, loreComponent)) {
            tooltipList.add(getPlateText("key.shift", ChatFormatting.AQUA).append(" ").append(Component.translatable(CCoreRef.MOD_ID + ".lore.info").withStyle(ChatFormatting.GRAY)));
        }
    }

    /**
     * Adds information lore to an Item.
     * @param tooltipList   The tooltip list of the Item.
     * @param loreComponent The translatable lore component to add.
     * @return true, if the shift key is down.
     */
    public static boolean addInformationLore(List<Component> tooltipList, MutableComponent loreComponent) {

        if (Screen.hasShiftDown()) {
            tooltipList.add(loreComponent.withStyle(ChatFormatting.GRAY));
            return true;
        }

        return false;
    }

    /**
     * Adds the first control lore to an Item.
     * This method generates the [Ctrl] label.
     * @param tooltipList   The tooltip list of the Item.
     * @param loreComponent The translatable lore component to add.
     * @param controlType   The type of control.
     */
    public static void addControlsLoreFirst(List<Component> tooltipList, MutableComponent loreComponent, ControlType controlType) {

        if (!addControlsLore(tooltipList, loreComponent, controlType)) {
            tooltipList.add(getPlateText("key.ctrl", ChatFormatting.AQUA).append(" ").append(Component.translatable(CCoreRef.MOD_ID + ".lore.controls").withStyle(ChatFormatting.GRAY)));
        }
    }

    /**
     * Adds control lore to an Item.
     * @param tooltipList   The tooltip list of the Item.
     * @param loreComponent The translatable lore component to add.
     * @param controlType   The type of control.
     */
    public static boolean addControlsLore(List<Component> tooltipList, MutableComponent loreComponent, ControlType controlType) {

        if (Screen.hasControlDown()) {
            addActionLore(tooltipList, loreComponent.withStyle(ChatFormatting.GRAY), controlType);
            return true;
        }

        return false;
    }


    /**
     * Helper method to add a control lore line to an Item.
     * @param tooltipList   The tooltip list of the Item.
     * @param loreComponent The translatable lore component to add.
     * @param controlType   The type of control.
     */
    private static void addActionLore(List<Component> tooltipList, MutableComponent loreComponent, ControlType controlType) {
        tooltipList.add(getPlateText(controlType.getName(), ChatFormatting.YELLOW).append(" ").append(loreComponent));
    }

    /**
     * Adds a blank line of lore to an Item.
     * @param tooltipList The tooltip list of the Item.
     */
    public static void addBlankLine(List<Component> tooltipList) {
        tooltipList.add(Component.literal("   "));
    }

    /**
     * @param valueKey The key used to translate the string.
     * @param format   The color used in the middle.
     * @return A String with surrounding brackets and color in the middle.
     */
    public static MutableComponent getPlateText(String valueKey, ChatFormatting format) {
        return Component.literal(ChatFormatting.GRAY + "[").append(Component.translatable(CCoreRef.MOD_ID + ".lore." + valueKey).withStyle(format)).append(ChatFormatting.GRAY + "]");
    }

    public enum ControlType {

        USE("use"),
        USE_OPEN_HAND("use-open-hand"),
        RELEASE_USE("release-use"),
        SNEAK_USE("sneak-use"),
        LEFT_CLICK_BLOCK("left-click-block"),
        SNEAK_LEFT_CLICK_BLOCK("sneak-left-click-block"),
        SNEAK_BREAK_BLOCK("sneak-break-block");

        private final String key;

        ControlType(String key) {
            this.key = key;
        }

        String getName() {
            return "control_type." + key;
        }
    }
}