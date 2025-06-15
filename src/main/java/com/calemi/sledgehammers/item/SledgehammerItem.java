package com.calemi.sledgehammers.item;

import com.calemi.ccore.api.block.scanner.VeinBlockScanner;
import com.calemi.ccore.api.item.ItemDropCollection;
import com.calemi.ccore.api.location.BlockLocation;
import com.calemi.ccore.api.raytrace.RayTraceHelper;
import com.calemi.ccore.api.shape.ShapeFlatCube;
import com.calemi.ccore.api.sound.SoundProfile;
import com.calemi.ccore.api.tooltip.TooltipHelper;
import com.calemi.sledgehammers.block.scanner.OreVeinBlockScanner;
import com.calemi.sledgehammers.config.SledgeConfig;
import com.calemi.sledgehammers.item.tier.SledgehammerTiers;
import com.calemi.sledgehammers.main.SledgeRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Objects;

public class SledgehammerItem extends TieredItem {

    public int baseChargeTime;
    public int chargeTime;

    public SledgehammerItem(SledgehammerTiers tier, Item.Properties properties) {
        super(tier, properties.stacksTo(1).component(DataComponents.TOOL, tier.createToolProperties()).attributes(createAttributes(tier)));

        this.chargeTime = baseChargeTime;
        this.baseChargeTime = tier.getChargeTime();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        TooltipHelper.addInformationLoreFirst(tooltipComponents, Component.translatable("hover_text.sledgehammers.sledgehammer.1"));
        TooltipHelper.addInformationLore(tooltipComponents, Component.translatable("hover_text.sledgehammers.sledgehammer.2"));
        TooltipHelper.addControlsLoreFirst(tooltipComponents, Component.translatable("hover_text.sledgehammers.sledgehammer.use"), TooltipHelper.ControlType.USE);
        TooltipHelper.addControlsLore(tooltipComponents, Component.translatable("hover_text.sledgehammers.sledgehammer.sneak-use"), TooltipHelper.ControlType.SNEAK_USE);
        TooltipHelper.addControlsLore(tooltipComponents, Component.translatable("hover_text.sledgehammers.sledgehammer.release-use"), TooltipHelper.ControlType.RELEASE_USE);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);

        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (!SledgeConfig.server.chargeAbilities.get()) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (!SledgeConfig.server.excavateAbility.get() && !SledgeConfig.server.veinMineAbility.get() && !SledgeConfig.server.fellTreeAbility.get()) {
            return InteractionResultHolder.pass(itemstack);
        }

