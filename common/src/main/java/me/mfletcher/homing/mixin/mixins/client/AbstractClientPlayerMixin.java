package me.mfletcher.homing.mixin.mixins.client;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.client.animation.HomingAnimation;
import me.mfletcher.homing.mixin.access.IAbstractClientPlayerMixin;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IAbstractClientPlayerMixin {
    @Shadow
    @Final
    public ClientLevel clientLevel;

    @Unique
    private final ModifierLayer<IAnimation> homing$animationContainer = new ModifierLayer<>();

//    @Unique
//    private final SoundInstance homing$boostSound = new SimpleSoundInstance(HomingSounds.BOOST_2.get(), SoundSource.PLAYERS, 0.5f, 1, SoundInstance.createUnseededRandom(), blockPosition());

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(level, pos, yaw, gameProfile);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel level, GameProfile profile, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1000, homing$animationContainer); //Register the layer with a priority
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (PlayerHomingData.isHoming(this))
            clientLevel.addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 0, 0, 0);
    }

    @Unique
    public void homing$startHomingAnimation() {
        HomingAnimation.playAnimation(homing$animationContainer, HomingAnimation.SPINDASH_ANIMATION);
    }

    @Unique
    public void homing$startBoostAnimation() {
        HomingAnimation.playAnimation(homing$animationContainer, HomingAnimation.BOOST_ANIMATION);
    }

    @Unique
    public void homing$stopAnimations() {
        HomingAnimation.stopAnimations(homing$animationContainer);
    }

    @Unique
    public void homing$setBoosting(boolean boosting) {
        if (PlayerHomingData.isBoosting(this) != boosting) {
            if (boosting) {
                homing$startBoostAnimation();
            } else {
                homing$stopAnimations();
            }
        }


        PlayerHomingData.setBoosting(this, boosting);

//        if (this.equals(Minecraft.getInstance().player)) {
//            if (boosting)
//                Minecraft.getInstance().getSoundManager().play(homing$boostSound);
//            else
//                Minecraft.getInstance().getSoundManager().stop(homing$boostSound);
//        }
    }

    @Unique
    public ModifierLayer<IAnimation> homing$getAnimationLayer() {
        return homing$animationContainer;
    }
}
