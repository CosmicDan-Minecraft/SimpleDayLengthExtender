package github.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.simpledaylengthextender.LevelTockHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class ClientLevelHooks {
    @Unique
    LevelTockHandler sdle_$tockHandler;
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
        if (sdle_$tockHandler == null) {
            System.out.println("~ Created new TockHandler on client");
            sdle_$tockHandler = new LevelTockHandler((Level)((Object)this));
        }
        sdle_$tockHandler.onTickTimeDayCycleRuleCheck(gameRules, gameruleKeyDoDaylight, null);
        // always call original, our LevelTockHandler updated gameruleKeyDoDaylight
        return original.call(gameRules, gameruleKeyDoDaylight);
    }
}
