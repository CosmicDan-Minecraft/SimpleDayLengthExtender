package ovh.cosmicdan.simpledaylengthextender;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

public final class SimpleDayLengthExtender {
    public static final String MOD_ID = "simpledaylengthextender";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ServerConfig serverConfig = null;

    private static boolean firstTick = true;

    public static void init() {
        // Register common config
        final Pair<ServerConfig, ForgeConfigSpec> specPairConfigCommon = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        serverConfig = specPairConfigCommon.getLeft();
        ModPlatformHelper.registerConfig(ModConfig.Type.SERVER, specPairConfigCommon.getRight());
    }

    public static boolean shouldAllowDaylightProgression(final LevelData levelData) {
        if (firstTick) {
            firstTick = false;
            LOGGER.info("World daytime multiplier is x" + serverConfig.dayLengthMultiplier.get());
            LOGGER.info("World nighttime multiplier is x" + serverConfig.nightLengthMultiplier.get());
        }
        boolean allowDaylightProgression = false;
        return allowDaylightProgression;
    }
}
