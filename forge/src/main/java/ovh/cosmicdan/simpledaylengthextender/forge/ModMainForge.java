package ovh.cosmicdan.simpledaylengthextender.forge;

import net.minecraftforge.fml.common.Mod;

import ovh.cosmicdan.simpledaylengthextender.ModMain;

@Mod(ModMain.MOD_ID)
public final class ModMainForge {
    public ModMainForge() {
        // Run our common setup.
        ModMain.init();
    }
}
