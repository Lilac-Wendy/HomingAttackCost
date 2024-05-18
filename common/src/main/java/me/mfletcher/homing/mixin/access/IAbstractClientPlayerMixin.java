package me.mfletcher.homing.mixin.access;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface IAbstractClientPlayerMixin {

    void homing$startHomingAnimation();

    void homing$stopAnimations();

    void homing$setBoosting(boolean boosting);

    ModifierLayer<IAnimation> homing$getAnimationLayer();
}
