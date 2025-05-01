package github.cosmicdan.simpledaylengthextender.forge;

import github.cosmicdan.simpledaylengthextender.IModPlatform;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import github.cosmicdan.simpledaylengthextender.TimeTocker;

import java.util.Optional;

import static github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.CONFIG;

public class ModPlatformForge implements IModPlatform {
    @Override
    public void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec);
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
        return 0;
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
