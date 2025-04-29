package github.cosmicdan.simpledaylengthextender.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import github.cosmicdan.simpledaylengthextender.IModPlatform;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;
import github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import github.cosmicdan.simpledaylengthextender.TimeTocker;

public class ModPlatformFabric implements IModPlatform {
    @Override
    public void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ForgeConfigRegistry.INSTANCE.register(SimpleDayLengthExtender.MOD_ID, type, spec);
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
