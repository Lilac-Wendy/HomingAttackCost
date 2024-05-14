package me.mfletcher.homing.mixinaccess;

import net.minecraft.world.entity.Entity;

public interface IServerPlayerMixin {
    void doHoming(Entity entity);

    Entity getHomingEntity();

    void setBoosting(boolean boosting);
}
