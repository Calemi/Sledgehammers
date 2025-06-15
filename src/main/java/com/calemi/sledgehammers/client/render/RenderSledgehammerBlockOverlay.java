package com.calemi.sledgehammers.client.render;

import com.calemi.ccore.api.block.scanner.VeinBlockScanner;
import com.calemi.ccore.api.location.BlockLocation;
import com.calemi.ccore.api.raytrace.RayTraceHelper;
import com.calemi.ccore.api.shape.ShapeFlatCube;
import com.calemi.sledgehammers.block.scanner.OreVeinBlockScanner;
import com.calemi.sledgehammers.config.SledgeConfig;
import com.calemi.sledgehammers.item.SledgehammerItem;
import com.calemi.sledgehammers.main.Sledgehammers;
import com.calemi.sledgehammers.main.SledgeRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.Tags;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RenderSledgehammerBlockOverlay {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderLevelStageEvent(RenderLevelStageEvent event) {

        if (!SledgeConfig.client.chargeBlockOutlines.get()) return;

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {

            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player == null) return;

            Level level = player.level();
            ItemStack usingStack = player.getUseItem();

            if (!(usingStack.getItem() instanceof SledgehammerItem sledgehammerItem)) return;

            RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.rayTraceBlock(level, player, player.blockInteractionRange());

            if (blockTrace == null) return;

            BlockLocation hitLocation = blockTrace.getHit();

            List<BlockPos> outlinePositions = new ArrayList<>();

            if (hitLocation.getBlockState().is(BlockTags.LOGS) && SledgeConfig.server.fellTreeAbility.get()) {

                VeinBlockScanner scan = new VeinBlockScanner(hitLocation, SledgeConfig.server.maxBlockBreakSize.get());
                scan.start();

                outlinePositions.addAll(scan.getCollectedPositions());
            }

            else if (hitLocation.getBlockState().is(Tags.Blocks.ORES) && SledgeConfig.server.veinMineAbility.get()) {

                VeinBlockScanner scan = new OreVeinBlockScanner(hitLocation, SledgeConfig.server.maxBlockBreakSize.get());
                scan.start();

                Sledgehammers.LOGGER.debug(hitLocation);

                outlinePositions.addAll(scan.getCollectedPositions());
            }

            else if (SledgeConfig.server.excavateAbility.get()) {

                Holder<Enchantment> efficiencyHolder = level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT).wrapAsHolder(Objects.requireNonNull(level.registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT).get(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "crushing"))));

                int radius = EnchantmentHelper.getEnchantmentLevel(efficiencyHolder, player) + 1;

                outlinePositions.addAll(new ShapeFlatCube(blockTrace.getHitSide(), radius).getWorldPositions(hitLocation));
            }

            PoseStack poseStack = event.getPoseStack();

            Camera activeRenderInfo = mc.getEntityRenderDispatcher().camera;
            Vec3 projectedView = activeRenderInfo.getPosition();

            float chargeScale = (float)Math.clamp(player.getTicksUsingItem(), 0, sledgehammerItem.chargeTime) / sledgehammerItem.chargeTime;

            poseStack.pushPose();
            poseStack.translate(hitLocation.getX() - projectedView.x, hitLocation.getY() - projectedView.y, hitLocation.getZ() - projectedView.z);

            VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(SledgeRenderTypes.LINES);


            renderBoxOutlines(outlinePositions, hitLocation, chargeScale, 1F, SledgeRenderTypes.LINES, mc, poseStack);
            renderBoxOutlines(outlinePositions, hitLocation, chargeScale, 0.25F, SledgeRenderTypes.LINES_TRANSPARENT, mc, poseStack);

            poseStack.popPose();
        }
    }

    private void renderBoxOutlines(List<BlockPos> outlinePositions, BlockLocation origin, float chargeScale, float alpha, RenderType lineRenderType, Minecraft mc, PoseStack poseStack) {

        VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(lineRenderType);
        Matrix4f matrix = poseStack.last().pose();

        for (BlockPos outlinePosition : outlinePositions) {

            if (origin.getLevel().getBlockState(outlinePosition).isAir()) continue;

            float x = outlinePosition.getX() - origin.getX();
            float y = outlinePosition.getY() - origin.getY();
            float z = outlinePosition.getZ() - origin.getZ();

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
