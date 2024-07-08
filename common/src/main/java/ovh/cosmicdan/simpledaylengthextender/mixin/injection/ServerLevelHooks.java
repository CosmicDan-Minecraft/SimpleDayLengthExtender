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
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

@Mixin(ServerLevel.class)
public abstract class ServerLevelHooks {
    @Unique
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    @Unique
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    @Unique
    private TimeTocker simpleDayLengthExtender_nightTocker = null;

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
        // TODO: Change it to actually change the gamerule. TFC complains otherwise :\
        if (simpleDayLengthExtender_isFirstLevelTick) {
            simpleDayLengthExtender_isFirstLevelTick = false;
            simpleDayLengthExtender_dayTocker = SimpleDayLengthExtender.buildNewTockerDay(getLevel().getLevelData());
            simpleDayLengthExtender_nightTocker = SimpleDayLengthExtender.buildNewTockerNight(getLevel().getLevelData());
        }

        final boolean doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(getLevel().getLevelData(), simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
        // TODO: If TFC has the doDaylightCycle-disable-when-no-players config enabled, if so only call this if player count > 0
        gameRules.getRule(gameruleKeyDoDaylight).set(doDaylightCycle, getServer());
        return original.call(gameRules, gameruleKeyDoDaylight);
    }
}
