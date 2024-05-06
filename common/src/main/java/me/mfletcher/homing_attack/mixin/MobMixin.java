package me.mfletcher.homing_attack.mixin;

import me.mfletcher.homing_attack.mixinaccess.IServerPlayerMixin;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements Targeting {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
    public void onTryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof ServerPlayer
//                && ((Player) target).getArmorValue() != 0
                && this.equals(((IServerPlayerMixin) target).getHomingEntity()))
            cir.setReturnValue(false);
    }
}
