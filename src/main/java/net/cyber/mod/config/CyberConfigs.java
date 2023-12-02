package net.cyber.mod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CyberConfigs {
    public static final CyberConfigs.Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CyberConfigs.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CyberConfigs.Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {
        public ForgeConfigSpec.ConfigValue<Integer> BaseEssence;

        public Common(ForgeConfigSpec.Builder builder) {

            BaseEssence = builder.comment("Changes the base amount of Essence")
                    .translation("config.tascension.BaseEssence")
                    .define("BaseEssence", 100);
        }

    }
}
