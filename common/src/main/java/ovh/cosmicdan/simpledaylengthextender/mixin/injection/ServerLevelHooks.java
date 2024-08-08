package ovh.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ovh.cosmicdan.simpledaylengthextender.ModPlatformHelper;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

@Mixin(ServerLevel.class)
public abstract class ServerLevelHooks {
    @Unique
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    @Unique
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    @Unique
    private TimeTocker simpleDayLengthExtender_nightTocker = null;
    @Unique
    private boolean simpleDayLengthExtender_disableCycleWhenEmpty = true;
    @Unique
    private boolean simpleDayLengthExtender_waitingForFirstPlayer = false;
    @Unique
    private int simpleDayLengthExtender_playersLastCount = 0;
    @Unique
    private long simpleDayLengthExtender_previousCalendarDay = 0;

    @Shadow
    public abstract ServerLevel getLevel();

    @Shadow
    public abstract @NotNull MinecraftServer getServer();

    /**
     *  Wraps the GameRules.RULE_DAYLIGHT check before incrementing day time, only proceeding if we allow it (based on
     *  the configured day/night length).<br/>
     *  <br/>
     *  Original code (example if unmodified vanilla):<pre>
     *      if (levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
     *          setDayTime(levelData.getDayTime() + 1L);
     *      }</pre>
     *
     *  Modified code (example if unmodified vanilla):<pre>
     *      if (onTickTimeDayCycleRuleCheck(levelData.getGameRules(), GameRules.RULE_DAYLIGHT, (args) -> {
     *          return ((GameRules)args[0]).getBoolean((GameRules.Key)args[1]);
     *      })) {
     *          setDayTime(levelData.getDayTime() + 1L);
     *      }</pre>
     *
     *  WrapOperation is "compatible" in the way that other WrapOperations on this specific point can be chained.
     *
     */
    @WrapOperation(
            method = "tickTime",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z")
    )
    public boolean onTickTimeDayCycleRuleCheck(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> gameruleKeyDoDaylight, Operation<Boolean> original) {
        // setup initial config if required (first tick of a new level)
        if (simpleDayLengthExtender_isFirstLevelTick) {
            simpleDayLengthExtender_isFirstLevelTick = false;
            simpleDayLengthExtender_dayTocker = SimpleDayLengthExtender.buildNewTockerDay(getLevel().getLevelData());
            simpleDayLengthExtender_nightTocker = SimpleDayLengthExtender.buildNewTockerNight(getLevel().getLevelData());
            simpleDayLengthExtender_disableCycleWhenEmpty = SimpleDayLengthExtender.shouldDisableCycleWhenEmtpy();
            if (SimpleDayLengthExtender.serverConfig.delayTimeCycleUntilFirstJoin.get()) {
                simpleDayLengthExtender_waitingForFirstPlayer = true;
                gameRules.getRule(gameruleKeyDoDaylight).set(false, getServer());
                SimpleDayLengthExtender.LOGGER.info("World started, doDaylightCycle is delayed until first player join...");
            }

        }

        // manage TFC calendar-affected lengths
        if (ModPlatformHelper.isTfcOverrideConfigured() && getLevel().getGameTime() % TFC_CHECK_INTERVAL == 0){
            if (ModPlatformHelper.getTfcCalendarDay(getLevel()) > simpleDayLengthExtender_previousCalendarDay)
            {
                float dayRatio = ModPlatformHelper.getTfcManagedRatio(getLevel());
                simpleDayLengthExtender_dayTocker = ModPlatformHelper.buildTfcManagedTocker(true, getLevel(), dayRatio);
                simpleDayLengthExtender_nightTocker = ModPlatformHelper.buildTfcManagedTocker(false, getLevel(), dayRatio);
                simpleDayLengthExtender_previousCalendarDay = ModPlatformHelper.getTfcCalendarDay(getLevel());
            }
        }

        if (simpleDayLengthExtender_waitingForFirstPlayer) {
            if (getServer().getPlayerCount() != 0) {
                // first player joined
                simpleDayLengthExtender_waitingForFirstPlayer = false;
                SimpleDayLengthExtender.LOGGER.info("A player has joined, starting doDaylightCycle");
            }
        }

        if (!simpleDayLengthExtender_waitingForFirstPlayer) {
            if (simpleDayLengthExtender_disableCycleWhenEmpty && playersHaveAllLeft()) {
                // disableCycleWhenEmpty is set and all players have left since last tick
                gameRules.getRule(gameruleKeyDoDaylight).set(false, getServer());
                SimpleDayLengthExtender.LOGGER.info("All players left, setting doDaylightCycle to false");
                simpleDayLengthExtender_waitingForFirstPlayer = true;
            } else {
                final boolean doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(getLevel().getLevelData(), simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
                gameRules.getRule(gameruleKeyDoDaylight).set(doDaylightCycle, getServer());
            }
        }

        return original.call(gameRules, gameruleKeyDoDaylight);
    }

    private boolean playersHaveAllLeft() {
        boolean allLeft = false;
        int playersNow = getServer().getPlayerCount();
        if (playersNow == 0 && simpleDayLengthExtender_playersLastCount != 0) {
            allLeft = true;
        }
        simpleDayLengthExtender_playersLastCount = playersNow;
        return allLeft;
    }
}
