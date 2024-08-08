package ovh.cosmicdan.simpledaylengthextender.fabric;

import java.util.Optional;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

public class ModPlatformHelperImpl {
    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ForgeConfigRegistry.INSTANCE.register(SimpleDayLengthExtender.MOD_ID, type, spec);
    }

    public static boolean isTfcTimeStopEnabled() {
        return false;
    }

    public static boolean isTfcOverrideConfigured()
    {
        return false;
    }

    public static float getTfcManagedRatio(Level level)
    {
        return -1;
    }

    public static long getTfcCalendarDay(Level level)
    {
        return 0;
    }

    @Nullable
    public static TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio)
    {
        return null;
    }


}
