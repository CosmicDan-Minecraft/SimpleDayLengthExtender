package ovh.cosmicdan.simpledaylengthextender;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ModPlatformHelper {
    @ExpectPlatform
    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {}

    @ExpectPlatform
    public static boolean isTfcTimeStopEnabled() {
        return false;
    }
}
