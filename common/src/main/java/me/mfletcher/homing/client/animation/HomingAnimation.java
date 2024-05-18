package me.mfletcher.homing.client.animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.mfletcher.homing.HomingAttack;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomingAnimation {
    public static final ResourceLocation BOOST_ANIMATION = new ResourceLocation(HomingAttack.MOD_ID, "boost");
    public static final ResourceLocation SPINDASH_ANIMATION = new ResourceLocation(HomingAttack.MOD_ID, "spindash");
    public static final ResourceLocation DASH_RING_ANIMATION = new ResourceLocation(HomingAttack.MOD_ID, "dash_ring_spin");

    public static void register() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(HomingAttack.MOD_ID, "animation"),
                42,
                (abstractClientPlayer) -> new ModifierLayer<>());
    }

    public static void playAnimation(@NotNull ModifierLayer<IAnimation> modifierLayer, @NotNull ResourceLocation animation) {
        KeyframeAnimation anim = Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(animation));
        modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.CONSTANT), new KeyframeAnimationPlayer(anim)
                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
    }

    public static void stopAnimations(ModifierLayer<IAnimation> modifierLayer) {
        modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.CONSTANT), null);
    }
}
