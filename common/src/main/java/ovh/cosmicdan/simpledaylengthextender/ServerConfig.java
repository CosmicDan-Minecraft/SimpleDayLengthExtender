package ovh.cosmicdan.simpledaylengthextender;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final String sectionGeneral = "general";

    // TODO: Make these per-level (dimension)...? Use some kind of list for phases rather than fixed day/night...?
    public final ForgeConfigSpec.IntValue dayStartInTicks;
    private static final String dayStartInTicksTxt = "Start time in ticks to use the day multiplier. 0 represents a standard Minecraft day start of 06:00. Must be less than nightStartInTicks.";
    public final ForgeConfigSpec.DoubleValue dayLengthMultiplier;
    private static final String dayLengthMultiplierTxt = "Multiply the day length by this number. Fractions are supported, but must be above 1.0 (cannot shorten).";
    public final ForgeConfigSpec.IntValue nightStartInTicks;
    private static final String nightStartInTicksTxt = "Start time in ticks to use the night multiplier. 13000 represents a standard Minecraft night start of 19:00.";
    public final ForgeConfigSpec.DoubleValue nightLengthMultiplier;
    private static final String nightLengthMultiplierTxt = "Multiply the night length by this number. Fractions supported, but must be above 1.0 (cannot shorten).";

    public ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push(sectionGeneral);
        dayStartInTicks = builder
                .comment(dayStartInTicksTxt)
                .defineInRange("dayStartInTicks", 0, 0, Integer.MAX_VALUE - 1001); // arbitrary max
        dayLengthMultiplier = builder
                .comment(dayLengthMultiplierTxt)
                .defineInRange("dayLengthMultiplier", 1.0D, 1.0D, 100.0D);
        nightStartInTicks = builder
                .comment(nightStartInTicksTxt)
                .defineInRange("nightStartInTicks", 13000, 1, Integer.MAX_VALUE - 1000); // arbitrary max
        nightLengthMultiplier = builder
                .comment(nightLengthMultiplierTxt)
                .defineInRange("nightLengthMultiplier", 1.0D, 1.0D, 100.0D);
    }
}
