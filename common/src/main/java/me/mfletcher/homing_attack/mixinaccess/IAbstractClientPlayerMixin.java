package me.mfletcher.homing_attack.mixinaccess;

public interface IAbstractClientPlayerMixin {

    void startHomingAnimation();

    void stopAnimations();

    boolean isBoosting();

    void setBoosting(boolean boosting);
}
