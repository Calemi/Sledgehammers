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

        public final ForgeConfigSpec.ConfigValue<Integer> maxVeinMineSize;

        public final ForgeConfigSpec.ConfigValue<Integer> starlightUpgradeSpawnChance;
        public final ForgeConfigSpec.ConfigValue<Integer> starlightUpgradeSpawnAmount;

        public CategoryServer (ForgeConfigSpec.Builder builder) {

            maxVeinMineSize = builder.comment("Max Vein Mine Size")
                    .comment("The max amount of Blocks to break when charging a Sledgehammer.")
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