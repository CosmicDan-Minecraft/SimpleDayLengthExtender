package github.cosmicdan.simpledaylengthextender.forge;

import github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import github.cosmicdan.simpledaylengthextender.TimeTocker;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.calendar.Calendars;

public class TfcHelper {
    public static boolean isTimeStopEnabled() {
        // SERVER ONLY
        return TFCConfig.SERVER.enableTimeStopWhenServerEmpty.get();
    }

    public static float affectTimeWithCalendar(Level level) {
        float dayLengthRatio = 0;
        float fractionOfYear = Calendars.get(level).getCalendarFractionOfYear();
        // simple sinusoidal model
        dayLengthRatio = Mth.sin((float) ((fractionOfYear+0.75f)*Math.PI*2f))/2f + 0.5f;
        SimpleDayLengthExtender.LOGGER.info("Calculated day ratio of " + dayLengthRatio + " based on the current TFC Calendar date.");
        return dayLengthRatio;
    }

    public static long getCalendarDay(Level level) {
        return Calendars.get(level).getTotalCalendarDays();
    }

    public static TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio) {
        // this makes the total day length 1.5 times longer than vanilla to avoid going below 1.0 multiplier for day or night
        final TimeTocker tocker;
        if (day) {
            tocker = SimpleDayLengthExtender.buildNewTocker(
                level.getLevelData(),
                "Day time",
                SimpleDayLengthExtender.CONFIG.dayLengthMultiplier.get() * (dayRatio+1),
                0
            );
        } else {
            tocker = SimpleDayLengthExtender.buildNewTocker(
                level.getLevelData(),
                "Night time",
                SimpleDayLengthExtender.CONFIG.nightLengthMultiplier.get() * (2-dayRatio),
                // adjust the night start so that night and day are both equal with equal multipliers, with default configs
                SimpleDayLengthExtender.CONFIG.nightStartInTicks.get() == 13000 ? 12000 : SimpleDayLengthExtender.CONFIG.nightStartInTicks.get()
            );
        }
        return tocker;
    }

    public static long getTfcTimeOfDay(Level level) {
        return Calendars.get(level).getCalendarDayTime();
    }

}
