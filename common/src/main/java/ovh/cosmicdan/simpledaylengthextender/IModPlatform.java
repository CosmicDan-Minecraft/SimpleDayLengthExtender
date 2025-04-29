package ovh.cosmicdan.simpledaylengthextender;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public interface IModPlatform {
    void registerConfig(ModConfig.Type type, ForgeConfigSpec spec);
    boolean isTfcTimeStopEnabled();
    boolean isTfcOverrideConfigured();
    float getTfcManagedRatio(Level level);
    long getTfcCalendarDay(Level level);
    TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio);
    long getTfcTimeOfDay();
}
