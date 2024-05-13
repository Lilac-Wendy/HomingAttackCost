package me.mfletcher.homing.mixinaccess;

import net.minecraft.world.entity.Entity;

public interface IMinecraftMixin {
    void setHomingReady();

    void setHomingUnready();

    Entity getHighlightedEntity();

    boolean isHomingReady();

}
