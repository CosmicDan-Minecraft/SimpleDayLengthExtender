package ovh.cosmicdan.simpledaylengthextender.forge;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import java.util.Optional;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

public class ModPlatformHelperImpl
{
    private static boolean tfcCheckPending = true;
    private static boolean tfcTimeStopEnabled = false;

    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec)
    {
        ModLoadingContext.get().registerConfig(type, spec);
    }

    public static boolean isTfcTimeStopEnabled()
    {
        if (tfcCheckPending)
        {
            tfcCheckPending = false;
            Optional<? extends ModContainer> tfcContainerMaybe = ModList.get().getModContainerById("tfc");
            if (tfcContainerMaybe.isPresent())
            {
                tfcTimeStopEnabled = TfcHelper.isTimeStopEnabled();
            }
        }
        return tfcTimeStopEnabled;
    }

    public static boolean isTfcOverrideConfigured()
    {
        Optional<? extends ModContainer> tfcContainerMaybe = ModList.get().getModContainerById("tfc");
        return tfcContainerMaybe.isPresent() && serverConfig.tfcCalendarAutomaticallyAffectsLength.get();
    }

    public static float getTfcManagedRatio(Level level)
    {
        if(isTfcOverrideConfigured()){
            return TfcHelper.affectTimeWithCalendar(level);
        }
        return 0;
    }

    public static long getTfcCalendarDay(Level level)
    {
        if(isTfcOverrideConfigured()){
            return TfcHelper.getCalendarDay(level);
        }
        return 0;
    }

    public static TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio)
    {
        if(isTfcOverrideConfigured()){
            return TfcHelper.buildTfcManagedTocker(day, level, dayRatio);
        }
        return null;
    }

    public static long getTfcTimeOfDay()
    {
        if(isTfcOverrideConfigured()){
            return TfcHelper.getTfcTimeOfDay();
        }
        return 0L;
    }
}
