package me.mfletcher.homing.mixinaccess;

public interface IAbstractClientPlayerMixin {

    void startHomingAnimation();

    void stopAnimations();

    void setBoosting(boolean boosting);
}
