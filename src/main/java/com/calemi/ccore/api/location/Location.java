package com.calemi.ccore.api.location;

import com.calemi.ccore.api.sound.SoundHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.List;

/**
 * Generic object used to store a location in a Level.
 * Contains helpful Block methods.
 */
public class Location {

    private Level level;
    private BlockPos blockPos;

    /**
     * @param level The Level of the Location.
     * @param x     The x position of the Location.
     * @param y     The y position of the Location.
     * @param z     The z position of the Location.
     */
    public Location(Level level, int x, int y, int z) {
        this.level = level;
        this.blockPos = new BlockPos(x, y, z);
    }

    /**
     * @param level    The Level of the Location.
     * @param blockPos The Block Position of the Location.
     */
    public Location(Level level, BlockPos blockPos) {
        this.level = level;
        this.blockPos = blockPos;
    }

    /**
     * Creates a Location from a Block Entity.
     * @param blockEntity The Block Entity to get the Location from.
     */
    public Location(BlockEntity blockEntity) {
        this(blockEntity.getLevel(), blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ());
    }

    /**
     * Creates a Location from an Entity.
     * @param entity The Entity to get the Location from.
     */
    public Location(Entity entity) {
        this(entity.level(), entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
    }

    /**
     * @return A new Location with the same values.
     */
    public Location copy() {
        return new Location(this.level, this.blockPos);
    }

    /**
     * @return The Level this Location is in.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the Level this Location is in.
     * @param level The new Level.
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * @return The Block Position of this Location.
     */
    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    /**
     * Sets the Block Position this Location is in.
     * @param blockPos The new Block Position.
     */
    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    /**
     * @return The x position of this Location.
     */
    public int getX() {
        return blockPos.getX();
    }

    /**
     * @return The y position of this Location.
     */
    public int getY() {
        return blockPos.getY();
    }

    /**
     * @return The z position of this Location.
     */
    public int getZ() {
        return blockPos.getZ();
    }

    /**
     * @return A centered Vector at this Location.
     */
    public Vec3 getVector() {
        return new Vec3(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D);
    }

    /**
     * Offsets this Location by coordinates.
     * @param x The x amount to move.
     * @param y The y amount to move.
     * @param z The z amount to move.
     */
    public void offset(int x, int y, int z) {
        setBlockPos(blockPos.offset(x, y, z));
    }


    /**
     * Offsets this Location in a direction.
     * @param dir      The direction to move.
     * @param distance The distance to move.
     */
    public void relative(Direction dir, int distance) {
        setBlockPos(blockPos.relative(dir, distance));
    }

    /**
     * Offsets this Location in a direction.
     * @param dir The direction to move.
     */
    public void relative(Direction dir) {
        setBlockPos(blockPos.relative(dir));
    }

    /**
     * @return The Block at this Location.
     */
    public Block getBlock() {
        return getBlockState().getBlock();
    }

    /**
     * @return The current light value at this Location.
     */
    public int getLightValue() {
        return level.getLightEmission(getBlockPos());
    }

    /**
     * @return The BlockState at this Location.
     */
    public BlockState getBlockState() {
        return level.getBlockState(blockPos);
    }

    /**
     * @return The BlockEntity at this Location.
     */
    public BlockEntity getBlockEntity() {
        return level.getBlockEntity(getBlockPos());
    }

    /**
     * @param location Reference Location.
     * @return The distance (in Blocks) between this Location and another.
     */
    public double getDistance(Location location) {

        int dx = getX() - location.getX();
        int dy = getY() - location.getY();
        int dz = getZ() - location.getZ();

        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /**
     * Sets the Block at this location.
     * @param state The new BlockState.
     */
    public void setBlockWithUpdate(BlockState state) {
        level.setBlock(getBlockPos(), state, 3);
    }

    /**
     * Sets the Block at this location.
     * @param state  The new BlockState.
     * @param placer The Player who created this change.
     */
    public void setBlockWithUpdate(BlockState state, Player placer) {
        setBlockWithUpdate(state);
        state.getBlock().setPlacedBy(level, getBlockPos(), state, placer, new ItemStack(state.getBlock()));
    }

    /**
     * Sets the Block at this location.
     * @param block The new Block.
     */
    public void setBlockWithUpdate(Block block) {
        setBlockWithUpdate(block.defaultBlockState());
    }

    /**
     * Sets the Block at this location.
     * @param block   The new Block.
     * @param context The context.
     */
    public void setBlockWithUpdate(Block block, BlockPlaceContext context) {
        setBlockWithUpdate(block.getStateForPlacement(context));
    }

    /**
     * Sets the Block at this location to Air.
     */
    public void setBlockToAir() {
        setBlockWithUpdate(Blocks.AIR);
    }

    /**
     * Breaks the Block at this location.
     * @param breaker The Player who broke the block.
     */
    public void breakBlock(Player breaker) {

        if (isAirBlock()) {
            return;
        }

        SoundHelper.playBlockBreak(this, getBlockState());

        if (breaker instanceof ServerPlayer) {

            ((ServerPlayer) breaker).gameMode.destroyBlock(blockPos);
            getLevel().addDestroyBlockEffect(getBlockPos(), getBlockState());
        }
    }

    /**
     * @param breaker the player trying to harvest the block at this Location;
     * @return true, if the player can harvest the block this Location.
     */
    public boolean canHarvestBlock(Player breaker) {
        return getBlockState().canHarvestBlock(getLevel(), getBlockPos(), breaker);
    }

    /**
     * @param breaker    The Player who is breaking the Block.
     * @param heldStack The Item Stack being held by the Player.
     * @return The Block's drops at this Location.
     */
    public List<ItemStack> getBlockDropsFromBreaking(Player breaker, ItemStack heldStack) {
        return Block.getDrops(getBlockState(), (ServerLevel) level, getBlockPos(), getBlockEntity(), breaker, heldStack);
    }


    /**
     * @param breaker The player to break the block at this Location.
     * @param heldStack The player's held Item stack.
     * @return the amount of experience points a player would receive from breaking the block at this Location.
     */
    public int getBlockExperienceFromBreaking(Player breaker, ItemStack heldStack) {
        return getBlockState().getExpDrop(getLevel(), getBlockPos(), getBlockEntity(), breaker, heldStack);
    }

    /**
     * Call only on the server.
     * Spawns experience points as orbs at this Location.
     * @param amount the amount of experience points to spawn.
     */
    public void spawnExperience(int amount) {
        getBlock().popExperience((ServerLevel) getLevel(), getBlockPos(), amount);
    }

    /**
     * @return true if this Location is at 0, 0, 0.
     */
    public boolean isZero() {
        return getX() == 0 && getY() == 0 && getZ() == 0;
    }

    /**
     * @return true if the Block at this Location is Air.
     */
    public boolean isAirBlock() {
        return getBlock() == Blocks.AIR;
    }

    /**
     * @return true if a Block could be placed at this Location.
     */
    public boolean isBlockValidForPlacing() {
        return getBlockState().canBeReplaced() || isAirBlock();
    }

    /**
     * @return true if the Block at this Location is a full cube.
     */
    public boolean isFullCube() {
        return getBlockState().isCollisionShapeFullBlock(level, getBlockPos());
    }

    /**
     * @param entity The Entity to check for.
     * @return true if the Entity exists at this Location
     */
    public boolean isEntityAtLocation(Entity entity) {

        int entityX = entity.getBlockX();
        int entityY = entity.getBlockY();
        int entityZ = entity.getBlockZ();

        return entityX == getX() && entityZ == getZ() && (entityY == getY() || entityY + 1 == getY());
    }

    /**
     * @return true if the Block at this Location has collision.
     */
    public boolean doesBlockHaveCollision() {
        return getBlockState().getCollisionShape(level, getBlockPos(), CollisionContext.empty()) != Shapes.empty();
    }

    /**
     * @param level The Level to construct the Location.
     * @param tag   The tag that stored the Location.
     * @return A location constructed from an NBT Tag.
     */
    public static Location readFromNBT(Level level, CompoundTag tag) {

        //Checks if the tag is missing a crucial value. If so, doesn't read the Location.
        if (!tag.contains("locX") || !tag.contains("locY") || !tag.contains("locZ")) {
            return null;
        }

        int x = tag.getInt("locX");
        int y = tag.getInt("locY");
        int z = tag.getInt("locZ");

        return new Location(level, x, y, z);
    }

    /**
     * Stores the Location in an NBT Tag.
     * @param tag The tag to store the Location.
     */
    public void writeToNBT(CompoundTag tag) {
        tag.putInt("locX", getX());
        tag.putInt("locY", getY());
        tag.putInt("locZ", getZ());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Location newLoc) {
            return level.equals(newLoc.level) && getBlockPos().equals(newLoc.getBlockPos());
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "[" + getX() + ", " + getY() + ", " + getZ() + "]";
    }
}