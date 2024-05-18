package me.mfletcher.homing.mixin.mixins.client;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.mixin.access.IAbstractClientPlayerMixin;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IAbstractClientPlayerMixin {
    @Shadow
    @Final
    public ClientLevel clientLevel;

//    @Unique
//    private final SoundInstance homing$boostSound = new SimpleSoundInstance(HomingSounds.BOOST_2.get(), SoundSource.PLAYERS, 0.5f, 1, SoundInstance.createUnseededRandom(), blockPosition());

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(level, pos, yaw, gameProfile);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (PlayerHomingData.isHoming(this))
            clientLevel.addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 0, 0, 0);
    }

    @SuppressWarnings({"unchecked", "UnreachableCode"})
    @Unique
    public void homing$startHomingAnimation() {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(HomingAttack.MOD_ID, "spindash"))))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
        }
    }

    @SuppressWarnings({"unchecked", "UnreachableCode"})
    @Unique
    public void homing$startBoostAnimation() {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(HomingAttack.MOD_ID, "boost"))))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
        }
    }

    @SuppressWarnings({"unchecked", "UnreachableCode"})
    @Unique
    public void homing$stopAnimations() {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.OUTEXPO), null);
        }
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
}
