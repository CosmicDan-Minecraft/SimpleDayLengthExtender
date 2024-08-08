package ovh.cosmicdan.simpledaylengthextender.forge;

import java.util.Optional;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.loading.FMLEnvironment;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;

@Mod(SimpleDayLengthExtender.MOD_ID)
public final class SimpleDayLengthExtenderForge {

    public SimpleDayLengthExtenderForge() {
        // Run our common setup.
        SimpleDayLengthExtender.init();
    }
}
