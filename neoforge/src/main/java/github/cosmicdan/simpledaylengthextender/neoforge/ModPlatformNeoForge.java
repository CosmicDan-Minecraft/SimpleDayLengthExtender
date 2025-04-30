package github.cosmicdan.simpledaylengthextender.neoforge;

import github.cosmicdan.simpledaylengthextender.IModPlatform;
import net.minecraft.world.level.Level;
import github.cosmicdan.simpledaylengthextender.TimeTocker;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModPlatformNeoForge implements IModPlatform {
    @Override
    public void registerConfigCommon(ModConfigSpec spec) {
        SimpleDayLengthExtenderNeoForge.CONTAINER.registerConfig(ModConfig.Type.COMMON, spec);
    }

    @Override
    public boolean isTfcInstalled() {
        return false;
    }

    @Override
    public boolean isTfcTimeStopEnabled() {
        // SERVER ONLY
        return false;
    }

    @Override
    public boolean isTfcOverrideConfigured() {
        return false;
    }

    @Override
    public float getTfcManagedRatio(Level level) {
        return 0.0f;
    }

    @Override
    public long getTfcCalendarDay(Level level) {
        return 0;
    }

    @Override
    public TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio) {
        return null;
    }

    @Override
    public long getTfcTimeOfDay(Level level) {
        return 0L;
    }
}
