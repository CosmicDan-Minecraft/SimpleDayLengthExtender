package ovh.cosmicdan.simpledaylengthextender.forge;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.calendar.Calendars;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

public class TfcHelper
{

    public static boolean isTimeStopEnabled() {
        return TFCConfig.SERVER.enableTimeStopWhenServerEmpty.get();
    }

    public static float affectTimeWithCalendar(Level level)
    {
        float dayLengthRatio = 0;

        float fractionOfYear = Calendars.get(level).getCalendarFractionOfYear();

        // simple sinusoidal model
        dayLengthRatio = Mth.sin((float) ((fractionOfYear+0.75f)*Math.PI*2f))/2f + 0.5f;

        LOGGER.info("Calculated day ratio of " + dayLengthRatio + " based on the current TFC Calendar date.");

        return dayLengthRatio;
    }

    public static long getCalendarDay(Level level){
        return Calendars.get(level).getTotalCalendarDays();
    }

    public static TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio){
        // this makes the total day length 1.5 times longer than vanilla to avoid going below 1.0 multiplier for day or night
        int dayLengthInTicks = (int)(8000f*(dayRatio+1));
        if (day){
            return buildNewTocker(
                level.getLevelData(),
                "Day time",
                serverConfig.dayLengthMultiplier.get()*(dayRatio+1),
                0
            );
        }
        return buildNewTocker(
            level.getLevelData(),
            "Night time",
            serverConfig.nightLengthMultiplier.get()*(2-dayRatio),
            serverConfig.nightStartInTicks.get() == 13000 ? 12000 : serverConfig.nightStartInTicks.get()
            // adjust the night start so that night and day are both equal with equal multipliers, with default configs
        );
    }

    public static long getTfcTimeOfDay()
    {
        return Calendars.SERVER.getCalendarDayTime();
    }

}
