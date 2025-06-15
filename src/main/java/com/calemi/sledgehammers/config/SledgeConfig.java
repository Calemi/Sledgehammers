package com.calemi.sledgehammers.config;

import com.calemi.sledgehammers.main.Sledgehammers;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SledgeConfig {

    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static final CategoryServer server = new CategoryServer(SERVER_BUILDER);
    public static final CategoryClient client = new CategoryClient(CLIENT_BUILDER);

    public static void init() {
        Sledgehammers.MOD_CONTAINER.registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
        Sledgehammers.MOD_CONTAINER.registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    public static class CategoryServer {

        public final ModConfigSpec.ConfigValue<Boolean> chargeAbilities;
        public final ModConfigSpec.ConfigValue<Boolean> excavateAbility;
        public final ModConfigSpec.ConfigValue<Boolean> veinMineAbility;
        public final ModConfigSpec.ConfigValue<Boolean> fellTreeAbility;
        public final ModConfigSpec.ConfigValue<Integer> maxBlockBreakSize;

        public CategoryServer (ModConfigSpec.Builder builder) {

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
        }
    }

    public static class CategoryClient {

        public final ModConfigSpec.ConfigValue<Boolean> chargeBlockOutlines;

        public CategoryClient (ModConfigSpec.Builder builder) {

            chargeBlockOutlines = builder.comment("Charge Block Outlines")
                    .comment("Enables the outlines that show what blocks the Sledgehammer will mine while charging.")
                    .define("chargeBlockOutlines", true);
        }
    }
}