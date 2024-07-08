package ovh.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

@Mixin(ClientLevel.class)
public abstract class ClientLevelHooks {
    @Unique
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    @Unique
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    @Unique
    private TimeTocker simpleDayLengthExtender_nightTocker = null;

    @Shadow
    public abstract LevelData getLevelData();

    /**
     * Basically identical as the ServerLevel hook but for ClientLevel :)
     *
     * Refer to: {@link ServerLevelHooks#onTickTimeDayCycleRuleCheck(GameRules, GameRules.Key, Operation)}
     *
     */
    @WrapOperation(
            method = "tickTime",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z")
    )
    public boolean onTickTimeDayCycleRuleCheck(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> gameruleKeyDoDaylight, Operation<Boolean> original) {
        if (simpleDayLengthExtender_isFirstLevelTick) {
            simpleDayLengthExtender_isFirstLevelTick = false;
            simpleDayLengthExtender_dayTocker = SimpleDayLengthExtender.buildNewTockerDay(getLevelData());
            simpleDayLengthExtender_nightTocker = SimpleDayLengthExtender.buildNewTockerNight(getLevelData());
        }

        final boolean doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(getLevelData(), simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
        if (doDaylightCycle)
            return original.call(gameRules, gameruleKeyDoDaylight);
        else
            return false;
    }
}
