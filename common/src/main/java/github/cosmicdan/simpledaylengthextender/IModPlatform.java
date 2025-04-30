package github.cosmicdan.simpledaylengthextender;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

public interface IModPlatform {
    void registerConfigCommon(ModConfigSpec spec);
    boolean isTfcInstalled();
    boolean isTfcTimeStopEnabled(); // SERVER ONLY
    boolean isTfcOverrideConfigured();
    float getTfcManagedRatio(Level level);
    long getTfcCalendarDay(Level level);
    TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio);
    long getTfcTimeOfDay(Level level);
}
