package ovh.cosmicdan.simpledaylengthextender.forge;

import net.minecraftforge.fml.common.Mod;

import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;

@Mod(SimpleDayLengthExtender.MOD_ID)
public final class SimpleDayLengthExtenderForge {
    public SimpleDayLengthExtenderForge() {
        // Run our common setup.
        SimpleDayLengthExtender.init();
    }
}
