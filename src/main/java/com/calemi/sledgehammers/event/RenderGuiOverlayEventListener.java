package com.calemi.sledgehammers.event;

import com.calemi.ccore.api.math.MathHelper;
import com.calemi.sledgehammers.item.SledgehammerItem;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

public class RenderGuiOverlayEventListener {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderGameOverlayEvent(RenderGuiLayerEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.options.hideGui || mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        Level level = mc.level;
        LocalPlayer player = mc.player;

        if (player == null || level == null) {
            return;
        }

        ItemStack activeItemStack = player.getUseItem();

        //Checks if the active Item is a Sledgehammer. Active Items are the ones being used; like charging a bow.
        if (activeItemStack.getItem() instanceof SledgehammerItem sledgehammer) {

            int scaledWidth = mc.getWindow().getGuiScaledWidth();
            int scaledHeight = mc.getWindow().getGuiScaledHeight();
            int midX = scaledWidth / 2;
            int midY = scaledHeight / 2;

            int hammerIconWidth = 13;
            int hammerIconHeight = 4;

            int chargeTime = sledgehammer.chargeTime;
            int scaledChargeTime = MathHelper.scaleInt(player.getTicksUsingItem(), chargeTime, hammerIconWidth);

            RenderSystem.setShaderTexture(0, SledgehammersRef.HAMMER_OVERLAY);

            //If the Sledgehammer is charging, render the loading charge overlay.
            if (player.getTicksUsingItem() < chargeTime) {

                //BACKGROUND HAMMER
                event.getGuiGraphics().blit(SledgehammersRef.HAMMER_OVERLAY, midX - 7, midY - 11, 0, 0, hammerIconWidth, hammerIconHeight);

                //CHARGING HAMMER
                event.getGuiGraphics().blit(SledgehammersRef.HAMMER_OVERLAY, midX - 7, midY - 11, hammerIconWidth, 0, scaledChargeTime, hammerIconHeight);
            }

            //If the Sledgehammer is ready, render the flashing ready overlay.
            else {

                if (level.getGameTime() % 5 > 1) {
                    //FLASHING HAMMER
                    event.getGuiGraphics().blit(SledgehammersRef.HAMMER_OVERLAY, midX - 7, midY - 11, hammerIconWidth * 2, 0, hammerIconWidth, hammerIconHeight);
                }
            }
        }

    }
}