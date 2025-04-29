package ovh.cosmicdan.simpledaylengthextender.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;
import ovh.cosmicdan.simpledaylengthextender.IModPlatform;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

public class ModPlatformFabric implements IModPlatform {
    @Override
    public void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ForgeConfigRegistry.INSTANCE.register(SimpleDayLengthExtender.MOD_ID, type, spec);
    }

    @Override
    public boolean isTfcTimeStopEnabled() {
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
    public long getTfcTimeOfDay() {
        return 0L;
    }
}
