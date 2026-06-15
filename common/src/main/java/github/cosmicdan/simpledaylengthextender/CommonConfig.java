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
    public final ForgeConfigSpec.BooleanValue autoCalendarAdjustments;
    private static final String autoCalendarAdjustmentsTxt = " If true (default), some mod-added calendars/seasons will dynamically adjust day/night ratio.\n" +
            " Note that day/night length will effectively be 1.5x more than the multipliers dictate for math reasons. For example if the multipliers are set to 2.0x then \n" +
            " the day length could be about x2.91 and night length about x1.59 depending on time of year. Keeping showDetailsToLog enabled will let you check final multipliers.\n" +
            " Also note that if the night start time is configured with the default 13000 it will be corrected to 12000 so that the day and night length difference makes sense.\n" +
            " Currently supported mods: TerraFirmaCraft 3.x and below, nothing else yet (please share your requests back to me - I don't know of any other mods that have variable day/night lengths).";
    public final ForgeConfigSpec.BooleanValue showDetailsToLog;
    private static final String showDetailsToLogTxt = " If true (default), details of day/night length will be logged to console. Setting to false might be useful to reduce log spam, especially\n" +
            " if autoCalendarAdjustments are in effect since it would make semi-regular log entries whenever calendar adjustments occur throughout the year/seasons.";


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
                .defineInRange("dayLengthMultiplier", 2.0D, 1.0D, 100.0D);
        nightStartInTicks = builder
                .comment(nightStartInTicksTxt)
                .defineInRange("nightStartInTicks", 13000, 1, Integer.MAX_VALUE - 1000); // arbitrary max
        nightLengthMultiplier = builder
                .comment(nightLengthMultiplierTxt)
                .defineInRange("nightLengthMultiplier", 2.0D, 1.0D, 100.0D);
        autoCalendarAdjustments = builder
                .comment(autoCalendarAdjustmentsTxt)
                .define("autoCalendarAdjustments", true);
        showDetailsToLog = builder
                .comment(showDetailsToLogTxt)
                .define("showDetailsToLog", true);
    }
}
