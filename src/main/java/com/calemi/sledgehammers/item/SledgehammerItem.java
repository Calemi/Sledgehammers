package com.calemi.sledgehammers.item;

import com.calemi.ccore.api.general.BlockScanner;
import com.calemi.ccore.api.general.Location;
import com.calemi.ccore.api.general.helper.LogHelper;
import com.calemi.ccore.api.general.helper.LoreHelper;
import com.calemi.ccore.api.general.helper.RayTraceHelper;
import com.calemi.ccore.api.general.helper.WorldEditHelper;
import com.calemi.ccore.api.shape.ShapeFlatCube;
import com.calemi.sledgehammers.config.SledgehammersConfig;
import com.calemi.sledgehammers.main.SledgehammersRef;
import com.calemi.sledgehammers.register.EnchantmentRegistry;
import com.calemi.sledgehammers.register.ItemRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SledgehammerItem extends DiggerItem {

    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public int baseChargeTime;
    public int chargeTime;

    public SledgehammerItem(SledgehammerTiers tier, Item.Properties properties) {
        super(0, 0, tier, BlockTags.MINEABLE_WITH_PICKAXE, properties.stacksTo(1));

        this.chargeTime = baseChargeTime;
        this.baseChargeTime = tier.getChargeTime();

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", tier.getAttackDamageBonus() - 1, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", tier.getAttackSpeed() - 4, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public SledgehammerItem(SledgehammerTiers tier) {
        this(tier, new Item.Properties().stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipList, TooltipFlag advanced) {

        LoreHelper.addInformationLoreFirst(tooltipList, Component.translatable("ch.lore.sledgehammer.1"));
        LoreHelper.addInformationLore(tooltipList, Component.translatable("ch.lore.sledgehammer.2"));
        LoreHelper.addControlsLoreFirst(tooltipList, Component.translatable("ch.lore.sledgehammer.use"), LoreHelper.ControlType.USE);
        LoreHelper.addControlsLore(tooltipList, Component.translatable("ch.lore.sledgehammer.sneak-use"), LoreHelper.ControlType.SNEAK_USE);
        LoreHelper.addControlsLore(tooltipList, Component.translatable("ch.lore.sledgehammer.release-use"), LoreHelper.ControlType.RELEASE_USE);

        if (level == null) {
            return;
        }

        Optional<ArmorTrim> optionalTrim = ArmorTrim.getTrim(level.registryAccess(), stack);

        if (stack.isEnchanted() || optionalTrim.isPresent()) {
            LoreHelper.addBlankLine(tooltipList);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);

        if (!SledgehammersConfig.server.chargeAbilities.get()) {
            return InteractionResultHolder.pass(itemstack);
        }

        if (!SledgehammersConfig.server.excavateAbility.get() && !SledgehammersConfig.server.veinMineAbility.get() && !SledgehammersConfig.server.fellTreeAbility.get()) {
            return InteractionResultHolder.pass(itemstack);
        }

        //If the off hand has an item and the Player is not crouching, prevent charging.
        if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty() && !player.isCrouching()) {
            return InteractionResultHolder.fail(itemstack);
        }

        chargeTime = Math.max(1, baseChargeTime - EnchantmentHelper.getBlockEfficiency(player) * 3);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack heldStack, Level level, LivingEntity entity, int timeLeft) {

        Player player = (Player) entity;

        InteractionHand hand = InteractionHand.OFF_HAND;

        //If the Sledgehammer is in the main hand, set the current hand to main.
        if (ItemStack.isSameItemSameTags(player.getMainHandItem(), heldStack)) {
            hand = InteractionHand.MAIN_HAND;
        }

        //Checks if fully charged.
        if (getUseDuration(heldStack) - timeLeft >= chargeTime) {

            player.swing(hand);

            RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.rayTraceBlock(level, player, 5);

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
        if (canBreakBlock(player, startLocation)) {

            //Start a scan of blocks that equal the starting Location's Block.
            BlockScanner scan = new BlockScanner(startLocation, startLocation.getBlock().defaultBlockState(), SledgehammersConfig.server.maxBlockBreakSize.get(), true);
            scan.startRadiusScan();

            LogHelper.log(SledgehammersRef.MOD_NAME, scan.buffer);

            int damage = getDamage(heldStack);

            //Iterate through the scanned Locations.
            for (Location nextLocation : scan.buffer) {

                int maxDamage = getMaxDamage(heldStack);

                //If the Sledgehammer is broken, stop the iteration.
                if (damage > maxDamage && maxDamage > 0) {
                    return;
                }

                nextLocation.breakBlock(player);
                damage++;
            }
        }
    }

    private void excavateBlocks(Level worldIn, ItemStack heldStack, Player player, Location location, Direction face) {

        int radius = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.CRUSHING.get(), player) + 1;

        ArrayList<Location> locations = WorldEditHelper.selectShape(new ShapeFlatCube(location, face, radius));

        int damage = getDamage(heldStack);

        //Iterate through the Locations from the World Edit shape.
        for (Location nextLocation : locations) {

            int maxDamage = getMaxDamage(heldStack);

            //If the Sledgehammer is broken, stop the iteration.
            if (damage > maxDamage && maxDamage > 0) {
                return;
            }

            //Checks if the next Location can be mined.
            if (canBreakBlock(player, nextLocation)) {
                nextLocation.breakBlock(player);
                damage++;
            }
        }
    }

    private boolean canBreakBlock(Player player, Location location) {
        float hardness = location.getBlockState().getDestroySpeed(location.getLevel(), location.getBlockPos());
        return hardness >= 0 && hardness <= 50 && ForgeHooks.isCorrectToolForDrops(location.getBlockState(), player);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity livingEntity) {

        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            damageHammer(stack, livingEntity);
        }

        return true;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity player, LivingEntity target) {
        damageHammer(stack, player);
        return true;
    }

    private void damageHammer(ItemStack stack, LivingEntity livingEntity) {
        if (stack.getItem() != ItemRegistry.STARLIGHT_SLEDGEHAMMER.get())
            stack.hurtAndBreak(1, livingEntity, (i) -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {
        return this.speed;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this == ItemRegistry.STARLIGHT_SLEDGEHAMMER.get() || stack.isEnchanted();
    }
}
