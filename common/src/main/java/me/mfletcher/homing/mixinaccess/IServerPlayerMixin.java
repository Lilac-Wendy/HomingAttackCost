package me.mfletcher.homing.mixinaccess;

import net.minecraft.world.entity.Entity;

public interface IServerPlayerMixin {
    void homing$doHoming(Entity entity);

    Entity homing$getHomingEntity();

    void homing$setBoosting(boolean boosting);
}
