package ovh.cosmicdan.simpledaylengthextender;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

public final class SimpleDayLengthExtender {
    public static final String MOD_ID = "simpledaylengthextender";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ServerConfig serverConfig = null;

    //private static boolean firstTick = true;
    //public static TimeTocker dayTocker = null;
    //public static TimeTocker nightTocker = null;

    public static void init() {
        // Register common config
        final Pair<ServerConfig, ForgeConfigSpec> specPairConfigCommon = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        serverConfig = specPairConfigCommon.getLeft();
        ModPlatformHelper.registerConfig(ModConfig.Type.SERVER, specPairConfigCommon.getRight());
    }

    public static TimeTocker buildNewTocker(LevelData levelData, String phaseName, double phaseMultiplier, int phaseStartInTicks) {
        LOGGER.info("Using a multiplier of x" + phaseMultiplier + " for " + phaseName);
        TimeTocker newTocker = new TimeTocker(phaseMultiplier, phaseStartInTicks);
        LOGGER.info("    - Phase starts at " + newTocker.phaseStartInTicks + " and will advance daylight cycle by " + newTocker.tockerInc + " ticks over " + newTocker.tockerMax + " game ticks on average.");
        return newTocker;
    }

    public static TimeTocker buildNewTockerDay(LevelData levelData) {
        return buildNewTocker(
                levelData,
                "Day time",
                serverConfig.dayLengthMultiplier.get(),
                serverConfig.dayStartInTicks.get()
        );
    }

    public static TimeTocker buildNewTockerNight(LevelData levelData) {
        return buildNewTocker(
                levelData,
                "Night time",
                serverConfig.nightLengthMultiplier.get(),
                serverConfig.nightStartInTicks.get()
        );
    }

    public static boolean shouldAllowDaylightProgression(LevelData levelData, TimeTocker dayTocker, TimeTocker nightTocker) {
        boolean shouldAdvanceTime = false;
        final long timeOfDay = levelData.getDayTime() % Level.TICKS_PER_DAY;
        if (timeOfDay >= nightTocker.phaseStartInTicks) {
            shouldAdvanceTime = nightTocker.shouldAdvanceTime(levelData);
        } else {
            shouldAdvanceTime = dayTocker.shouldAdvanceTime(levelData);
        }
        return shouldAdvanceTime;
    }

    public static boolean shouldDisableCycleWhenEmtpy() {
        boolean shouldDisable = SimpleDayLengthExtender.serverConfig.disableTimeCycleWhenServerEmpty.get();
        if (ModPlatformHelper.isTfcTimeStopEnabled()) {
            if (shouldDisable == false) {
                LOGGER.warn("The config setting 'disableTimeCycleWhenServerEmpty' was overridden to true because " +
                        "TFC's 'enableTimeStopWhenServerEmpty' is also true. To disable this warning, set " +
                        "'disableTimeCycleWhenServerEmpty' to true or disable TFC's time stop.");
            }
            shouldDisable = true;
        }
        return shouldDisable;
    }
}
