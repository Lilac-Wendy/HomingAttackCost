package me.mfletcher.homing.fabric;

import be.florens.expandability.api.fabric.LivingFluidCollisionCallback;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.data.PlayerHomingData;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

import java.io.IOException;

public final class HomingAttackFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        HomingAttack.init();

        LivingFluidCollisionCallback.EVENT.register(HomingAttackFabric::onFluidCollision);
    }

    private static boolean onFluidCollision(LivingEntity entity, FluidState fluidState) {
        if (entity instanceof Player player) {
            if (PlayerHomingData.isBoosting(player) && !player.isUsingItem() && !player.isCrouching() && !player.isInWater() && !player.isSwimming()) {
                if (fluidState.is(FluidTags.LAVA) && !player.fireImmune() && !EnchantmentHelper.hasFrostWalker(player)) {
                    player.hurt(player.damageSources().hotFloor(), 1);
                }
                try (Level level = player.level()) {
                    level.addParticle(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 0, 3, 0);
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }
        return false;
    }
}
