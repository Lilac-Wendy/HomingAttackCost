package me.mfletcher.homing.mixinaccess;

public interface IAbstractClientPlayerMixin {

    void startHomingAnimation();

    void stopAnimations();

    boolean isBoosting();

    void setBoosting(boolean boosting);
}
