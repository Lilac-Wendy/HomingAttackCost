package me.mfletcher.homing_attack.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.mfletcher.homing_attack.HomingAttack;
import me.mfletcher.homing_attack.mixinaccess.IAbstractClientPlayerMixin;
import me.mfletcher.homing_attack.mixinaccess.IKeyboardInputMixin;
import net.minecraft.client.Minecraft;
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

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IAbstractClientPlayerMixin {
    @Shadow
    @Final
    public ClientLevel clientLevel;

    @Unique
    private boolean isHoming = false;

    @Unique
    private boolean isBoosting;

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(level, pos, yaw, gameProfile);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isHoming)
            clientLevel.addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 0, 0, 0);
    }

    @Unique
    public void startHomingAnimation() {
        System.out.println("Starting homing animation");
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(HomingAttack.MOD_ID, "spindash")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
            isHoming = true;
        }
    }

    @Unique
    public void startBoostAnimation() {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(HomingAttack.MOD_ID, "boost")))
                    .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
        }
    }

    @Unique
    public void stopAnimations() {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) (Object) this).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
        if (animation != null) {
            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.OUTEXPO), null);
        }
        isHoming = false;
    }

    @Unique
    public boolean isBoosting() {
        return isBoosting;
    }

    @Unique
    public void setBoosting(boolean boosting) {
        if (isBoosting != boosting) {
            if (boosting) {
                startBoostAnimation();
            } else {
                stopAnimations();
            }
        }


        isBoosting = boosting;

        if (this.equals(Minecraft.getInstance().player)) {
            ((IKeyboardInputMixin) Minecraft.getInstance().player.input).setBoosting(isBoosting);
        }
    }
}
