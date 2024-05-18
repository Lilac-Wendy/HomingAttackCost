package me.mfletcher.homing.mixin.mixins;

import com.mojang.authlib.GameProfile;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.sounds.HomingSounds;
import me.mfletcher.homing.PlayerHomingAttackInfo;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.mixin.access.IServerPlayerMixin;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.BoostS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IServerPlayerMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    public abstract void sendSystemMessage(Component pComponent);

    @Unique
    @Nullable
    private PlayerHomingAttackInfo homing$playerHomingAttackInfo = null;

    @Unique
    private final MobEffectInstance homing$speedEffect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20,
            HomingAttack.config.boostLevel, false, false, false);


    public ServerPlayerMixin(Level level, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(level, pos, yaw, gameProfile);
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (homing$playerHomingAttackInfo == null) {
            if (PlayerHomingData.isBoosting(this)) {
                addEffect(homing$speedEffect);
                causeFoodExhaustion(0.05F);
                super.travel(new Vec3(0, 0, 1));
            } else super.travel(movementInput);
        } else super.travel(movementInput);
    }

    @Inject(method = "setPlayerInput", at = @At("HEAD"), cancellable = true)
    public void onUpdateInput(CallbackInfo ci) {
        if (homing$playerHomingAttackInfo != null)
            ci.cancel();
    }

    @Unique
    public void homing$doHoming(Entity entity) {
        if (entity.distanceTo(this) <= HomingAttack.config.homingRange && homing$playerHomingAttackInfo == null) {
            level().playSound(null, blockPosition(), HomingSounds.HOMING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            homing$playerHomingAttackInfo = new PlayerHomingAttackInfo((ServerPlayer) (Player) this, entity);
        } else
            LOGGER.error("Homing attack failed: " + homing$playerHomingAttackInfo);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        if (homing$playerHomingAttackInfo != null)
            if (!homing$playerHomingAttackInfo.tick())
                homing$playerHomingAttackInfo = null;
    }

    @Unique
    public Entity homing$getHomingEntity() {
        if (homing$playerHomingAttackInfo != null)
            return homing$playerHomingAttackInfo.getTarget();
        return null;
    }

    @Unique
    public void homing$setBoosting(boolean boosting) {
        PlayerHomingData.setBoosting(this, boosting);
        if (!boosting)
            removeEffect(homing$speedEffect.getEffect());
        else
            level().playSound(null, blockPosition(), HomingSounds.BOOST.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
        for (Player p : level().players())
            if (p.distanceTo(this) < 128) {
                HomingMessages.sendToPlayer(new BoostS2CPacket(getId(), PlayerHomingData.isBoosting(this)), (ServerPlayer) p);
            }

    }
}
