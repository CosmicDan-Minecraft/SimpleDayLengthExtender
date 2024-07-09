package ovh.cosmicdan.simpledaylengthextender.forge;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Optional;

public class ModPlatformHelperImpl {
    private static boolean tfcCheckPending = true;
    private static boolean tfcTimeStopEnabled = false;

    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ModLoadingContext.get().registerConfig(type, spec);
    }

    public static boolean isTfcTimeStopEnabled() {
        if (tfcCheckPending) {
            tfcCheckPending = false;
            Optional<? extends ModContainer> tfcContainerMaybe = ModList.get().getModContainerById("tfc");
            if (tfcContainerMaybe.isPresent()) {
                tfcTimeStopEnabled = TfcHelper.isTimeStopEnabled();
            }
        }
        return tfcTimeStopEnabled;
    }
}
