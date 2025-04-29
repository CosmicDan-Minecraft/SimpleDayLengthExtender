package github.cosmicdan.simpledaylengthextender;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final String sectionGeneral = "general";

    public final ForgeConfigSpec.BooleanValue delayTimeCycleUntilFirstJoin;
    private static final String delayTimeCycleUntilFirstJoinTxt = " If true (default), the daylight cycle will not start until the first player joins the world. Handy for multiplayer and/or heavy modpacks.";
    public final ForgeConfigSpec.BooleanValue disableTimeCycleWhenServerEmpty;
    private static final String disableTimeCycleWhenServerEmptyTxt = " If true (NOT default), doDaylightCycle will be forced to false when the server/world is empty. Might be useful for multiplayer.";
    public final ForgeConfigSpec.IntValue dayStartInTicks;
    private static final String dayStartInTicksTxt = " Start time in ticks to use the day multiplier. 0 represents a standard Minecraft day start of 06:00. Must be less than nightStartInTicks.";
    public final ForgeConfigSpec.DoubleValue dayLengthMultiplier;
    private static final String dayLengthMultiplierTxt = " Multiply the day length by this number. Fractions are supported, but must be above 1.0 (cannot shorten).";
    public final ForgeConfigSpec.IntValue nightStartInTicks;
    private static final String nightStartInTicksTxt = " Start time in ticks to use the night multiplier. 13000 represents a standard Minecraft night start of 19:00.";
    public final ForgeConfigSpec.DoubleValue nightLengthMultiplier;
    private static final String nightLengthMultiplierTxt = " Multiply the night length by this number. Fractions supported, but must be above 1.0 (cannot shorten).";
    public final ForgeConfigSpec.BooleanValue tfcCalendarAutomaticallyAffectsLength;
    private static final String tfcCalendarAutomaticallyAffectsLengthTxt = " If true (default), the TFC Calendar will adjust day/night ratio depending on season. No effect if TFC is not installed.\n" +
            " Note that day and night length will be forced to a minimum of x1.5 each in order to prevent either phase going below x1.0 which isn't possible.\n" +
            " Note that if the night start time is configured to the default 13000 it will be adjusted to 12000 so that day and night share an equal length.\n";
    public final ForgeConfigSpec.BooleanValue showDetailsToLog;
    private static final String showDetailsToLogTxt = " If true (default), details of day/night length will be logged to console. Setting to false might be useful to reduce log spam, especially\n" +
            " if TFC is installed and using calendar adjustments since it would make semi-regular log entries whenever calendar adjustments occur throughout the year.";


    public CommonConfig(final ForgeConfigSpec.Builder builder) {
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
                .define("tfcCalendarAutomatic", true);
        showDetailsToLog = builder
                .comment(showDetailsToLogTxt)
                .define("showDetailsToLog", true);
    }
}
