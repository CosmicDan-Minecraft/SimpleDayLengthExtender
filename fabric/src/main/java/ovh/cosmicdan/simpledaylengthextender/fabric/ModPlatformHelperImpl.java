package ovh.cosmicdan.simpledaylengthextender.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;

public class ModPlatformHelperImpl {
    public static void registerConfig(ModConfig.Type type, ForgeConfigSpec spec) {
        ForgeConfigRegistry.INSTANCE.register(SimpleDayLengthExtender.MOD_ID, type, spec);
    }
}
