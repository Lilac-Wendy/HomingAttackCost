package me.mfletcher.homing_attack.mixinaccess;

import net.minecraft.world.entity.Entity;

public interface IMinecraftMixin {
    void setHomingReady();

    void setHomingUnready();

    Entity getHighlightedEntity();

}
