package ovh.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ovh.cosmicdan.simpledaylengthextender.ModPlatformHelper;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

@Mixin(ClientLevel.class)
public abstract class ClientLevelHooks {
    @Unique
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    @Unique
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    @Unique
    private TimeTocker simpleDayLengthExtender_nightTocker = null;

    @Unique
    private long simpleDayLengthExtender_previousCalendarDay = 0;

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

        // manage TFC calendar-affected lengths
        if (ModPlatformHelper.isTfcOverrideConfigured() && ((Level)((Object)this)).getGameTime() % TFC_CHECK_INTERVAL == 0){
            Level level = ((Level)((Object)this));

            if (ModPlatformHelper.getTfcCalendarDay(level) > simpleDayLengthExtender_previousCalendarDay)
            {
                float dayRatio = ModPlatformHelper.getTfcManagedRatio(level);
                simpleDayLengthExtender_dayTocker = ModPlatformHelper.buildTfcManagedTocker(true, level, dayRatio);
                simpleDayLengthExtender_nightTocker = ModPlatformHelper.buildTfcManagedTocker(false, level, dayRatio);
                simpleDayLengthExtender_previousCalendarDay = ModPlatformHelper.getTfcCalendarDay(level);
            }
        }

        final boolean doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(getLevelData(), simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
        if (doDaylightCycle)
            return original.call(gameRules, gameruleKeyDoDaylight);
        else
            return false;
    }
}
