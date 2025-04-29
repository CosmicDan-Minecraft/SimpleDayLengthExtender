package ovh.cosmicdan.simpledaylengthextender;

import java.util.Optional;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;

public class ModPlatformHelper {
    @ExpectPlatform
    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {}

    @ExpectPlatform
    public static boolean isTfcTimeStopEnabled() {
        return false;
    }

    @ExpectPlatform
    public static boolean isTfcOverrideConfigured()
    {
        return false;
    }

    @ExpectPlatform
    public static float getTfcManagedRatio(Level level)
    {
        return -1;
    }

    @ExpectPlatform
    public static long getTfcCalendarDay(Level level)
    {
        return 0;
    }

    @ExpectPlatform
    @Nullable
    public static TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio)
    {
        return null;
    }

    @ExpectPlatform
    @Nullable
    public static long getTfcTimeOfDay()
    {
        return 0L;
    }
}
