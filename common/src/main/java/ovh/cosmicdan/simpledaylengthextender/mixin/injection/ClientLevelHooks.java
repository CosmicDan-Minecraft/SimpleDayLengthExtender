package ovh.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender;
import ovh.cosmicdan.simpledaylengthextender.TimeTocker;

import java.util.function.Supplier;

import static ovh.cosmicdan.simpledaylengthextender.SimpleDayLengthExtender.*;

@Mixin(ClientLevel.class)
public abstract class ClientLevelHooks extends Level {
    @Unique
    private boolean simpleDayLengthExtender_isFirstLevelTick = true;
    @Unique
    private TimeTocker simpleDayLengthExtender_dayTocker = null;
    @Unique
    private TimeTocker simpleDayLengthExtender_nightTocker = null;

    @Unique
    private long simpleDayLengthExtender_previousCalendarDay = 0;

    protected ClientLevelHooks(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

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
            if (MODPLATFORM.isTfcOverrideConfigured()) {
                float dayRatio = MODPLATFORM.getTfcManagedRatio(this);
                simpleDayLengthExtender_dayTocker = MODPLATFORM.buildTfcManagedTocker(true, this, dayRatio);
                simpleDayLengthExtender_nightTocker = MODPLATFORM.buildTfcManagedTocker(false, this, dayRatio);
                simpleDayLengthExtender_previousCalendarDay = MODPLATFORM.getTfcCalendarDay(this);
            } else {
                simpleDayLengthExtender_dayTocker = SimpleDayLengthExtender.buildNewTockerDay(getLevelData());
                simpleDayLengthExtender_nightTocker = SimpleDayLengthExtender.buildNewTockerNight(getLevelData());
            }
            simpleDayLengthExtender_isFirstLevelTick = false;
        } else {
            // manage TFC calendar-affected lengths
            if (MODPLATFORM.isTfcOverrideConfigured() && this.getGameTime() % TFC_CHECK_INTERVAL == 0 ) {
                if (MODPLATFORM.getTfcCalendarDay(this) > simpleDayLengthExtender_previousCalendarDay) {
                    float dayRatio = MODPLATFORM.getTfcManagedRatio(this);
                    simpleDayLengthExtender_dayTocker = MODPLATFORM.buildTfcManagedTocker(true, this, dayRatio);
                    simpleDayLengthExtender_nightTocker = MODPLATFORM.buildTfcManagedTocker(false, this, dayRatio);
                    simpleDayLengthExtender_previousCalendarDay = MODPLATFORM.getTfcCalendarDay(this);
                }
            }
        }

        final boolean doDaylightCycle = SimpleDayLengthExtender.shouldAllowDaylightProgression(getLevelData(), simpleDayLengthExtender_dayTocker, simpleDayLengthExtender_nightTocker);
        if (doDaylightCycle)
            return original.call(gameRules, gameruleKeyDoDaylight);
        else
            return false;
    }
}
