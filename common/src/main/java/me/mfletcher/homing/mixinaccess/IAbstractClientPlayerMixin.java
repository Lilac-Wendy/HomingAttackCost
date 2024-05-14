package me.mfletcher.homing.mixinaccess;

public interface IAbstractClientPlayerMixin {

    void homing$startHomingAnimation();

    void homing$stopAnimations();

    void homing$setBoosting(boolean boosting);
}
