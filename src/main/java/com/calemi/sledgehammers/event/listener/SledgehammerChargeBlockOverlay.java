package com.calemi.sledgehammers.event.listener;

import com.calemi.ccore.api.location.Location;
import com.calemi.ccore.api.raytrace.RayTraceHelper;
import com.calemi.ccore.api.scanner.VeinBlockScanner;
import com.calemi.ccore.api.worldedit.WorldEditHelper;
import com.calemi.ccore.api.worldedit.shape.ShapeFlatCube;
import com.calemi.sledgehammers.client.SledgehammerRenderTypes;
import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.item.ItemTagLists;
import com.calemi.sledgehammers.item.SledgehammerItem;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SledgehammerChargeBlockOverlay {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderLevelStageEvent(RenderLevelStageEvent event) {

        if (!SledgehammersConfig.client.chargeBlockOutlines.get()) return;

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {

            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player == null) return;

            Level level = player.level();
            ItemStack usingStack = player.getUseItem();

            if (!(usingStack.getItem() instanceof SledgehammerItem sledgehammerItem)) return;

            RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.rayTraceBlock(level, player, player.blockInteractionRange());

            if (blockTrace == null) return;

            Location hitLocatiom = blockTrace.getHit();

            List<Location> outlineLocations = new ArrayList<>();

            if (ItemTagLists.isLog(hitLocatiom.getBlock()) && SledgehammersConfig.server.fellTreeAbility.get()) {

                VeinBlockScanner scan = new VeinBlockScanner(hitLocatiom, SledgehammersConfig.server.maxBlockBreakSize.get());
                scan.start();

                outlineLocations.addAll(scan.collectedLocations);
            }

            else if (ItemTagLists.isOre(hitLocatiom.getBlock()) && SledgehammersConfig.server.veinMineAbility.get()) {

                VeinBlockScanner scan = new VeinBlockScanner(hitLocatiom, SledgehammersConfig.server.maxBlockBreakSize.get());
                scan.start();

                outlineLocations.addAll(scan.collectedLocations);
            }

            else if (SledgehammersConfig.server.excavateAbility.get()) {

                Holder<Enchantment> efficiencyHolder = level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT).wrapAsHolder(Objects.requireNonNull(level.registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT).get(ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "crushing"))));

                int radius = EnchantmentHelper.getEnchantmentLevel(efficiencyHolder, player) + 1;

                outlineLocations.addAll(WorldEditHelper.selectShape(new ShapeFlatCube(hitLocatiom, blockTrace.getHitSide(), radius)));
            }

            PoseStack poseStack = event.getPoseStack();

            Camera activeRenderInfo = mc.getEntityRenderDispatcher().camera;
            Vec3 projectedView = activeRenderInfo.getPosition();

            float chargeScale = (float)Math.clamp(player.getTicksUsingItem(), 0, sledgehammerItem.chargeTime) / sledgehammerItem.chargeTime;

            poseStack.pushPose();
            poseStack.translate(hitLocatiom.getX() - projectedView.x, hitLocatiom.getY() - projectedView.y, hitLocatiom.getZ() - projectedView.z);

            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(SledgehammerRenderTypes.LINES);


            renderBoxOutlines(outlineLocations, hitLocatiom, chargeScale, 1F, SledgehammerRenderTypes.LINES, mc, poseStack);
            renderBoxOutlines(outlineLocations, hitLocatiom, chargeScale, 0.25F, SledgehammerRenderTypes.LINES_TRANSPARENT, mc, poseStack);

            poseStack.popPose();
        }
    }

    private void renderBoxOutlines(List<Location> outlineLocations, Location origin, float chargeScale, float alpha, RenderType lineRenderType, Minecraft mc, PoseStack poseStack) {

        VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(lineRenderType);
        Matrix4f matrix = poseStack.last().pose();

        for (Location outlineLocation : outlineLocations) {

            if (outlineLocation.isAirBlock()) continue;

            float x = outlineLocation.getX() - origin.getX();
            float y = outlineLocation.getY() - origin.getY();
            float z = outlineLocation.getZ() - origin.getZ();

            float growOffset = (1 - chargeScale) * 0.5F;
            float growScale = 0.5F;

            float offset = 0.001F;

            renderBox(x + growOffset - offset, y + growOffset - offset, z + growOffset - offset,
                    x - growOffset + 1 + offset, y - growOffset + 1 + offset, z - growOffset + 1 + offset, chargeScale * alpha, buffer, matrix);
        }

        mc.renderBuffers().bufferSource().endBatch(lineRenderType);
    }

    private void renderBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float alpha, VertexConsumer buffer, Matrix4f matrix) {

        buffer.addVertex(matrix, minX, minY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, minY, minZ).setColor(1, 1, 1, alpha);;

        buffer.addVertex(matrix, minX, minY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, maxY, minZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, minX, minY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, minY, maxZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, minY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, minY, minZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, minY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, minY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, minY, maxZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, minY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, maxY, minZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, minX, minY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, maxY, maxZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, minX, maxY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, maxY, minZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, minX, maxY, minZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, maxY, maxZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, maxX, maxY, minZ).setColor(1, 1, 1, alpha);

        buffer.addVertex(matrix, maxX, maxY, maxZ).setColor(1, 1, 1, alpha);
        buffer.addVertex(matrix, minX, maxY, maxZ).setColor(1, 1, 1, alpha);
    }
}