        //If the offhand has an item and the Player is not crouching, prevent charging.
        if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty() && !player.isCrouching()) {
            return InteractionResultHolder.fail(itemstack);
        }

        Holder<Enchantment> enchantmentHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(Enchantments.EFFICIENCY)
                .orElse(null);

        chargeTime = Math.max(1, baseChargeTime - (enchantmentHolder != null ? itemstack.getEnchantmentLevel(enchantmentHolder) : 0) * 3);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack heldStack, Level level, LivingEntity livingEntity, int timeLeft) {

        Player player = (Player) livingEntity;

        //If the Sledgehammer is somehow not in the main hand, stop action.
        if (!ItemStack.isSameItemSameComponents(player.getMainHandItem(), heldStack)) {
            return;
        }

        //Checks if fully charged.
        if (getUseDuration(heldStack, livingEntity) - timeLeft >= chargeTime) {

            player.swing(InteractionHand.MAIN_HAND);

            RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.rayTraceBlock(level, player, player.blockInteractionRange());

            if (blockTrace == null) {
                return;
            }

            BlockLocation hit = blockTrace.getHit();

            if (hit.getBlockState().is(BlockTags.LOGS) && SledgeConfig.server.fellTreeAbility.get()) {
                veinMine(heldStack, player, hit, false);
                return;
            }

            if (hit.getBlockState().is(Tags.Blocks.ORES) && SledgeConfig.server.veinMineAbility.get()) {
                veinMine(heldStack, player, hit, true);
                return;
            }

            if (SledgeConfig.server.excavateAbility.get()) excavateBlocks(level, heldStack, player, hit, blockTrace.getHitSide());
        }
    }

    private void veinMine(ItemStack heldStack, Player player, BlockLocation startLocation, boolean isOre) {

        //Checks if the starting Location can be mined.
        if (canBreakBlock(player, heldStack, startLocation)) {

            Level level = startLocation.getLevel();

            //Start a scan of blocks that equal the starting Location's Block.
            VeinBlockScanner scan;

            if (isOre) {
                scan = new OreVeinBlockScanner(startLocation, SledgeConfig.server.maxBlockBreakSize.get());
            }

            else {
                scan = new VeinBlockScanner(startLocation, SledgeConfig.server.maxBlockBreakSize.get());
            }

            scan.start();

            ItemDropCollection dropCollection = new ItemDropCollection();

            //Iterate through the scanned Locations.
            for (BlockPos nextPosition : scan.getCollectedPositions()) {

                BlockLocation nextLocation = new BlockLocation(level, nextPosition);
                BlockState nextState = nextLocation.getBlockState();
                Block nextBlock = nextState.getBlock();
                BlockEntity nextBlockEntity = nextLocation.getBlockEntity();

                //If the Sledgehammer is broken, stop the iteration.
                if (getDamage(heldStack) > getMaxDamage(heldStack)) {
                    break;
                }

                new SoundProfile().setEvent(nextLocation.getSoundType().getBreakSound()).setLevelAndPosition(nextLocation).play();

                if (nextLocation.equals(startLocation)) {
                    level.addDestroyBlockEffect(nextPosition, nextState);
                }

                if (!level.isClientSide()) {

                    List<ItemStack> drops = nextLocation.getBlockDropsFromBreaking(player, heldStack);

                    for (ItemStack drop : drops){
                        dropCollection.addDrop(drop);
                    }

                    int experienceAmount = nextLocation.getBlockExperienceFromBreaking(player, heldStack);

                    nextLocation.spawnExperience(experienceAmount);
                }

                if (!level.isClientSide()) nextLocation.setBlock(Blocks.AIR.defaultBlockState(), 3);
            }

            if (!player.isCreative()) {
                dropCollection.dropAll(startLocation);
            }
        }
    }

    private void excavateBlocks(Level level, ItemStack heldStack, Player player, BlockLocation startLocation, Direction face) {

        Holder<Enchantment> efficiencyHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT).wrapAsHolder(Objects.requireNonNull(level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT).get(ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "crushing"))));

        int radius = EnchantmentHelper.getEnchantmentLevel(efficiencyHolder, player) + 1;

        List<BlockPos> positions = new ShapeFlatCube(face, radius).getWorldPositions(startLocation);

        ItemDropCollection dropCollection = new ItemDropCollection();

        //Iterate through the Locations from the World Edit shape.
        for (BlockPos nextPosition : positions) {

            //If the Sledgehammer is broken, stop the iteration.
            if (getDamage(heldStack) > getMaxDamage(heldStack)) {
                break;
            }

            BlockLocation nextLocation = new BlockLocation(level, nextPosition);
            BlockState nextState = nextLocation.getBlockState();
            Block nextBlock = nextState.getBlock();
            BlockEntity nextBlockEntity = nextLocation.getBlockEntity();

            //Checks if the next Location can be mined.
            if (canBreakBlock(player, heldStack, nextLocation)) {

                if (nextLocation.equals(startLocation)) {
                    level.addDestroyBlockEffect(nextPosition, nextState);
                }

                new SoundProfile().setEvent(nextLocation.getSoundType().getBreakSound()).setLevelAndPosition(nextLocation).play();

                if (!level.isClientSide()) {

                    List<ItemStack> drops = nextLocation.getBlockDropsFromBreaking(player, heldStack);

                    for (ItemStack drop : drops){
                        dropCollection.addDrop(drop);
                    }

                    int experienceAmount = nextLocation.getBlockExperienceFromBreaking(player, heldStack);

                    nextLocation.spawnExperience(experienceAmount);
                }

                if (!level.isClientSide()) nextLocation.setBlock(Blocks.AIR.defaultBlockState());
                damageHammer(heldStack, player);
            }
        }

        if (!player.isCreative()) {
            dropCollection.dropAll(startLocation);
        }
    }

    private boolean canBreakBlock(Player player, ItemStack heldStack, BlockLocation location) {
        float hardness = location.getBlockState().getDestroySpeed(location.getLevel(), location.getBlockPos());
        return !location.getBlock().equals(Blocks.AIR) && location.getBlockState().canHarvestBlock(location.getLevel(), location.getBlockPos(), player) && hardness >= 0 && hardness <= 50 && heldStack.isCorrectToolForDrops(location.getBlockState());
    }

    @Override
    public boolean mineBlock(ItemStack heldStack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {

        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            damageHammer(heldStack, livingEntity);
        }

        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack heldStack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack heldStack, LivingEntity target, LivingEntity attacker) {
        damageHammer(heldStack, attacker);
    }

    private void damageHammer(ItemStack heldStack, LivingEntity livingEntity) {
        if (heldStack.getItem() != SledgeItems.STARLIGHT_SLEDGEHAMMER.get())
            heldStack.hurtAndBreak(1, livingEntity, EquipmentSlot.MAINHAND);
    }

    private static ItemAttributeModifiers createAttributes(SledgehammerTiers tier) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus() - 1, AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, tier.getAttackSpeed() - 4, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this == SledgeItems.STARLIGHT_SLEDGEHAMMER.get() || stack.isEnchanted();
    }
}