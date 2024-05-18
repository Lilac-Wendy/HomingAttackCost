package me.mfletcher.homing.mixin.access;

import net.minecraft.world.entity.Entity;

public interface IMinecraftMixin {
    void homing$setHomingReady();

    void homing$setHomingUnready();

    Entity homing$getHighlightedEntity();

    boolean homing$isHomingReady();

}
