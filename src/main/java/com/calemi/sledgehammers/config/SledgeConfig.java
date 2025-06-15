package com.calemi.sledgehammers.config;

import com.calemi.ccore.api.string.StringHelper;
import com.calemi.sledgehammers.main.SledgeRef;
import com.calemi.sledgehammers.main.Sledgehammers;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SledgeConfig {

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final CategoryClient client = new CategoryClient(CLIENT_BUILDER);
    public static final CategoryServer server = new CategoryServer(SERVER_BUILDER);

    public static void init() {
        Sledgehammers.MOD_CONTAINER.registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
        Sledgehammers.MOD_CONTAINER.registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    public static class CategoryClient {

        public final ModConfigSpec.ConfigValue<Boolean> chargeBlockOutlines;
        public final ModConfigSpec.ConfigValue<Boolean> chargeHammerOverlay;

        public CategoryClient(ModConfigSpec.Builder builder) {

            chargeBlockOutlines = booleanValue(builder, "chargeBlockOutlines", true,
                    "Enables the outlines that show what blocks a Sledgehammer will mine while charging."
            );

            chargeHammerOverlay = booleanValue(builder, "chargeHammerOverlay", true,
                    "Enables the hammer icon overlay that appears when a Sledgehammer is charging."
            );
        }
    }

    public static class CategoryServer {

        public final ModConfigSpec.ConfigValue<Boolean> chargeAbilities;
        public final ModConfigSpec.ConfigValue<Boolean> excavateAbility;
        public final ModConfigSpec.ConfigValue<Boolean> veinMineAbility;
        public final ModConfigSpec.ConfigValue<Boolean> fellTreeAbility;
        public final ModConfigSpec.ConfigValue<Integer> maxBlockBreakSize;

        public CategoryServer(ModConfigSpec.Builder builder) {

            chargeAbilities = booleanValue(builder, "chargeAbilities", true,
                    "Enables the ability to charge up the Sledgehammer by using it.",
                    "When releasing can excavate, vein mine ores, and fell trees."
            );

            excavateAbility = booleanValue(builder, "excavateAbility", true,
                    "Enables the ability to excavate using a Sledgehammer.",
                    "This ability destroys a 3x3 flat cube (by default) of blocks."
            );

            veinMineAbility = booleanValue(builder, "veinMineAbility", true,
                    "Enables the ability to vein mine ores using a Sledgehammer.",
                    "This ability destroys all connected ores."
            );

            fellTreeAbility = booleanValue(builder, "fellTreeAbility", true,
                    "Enables the ability to fell trees using a Sledgehammer.",
                    "This ability destroys all connected logs."
            );


            maxBlockBreakSize = intValue(builder, "maxBlockBreakSize", 64, 0, 1024,
                    "The maximum amount of blocks that a Sledgehammer can break at once using the charge ability."
            );
        }
    }

    private static ModConfigSpec.BooleanValue booleanValue(ModConfigSpec.Builder builder, String camelCaseName, boolean defaultValue, String... desc) {
        return startBuilder(builder, camelCaseName, desc)
                .define(camelCaseName, defaultValue);
    }

    private static ModConfigSpec.IntValue intValue(ModConfigSpec.Builder builder, String camelCaseName, int defaultValue, int min, int max, String... desc) {
        return startBuilder(builder, camelCaseName, desc)
                .defineInRange(camelCaseName, defaultValue, min, max);
    }

    private static ModConfigSpec.Builder startBuilder(ModConfigSpec.Builder builder, String camelCaseName, String... desc) {
        return builder
                .translation(getPrefixedKey(StringHelper.camelToSnake(camelCaseName)))
                .comment(StringHelper.camelToTitle(camelCaseName))
                .comment(desc);
    }

    private static String getPrefixedKey(String suffix) {
        return "config." + SledgeRef.ID + "." + suffix;
    }
}