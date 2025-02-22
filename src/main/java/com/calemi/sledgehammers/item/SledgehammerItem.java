package com.calemi.sledgehammers.item;

import com.calemi.ccore.api.item.ItemDropCollection;
import com.calemi.ccore.api.location.Location;
import com.calemi.ccore.api.log.LogHelper;
import com.calemi.ccore.api.raytrace.RayTraceHelper;
import com.calemi.ccore.api.scanner.VeinBlockScanner;
import com.calemi.ccore.api.sound.SoundHelper;
import com.calemi.ccore.api.tooltip.TooltipHelper;
import com.calemi.ccore.api.worldedit.WorldEditHelper;
import com.calemi.ccore.api.worldedit.shape.ShapeFlatCube;
import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.register.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.ArrayList;
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

        TooltipHelper.addInformationLoreFirst(tooltipComponents, Component.translatable("ch.lore.sledgehammer.1"));
        TooltipHelper.addInformationLore(tooltipComponents, Component.translatable("ch.lore.sledgehammer.2"));
        TooltipHelper.addControlsLoreFirst(tooltipComponents, Component.translatable("ch.lore.sledgehammer.use"), TooltipHelper.ControlType.USE);
        TooltipHelper.addControlsLore(tooltipComponents, Component.translatable("ch.lore.sledgehammer.sneak-use"), TooltipHelper.ControlType.SNEAK_USE);
        TooltipHelper.addControlsLore(tooltipComponents, Component.translatable("ch.lore.sledgehammer.release-use"), TooltipHelper.ControlType.RELEASE_USE);
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

        if (!SledgehammersConfig.server.chargeAbilities.get()) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (!SledgehammersConfig.server.excavateAbility.get() && !SledgehammersConfig.server.veinMineAbility.get() && !SledgehammersConfig.server.fellTreeAbility.get()) {
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

            Location hit = blockTrace.getHit();

            if (ItemTagLists.isLog(hit.getBlock()) && SledgehammersConfig.server.fellTreeAbility.get()) {
                veinMine(heldStack, player, hit);
                return;
            }

            if (ItemTagLists.isOre(hit.getBlock()) && SledgehammersConfig.server.veinMineAbility.get()) {
                veinMine(heldStack, player, hit);
                return;
            }

            if (SledgehammersConfig.server.excavateAbility.get()) excavateBlocks(level, heldStack, player, hit, blockTrace.getHitSide());
        }
    }

    private void veinMine(ItemStack heldStack, Player player, Location startLocation) {

        //Checks if the starting Location can be mined.
        if (canBreakBlock(player, heldStack, startLocation)) {

            Level level = startLocation.getLevel();

            //Start a scan of blocks that equal the starting Location's Block.
            VeinBlockScanner scan = new VeinBlockScanner(startLocation, SledgehammersConfig.server.maxBlockBreakSize.get());
            scan.start();

            LogHelper.log(SledgehammersRef.MOD_NAME, scan.collectedLocations);

            ItemDropCollection dropCollection = new ItemDropCollection();

            //Iterate through the scanned Locations.
            for (Location nextLocation : scan.collectedLocations) {

                //If the Sledgehammer is broken, stop the iteration.
                if (getDamage(heldStack) > getMaxDamage(heldStack)) {
                    break;
                }

                level.addDestroyBlockEffect(nextLocation.getBlockPos(), nextLocation.getBlockState());
                SoundHelper.playBlockBreak(nextLocation, nextLocation.getBlockState());

                if (!level.isClientSide()) {

                    for (ItemStack drop : nextLocation.getBlockDropsFromBreaking(player, heldStack)) {
                        dropCollection.addDrop(drop);
                    }

                    startLocation.spawnExperience(nextLocation.getBlockExperienceFromBreaking(player, heldStack));
                }

                if (!level.isClientSide()) nextLocation.setBlockToAir();
            }

            if (!player.isCreative()) {
                dropCollection.dropAll(startLocation);
            }
        }
    }

    private void excavateBlocks(Level level, ItemStack heldStack, Player player, Location startLocation, Direction face) {


        Holder<Enchantment> efficiencyHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT).wrapAsHolder(Objects.requireNonNull(level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT).get(ResourceLocation.fromNamespaceAndPath(SledgehammersRef.MOD_ID, "crushing"))));

        int radius = EnchantmentHelper.getEnchantmentLevel(efficiencyHolder, player) + 1;

        ArrayList<Location> locations = WorldEditHelper.selectShape(new ShapeFlatCube(startLocation, face, radius));

        ItemDropCollection dropCollection = new ItemDropCollection();

        //Iterate through the Locations from the World Edit shape.
        for (Location nextLocation : locations) {

            //If the Sledgehammer is broken, stop the iteration.
            if (getDamage(heldStack) > getMaxDamage(heldStack)) {
                break;
            }

            //Checks if the next Location can be mined.
            if (canBreakBlock(player, heldStack, nextLocation)) {

                level.addDestroyBlockEffect(nextLocation.getBlockPos(), nextLocation.getBlockState());
                SoundHelper.playBlockBreak(nextLocation, nextLocation.getBlockState());

                if (!level.isClientSide()) {

                    for (ItemStack drop : nextLocation.getBlockDropsFromBreaking(player, heldStack)) {
                        dropCollection.addDrop(drop);
                    }

                    startLocation.spawnExperience(nextLocation.getBlockExperienceFromBreaking(player, heldStack));
                }

                if (!level.isClientSide()) nextLocation.setBlockToAir();
                damageHammer(heldStack, player);
            }
        }

        if (!player.isCreative()) {
            dropCollection.dropAll(startLocation);
        }
    }

    private boolean canBreakBlock(Player player, ItemStack heldStack, Location location) {
        float hardness = location.getBlockState().getDestroySpeed(location.getLevel(), location.getBlockPos());
        return !location.isAirBlock() && location.canHarvestBlock(player) && hardness >= 0 && hardness <= 50 && heldStack.isCorrectToolForDrops(location.getBlockState());
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
        if (heldStack.getItem() != ItemRegistry.STARLIGHT_SLEDGEHAMMER.get())
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
        return this == ItemRegistry.STARLIGHT_SLEDGEHAMMER.get() || stack.isEnchanted();
    }
}