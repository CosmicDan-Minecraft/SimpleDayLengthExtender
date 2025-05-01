package github.cosmicdan.simpledaylengthextender.mixin.injection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.cosmicdan.simpledaylengthextender.LevelTockHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class ClientLevelHooks {
    @Shadow
    private boolean tickDayTime;
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
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/multiplayer/ClientLevel;tickDayTime:Z")
    )
    public boolean onTickTimeDayCycleRuleCheck(ClientLevel instance, Operation<Boolean> original) {
        if (sdle_$tockHandler == null)
            sdle_$tockHandler = new LevelTockHandler((Level)((Object)this));
        // New to 1.21.5 - ClientLevel no longer uses the gamerule, has its own internal flag instead
        tickDayTime = sdle_$tockHandler.onTickTimeDayCycleRuleCheck(null, null, null);
        // always call original, we've already updated tickDayTime
        return original.call(instance);
    }
}
