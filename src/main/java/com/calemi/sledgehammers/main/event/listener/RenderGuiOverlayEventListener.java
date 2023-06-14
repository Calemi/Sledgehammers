package com.calemi.sledgehammers.main.event.listener;

import com.calemi.ccore.api.general.helper.MathHelper;
import com.calemi.ccore.api.general.helper.ScreenHelper;
import com.calemi.ccore.api.screen.ScreenRect;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.main.item.SledgehammerItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderGuiOverlayEventListener {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderGameOverlayEvent(RenderGuiOverlayEvent.Post event) {

        //Checks if the current render is on the "TEXT" layer.
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {

            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            LocalPlayer player = mc.player;

            if (player == null || level == null) {
                return;
            }

            ItemStack activeItemStack = player.getUseItem();

            int scaledWidth = mc.getWindow().getGuiScaledWidth();
            int scaledHeight = mc.getWindow().getGuiScaledHeight();
            int midX = scaledWidth / 2;
            int midY = scaledHeight / 2;

            //Checks if the active Item is a Sledgehammer. Active Items are the ones being used; like charging a bow.
            if (activeItemStack.getItem() instanceof SledgehammerItem sledgehammer) {

                int chargeTime = sledgehammer.chargeTime;
                int hammerIconWidth = 13;
                int scaledChargeTime = MathHelper.scaleInt(player.getTicksUsingItem(), chargeTime, hammerIconWidth);

                RenderSystem.setShaderTexture(0, new ResourceLocation(SledgehammersRef.MOD_ID + ":textures/gui/hammer_overlay.png"));

                //If the Sledgehammer is charging, render the loading charge overlay.
                if (player.getTicksUsingItem() < chargeTime) {
                    ScreenHelper.drawRect(0, 0, new ScreenRect(midX - 7, midY - 11, hammerIconWidth, 4), 0);
                    ScreenHelper.drawRect(hammerIconWidth, 0, new ScreenRect(midX - 7, midY - 11, scaledChargeTime, 4), 0);
                }

                //If the Sledgehammer is ready, render the flashing ready overlay.
                else {

                    if (level.getGameTime() % 5 > 1) {
                        ScreenHelper.drawRect(hammerIconWidth * 2, 0, new ScreenRect(midX - 7, midY - 11, hammerIconWidth, 4), 0);
                    }
                }
            }
        }
    }
}
