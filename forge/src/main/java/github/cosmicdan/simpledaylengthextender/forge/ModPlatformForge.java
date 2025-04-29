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
    private boolean tfcInstalledCheckDone = false;
    private boolean tfcInstalled = false;
    private boolean tfcCheckPending = true;
    private boolean tfcTimeStopEnabled = false;

    @Override
    public void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec);
    }

    @Override
    public boolean isTfcInstalled() {
        if (!tfcInstalledCheckDone) {
            tfcInstalledCheckDone = true;
            Optional<? extends ModContainer> tfcContainerMaybe = ModList.get().getModContainerById("tfc");
            if (tfcContainerMaybe.isPresent())
                tfcInstalled = true;
        }
        return tfcInstalled;
    }

    @Override
    public boolean isTfcTimeStopEnabled() {
        // SERVER ONLY
        if (tfcCheckPending) {
            tfcCheckPending = false;
            if (isTfcInstalled())
                tfcTimeStopEnabled = TfcHelper.isTimeStopEnabled();
        }
        return tfcTimeStopEnabled;
    }

    @Override
    public boolean isTfcOverrideConfigured() {
        return isTfcInstalled() && CONFIG.autoCalendarAdjustments.get();
    }

    @Override
    public float getTfcManagedRatio(Level level) {
        if (isTfcOverrideConfigured())
            return TfcHelper.affectTimeWithCalendar(level);
        else
            return 0;
    }

    @Override
    public long getTfcCalendarDay(Level level) {
        if (isTfcOverrideConfigured())
            return TfcHelper.getCalendarDay(level);
        else
            return 0;
    }

    @Override
    public TimeTocker buildTfcManagedTocker(Boolean day, Level level, float dayRatio) {
        if (isTfcOverrideConfigured())
            return TfcHelper.buildTfcManagedTocker(day, level, dayRatio);
        else
            return null;
    }

    @Override
    public long getTfcTimeOfDay(Level level) {
        if (isTfcOverrideConfigured())
            return TfcHelper.getTfcTimeOfDay(level);
        else
            return 0L;
    }
}
