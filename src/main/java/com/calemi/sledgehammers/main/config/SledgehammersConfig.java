package com.calemi.sledgehammers.main.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class SledgehammersConfig {

    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final CategoryServer server = new CategoryServer(SERVER_BUILDER);

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    public static class CategoryServer {

        public final ForgeConfigSpec.ConfigValue<Boolean> chargeAbilities;
        public final ForgeConfigSpec.ConfigValue<Boolean> excavateAbility;
        public final ForgeConfigSpec.ConfigValue<Boolean> veinMineAbility;
        public final ForgeConfigSpec.ConfigValue<Boolean> fellTreeAbility;
        public final ForgeConfigSpec.ConfigValue<Integer> maxBlockBreakSize;

        public final ForgeConfigSpec.ConfigValue<Integer> starlightUpgradeSpawnChance;
        public final ForgeConfigSpec.ConfigValue<Integer> starlightUpgradeSpawnAmount;

        public CategoryServer (ForgeConfigSpec.Builder builder) {

            chargeAbilities = builder.comment("Charge Abilities")
                    .comment("Enables the ability to charge up the Sledgehammer by using it.")
                    .comment("When releasing can excavate, vein mine ores, and fell trees.")
                    .define("chargeAbilities", true);

            excavateAbility = builder.comment("Excavate Ability")
                    .comment("Enables the ability to excavate using a Sledgehammer.")
                    .comment("This ability destroys a 3x3 flat cube (by default) of blocks.")
                    .define("excavateAbility", true);

            veinMineAbility = builder.comment("Vein Mine Ability")
                    .comment("Enables the ability to vein mine ores using a Sledgehammer.")
                    .comment("This ability destroys all connected ores.")
                    .define("veinMineAbility", true);

            fellTreeAbility = builder.comment("Fell Tree Ability")
                    .comment("Enables the ability to fell trees using a Sledgehammer.")
                    .comment("This ability destroys all connected logs.")
                    .define("fellTreeAbility", true);

            maxBlockBreakSize = builder.comment("Max Vein Mine Size")
                    .comment("The maximum amount of blocks that a Sledgehammer can break at once using the charge ability.")
                    .defineInRange("maxVeinMineSize", 64, 0, 1024);

            starlightUpgradeSpawnChance = builder.comment("Starlight Upgrade Spawn Chance")
                    .comment("The chance of a Starlight Upgrade Smithing Template appearing in an ancient city or end city chest.")
                    .comment("It is a percentage value.", "Set it to 0 to prevent spawning.")
                    .defineInRange("starlightUpgradeSpawnChance", 50, 0, 100);

            starlightUpgradeSpawnAmount = builder.comment("Starlight Upgrade Spawn Amount")
                    .comment("The amount of Starlight Upgrade Smithing Templates appear in a chest.")
                    .defineInRange("starlightUpgradeSpawnAmount", 2, 1, 64);
        }
    }
}