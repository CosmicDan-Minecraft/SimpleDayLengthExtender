package github.cosmicdan.simpledaylengthextender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.MODPLATFORM;
import static github.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.TFC_CHECK_INTERVAL;

public class LevelTockHandler {
    // TODO: Destroy self or whatever when server/world closes
    private final Level level;

    // common-side vars
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    private TimeTocker simpleDayLengthExtender_nightTocker = null;
    private long simpleDayLengthExtender_previousCalendarDay = 0;
    // server-side vars
    private boolean simpleDayLengthExtender_disableCycleWhenEmpty = true;
    private boolean simpleDayLengthExtender_waitingForPlayer = false;
    private int simpleDayLengthExtender_playersLastCount = 0;

    public LevelTockHandler(Level levelIn) {
        level = levelIn;
    }

    public void onTickTimeDayCycleRuleCheck(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> gameruleKeyDoDaylight, @Nullable MinecraftServer server) {
        // setup initial config if required (first tick of a new level)
        if (simpleDayLengthExtender_isFirstLevelTick) {
            if (MODPLATFORM.isTfcOverrideConfigured()) {
                doTfcUpdates();
            } else {
                simpleDayLengthExtender_dayTocker = SimpleDayLengthExtender.buildNewTockerDay(level.getLevelData());
                simpleDayLengthExtender_nightTocker = SimpleDayLengthExtender.buildNewTockerNight(level.getLevelData());
            }
            simpleDayLengthExtender_isFirstLevelTick = false;

            if (server != null) {
                simpleDayLengthExtender_disableCycleWhenEmpty = SimpleDayLengthExtender.shouldDisableCycleWhenEmpty();
                if (SimpleDayLengthExtender.CONFIG.delayTimeCycleUntilFirstJoin.get()) {
                    simpleDayLengthExtender_waitingForPlayer = true;
                    gameRules.getRule(gameruleKeyDoDaylight).set(false, server);
                    SimpleDayLengthExtender.LOGGER.info("World started, doDaylightCycle is delayed until first player join...");
                }
            }
        } else if (MODPLATFORM.isTfcOverrideConfigured() && level.getGameTime() % TFC_CHECK_INTERVAL == 0) {
            // TFC calendar-affected lengths need regular re-init
            if (MODPLATFORM.getTfcCalendarDay(level) > simpleDayLengthExtender_previousCalendarDay) {
                doTfcUpdates();
            }
        }

        // determine if we should tick or not
        final boolean doDaylightCycle;
        if (server != null) {
            // server
            if (simpleDayLengthExtender_waitingForPlayer) {
                // server was empty last tick...
                if (server.getPlayerCount() != 0) {
                    // ...but a player has now joined
                    SimpleDayLengthExtender.LOGGER.info("A player has joined, starting doDaylightCycle");
                    simpleDayLengthExtender_waitingForPlayer = false;
                    doDaylightCycle = true;
                } else {
                    // ...and is still empty this tick
                    doDaylightCycle = false;
                }
            } else {
                // server had a player last tick...
                if (simpleDayLengthExtender_disableCycleWhenEmpty && playersHaveAllLeft(server)) {
                    // ...but now there are no players and simpleDayLengthExtender_disableCycleWhenEmpty is enabled
                    SimpleDayLengthExtender.LOGGER.info("No players in server, setting doDaylightCycle to false");
                    simpleDayLengthExtender_waitingForPlayer = true;
                    doDaylightCycle = false;
                } else {
                    // ...and there still is a player, or simpleDayLengthExtender_disableCycleWhenEmpty is NOT enabled
                    doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(level, simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
                }
            }
        } else {
            // client can always check, no reason not to
            doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(level, simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
        }
        // Finally update the gamerule. Note that server is null on client, that's intentional
        gameRules.getRule(gameruleKeyDoDaylight).set(doDaylightCycle, server);
    }

    private void doTfcUpdates() {
        float dayRatio = MODPLATFORM.getTfcManagedRatio(level);
        simpleDayLengthExtender_dayTocker = MODPLATFORM.buildTfcManagedTocker(true, level, dayRatio);
        simpleDayLengthExtender_nightTocker = MODPLATFORM.buildTfcManagedTocker(false, level, dayRatio);
        simpleDayLengthExtender_previousCalendarDay = MODPLATFORM.getTfcCalendarDay(level);
    }

    private boolean playersHaveAllLeft(MinecraftServer server) {
        boolean allLeft = false;
        int playersNow = server.getPlayerCount();
        if (playersNow == 0 && simpleDayLengthExtender_playersLastCount != 0) {
            allLeft = true;
        }
        simpleDayLengthExtender_playersLastCount = playersNow;
        return allLeft;
    }
}
