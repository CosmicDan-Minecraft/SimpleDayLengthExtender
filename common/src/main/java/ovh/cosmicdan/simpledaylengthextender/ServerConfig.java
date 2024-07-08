package ovh.cosmicdan.simpledaylengthextender;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final String sectionGeneral = "general";

    public final ForgeConfigSpec.DoubleValue dayLengthMultiplier;
    private static final String dayLengthMultiplierTxt = "Multiply the day length by this number";
    public final ForgeConfigSpec.DoubleValue nightLengthMultiplier;
    private static final String nightLengthMultiplierTxt = "Multiply the night length by this number";

    public ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push(sectionGeneral);
        dayLengthMultiplier = builder
                .comment(dayLengthMultiplierTxt)
                .defineInRange("dayLengthMultiplier", 1.0D, 1.0D, 10.0D);
        nightLengthMultiplier = builder
                .comment(nightLengthMultiplierTxt)
                .defineInRange("nightLengthMultiplier", 1.0D, 1.0D, 10.0D);
    }
}
