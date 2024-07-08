package ovh.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import ovh.cosmicdan.simpledaylengthextender.ModMain;

@Mixin(ServerLevel.class)
public abstract class ServerLevelHooks {

    @Shadow
    public abstract ServerLevel getLevel();

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
        final boolean isDoDaylightCycleRuleEnabled = original.call(gameRules, gameruleKeyDoDaylight);
        if (ModMain.shouldAllowDaylightProgression(getLevel().getLevelData()))
            return isDoDaylightCycleRuleEnabled;
        else
            return false;
    }
}
