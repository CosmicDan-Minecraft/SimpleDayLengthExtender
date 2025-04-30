package github.cosmicdan.simpledaylengthextender.neoforge;

import github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(SimpleDayLengthExtender.MOD_ID)
public final class SimpleDayLengthExtenderNeoForge {
    public static ModContainer CONTAINER;

    public SimpleDayLengthExtenderNeoForge(ModContainer container, IEventBus modBus) {
        CONTAINER = container;
        SimpleDayLengthExtender.init(new ModPlatformNeoForge());
    }
}
