package ovh.cosmicdan.simpledaylengthextender;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final String sectionGeneral = "general";

    public final ForgeConfigSpec.BooleanValue delayTimeCycleUntilFirstJoin;
    private static final String delayTimeCycleUntilFirstJoinTxt = "If true (default), the daylight cycle will not start until the first player joins the world. Handy for multiplayer and/or heavy modpacks.";
    public final ForgeConfigSpec.BooleanValue disableTimeCycleWhenServerEmpty;
    private static final String disableTimeCycleWhenServerEmptyTxt = "If true (NOT default), doDaylightCycle will be forced to false when the server/world is empty. Might be useful for multiplayer.";
    // TODO: Make these per-level (dimension)...? Use some kind of list for phases rather than fixed day/night...?
    public final ForgeConfigSpec.IntValue dayStartInTicks;
    private static final String dayStartInTicksTxt = "Start time in ticks to use the day multiplier. 0 represents a standard Minecraft day start of 06:00. Must be less than nightStartInTicks.";
    public final ForgeConfigSpec.DoubleValue dayLengthMultiplier;
    private static final String dayLengthMultiplierTxt = "Multiply the day length by this number. Fractions are supported, but must be above 1.0 (cannot shorten).";
    public final ForgeConfigSpec.IntValue nightStartInTicks;
    private static final String nightStartInTicksTxt = "Start time in ticks to use the night multiplier. 13000 represents a standard Minecraft night start of 19:00.";
    public final ForgeConfigSpec.DoubleValue nightLengthMultiplier;
    private static final String nightLengthMultiplierTxt = "Multiply the night length by this number. Fractions supported, but must be above 1.0 (cannot shorten).";
    public final ForgeConfigSpec.BooleanValue tfcCalendarAutomaticallyAffectsLength;
    private static final String tfcCalendarAutomaticallyAffectsLengthTxt = "Automatically uses the TFC Calendar to adjust day and night lengths.";


    public ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push(sectionGeneral);

        delayTimeCycleUntilFirstJoin = builder
                .comment(delayTimeCycleUntilFirstJoinTxt)
                .define("delayTimeCycleUntilFirstJoin", true);
        disableTimeCycleWhenServerEmpty = builder
                .comment(disableTimeCycleWhenServerEmptyTxt)
                .define("disableTimeCycleWhenServerEmpty", false);
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
        tfcCalendarAutomaticallyAffectsLength = builder
            .comment(tfcCalendarAutomaticallyAffectsLengthTxt)
            .define("tfcCalendarAutomatic", false);
    }
}
