package me.mfletcher.homing_attack.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import me.mfletcher.homing_attack.HomingAttack;

@Mod(HomingAttack.MOD_ID)
public final class HomingAttackForge {
    public HomingAttackForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(HomingAttack.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        HomingAttack.init();
    }
}
