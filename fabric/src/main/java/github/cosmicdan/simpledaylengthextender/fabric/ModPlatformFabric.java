package github.cosmicdan.simpledaylengthextender.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import github.cosmicdan.simpledaylengthextender.IModPlatform;
import github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import net.minecraft.world.level.Level;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;
import github.cosmicdan.simpledaylengthextender.TimeTocker;

public class ModPlatformFabric implements IModPlatform {
    @Override
    public void registerConfigCommon(ModConfigSpec spec) {
        ConfigRegistry.INSTANCE.register(SimpleDayLengthExtender.MOD_ID, ModConfig.Type.COMMON, spec);
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
        return -1;
    }

    @Override
    public long getTfcCalendarDay(Level level) {
        return 0;
    }

    @Override
    @Nullable
    public TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio) {
        return null;
    }

    @Override
    public long getTfcTimeOfDay(Level level) {
        return 0L;
    }
}
