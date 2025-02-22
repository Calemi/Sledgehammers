package com.calemi.ccore.api.raytrace;

import com.calemi.ccore.api.location.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Use this class for ray tracing.
 */
public class RayTraceHelper {

    /**
     * @param level       The Level.
     * @param player      The Player.
     * @param maxDistance The max distance to search.
     * @return The Block the Player is looking at. Returns null if nothing is found.
     */
    public static BlockTrace rayTraceBlock(Level level, Player player, double maxDistance) {

        Vec3 playerPosVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 playerLookVec = player.getLookAngle();
        Direction playerLookDir = Direction.getNearest(playerLookVec.x, playerLookVec.y, playerLookVec.z);

        BlockHitResult rayTrace = level.clip(new ClipContext(playerPosVec, playerPosVec.add(playerLookVec.scale(maxDistance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        UseOnContext itemUseContext = new UseOnContext(player, InteractionHand.MAIN_HAND, rayTrace);
        BlockPlaceContext blockUseContext = new BlockPlaceContext(itemUseContext);

        if (rayTrace.getType() == BlockHitResult.Type.BLOCK) {

            BlockPos pos = blockUseContext.getClickedPos();
            Location locationOffset = new Location(level, pos);

            BlockPos difference = locationOffset.getBlockPos().subtract(itemUseContext.getClickedPos());
            Direction blockSide = Direction.getNearest(difference.getX(), difference.getY(), difference.getZ());

            Location locationReal = locationOffset.copy();
            locationReal.relative(blockSide.getOpposite(), 1);

            return new BlockTrace(locationReal, blockSide);
        }

        return null;
    }

    /**
     * @param level       The Level.
     * @param player      The Player.
     * @param maxDistance The max distance to search.
     * @return The Entity the Player is looking at. Returns null if nothing is found.
     */
    public static Entity rayTraceEntity(Level level, Player player, int maxDistance) {

        Entity entityHit = null;

        for (int i = 0; i < maxDistance; i++) {

            Vec3 playerLookVec = player.getLookAngle();
            Vec3 playerLookVecOffset = new Vec3(player.getX() + (playerLookVec.x * i), player.getY() + (playerLookVec.y * i), player.getZ() + (playerLookVec.z * i));

            List<Entity> entities = level.getEntities(player, new AABB(playerLookVecOffset.add(-1, -1, -1), playerLookVecOffset.add(1, 1, 1)), (entity) -> entity instanceof LivingEntity);

            if (!entities.isEmpty()) {
                entityHit = entities.getFirst();
                break;
            }
        }

        return entityHit;
    }

    public record BlockTrace(Location hit, Direction hitSide) {

        public Location getHit() {
            return hit;
        }

        public Direction getHitSide() {
            return hitSide;
        }
    }
}