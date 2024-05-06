package me.mfletcher.homing_attack.fabric;

import net.fabricmc.api.ModInitializer;

import me.mfletcher.homing_attack.HomingAttack;

public final class HomingAttackFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        HomingAttack.init();
    }
}
