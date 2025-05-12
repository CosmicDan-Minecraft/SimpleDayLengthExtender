package github.cosmicdan.simpledaylengthextender;

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
    public static IModPlatform MODPLATFORM;

    public static CommonConfig CONFIG = null;

    public static final int TFC_CHECK_INTERVAL = 1024;

    public static void init(IModPlatform modPlatform) {
        // Register common config
        final Pair<CommonConfig, ForgeConfigSpec> specPairConfigCommon = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        CONFIG = specPairConfigCommon.getLeft();
        MODPLATFORM = modPlatform;
        MODPLATFORM.registerConfig(ModConfig.Type.COMMON, specPairConfigCommon.getRight());
    }

    public static TimeTocker buildNewTocker(LevelData levelData, String phaseName, double phaseMultiplier, int phaseStartInTicks) {
        TimeTocker newTocker = new TimeTocker(phaseMultiplier, phaseStartInTicks);
        if (SimpleDayLengthExtender.CONFIG.showDetailsToLog.get()) {
            LOGGER.info("Using a multiplier of x" + phaseMultiplier + " for " + phaseName);
            LOGGER.info("    - Phase starts at " + newTocker.phaseStartInTicks + " and will advance daylight cycle by " + newTocker.tockerInc + " ticks over " + newTocker.tockerMax + " game ticks on average.");
        }
        return newTocker;
    }

    public static TimeTocker buildNewTockerDay(LevelData levelData) {
        return buildNewTocker(
                levelData,
                "Day time",
                CONFIG.dayLengthMultiplier.get(),
                CONFIG.dayStartInTicks.get()
        );
    }

    public static TimeTocker buildNewTockerNight(LevelData levelData) {
        return buildNewTocker(
                levelData,
                "Night time",
                CONFIG.nightLengthMultiplier.get(),
                CONFIG.nightStartInTicks.get()
        );
    }

    public static boolean shouldAllowDaylightProgression(Level level, TimeTocker dayTocker, TimeTocker nightTocker) {
        boolean shouldAdvanceTime = false;

        final long timeOfDay;
        if (MODPLATFORM.isTfcOverrideConfigured())
            timeOfDay = MODPLATFORM.getTfcTimeOfDay(level);
        else
            timeOfDay = level.getLevelData().getDayTime() % 24000;

        if (timeOfDay >= nightTocker.phaseStartInTicks) {
            shouldAdvanceTime = nightTocker.shouldAdvanceTime(level);
        } else {
            shouldAdvanceTime = dayTocker.shouldAdvanceTime(level);
        }

        return shouldAdvanceTime;
    }

    // SERVER ONLY
    public static boolean shouldDisableCycleWhenEmpty() {
        boolean shouldDisable = SimpleDayLengthExtender.CONFIG.disableTimeCycleWhenServerEmpty.get();
        if (MODPLATFORM.isTfcTimeStopEnabled()) {
            if (!shouldDisable) {
                LOGGER.warn("The config setting 'disableTimeCycleWhenServerEmpty' was overridden to true because " +
                        "TFC's 'enableTimeStopWhenServerEmpty' is also true. This warning will not appear again.");
                SimpleDayLengthExtender.CONFIG.disableTimeCycleWhenServerEmpty.set(true);
                SimpleDayLengthExtender.CONFIG.disableTimeCycleWhenServerEmpty.save();
            }
            shouldDisable = true;
        }
        return shouldDisable;
    }
}
