package ovh.cosmicdan.simpledaylengthextender.fabric;

import net.fabricmc.api.ModInitializer;

import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;

public final class SimpleDayLengthExtenderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SimpleDayLengthExtender.init();
    }
}
