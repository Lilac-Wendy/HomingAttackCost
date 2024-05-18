package me.mfletcher.homing.mixin.access;

public interface IAbstractClientPlayerMixin {

    void homing$startHomingAnimation();

    void homing$stopAnimations();

    void homing$setBoosting(boolean boosting);
}
