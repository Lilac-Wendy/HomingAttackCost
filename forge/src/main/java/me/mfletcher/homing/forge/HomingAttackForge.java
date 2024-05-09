package me.mfletcher.homing.forge;

import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import dev.architectury.platform.forge.EventBuses;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.PlayerHomingData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HomingAttack.MOD_ID)
public final class HomingAttackForge {
    public HomingAttackForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(HomingAttack.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        HomingAttack.init();

        MinecraftForge.EVENT_BUS.addListener(HomingAttackForge::onFluidCollision);
    }

    private static void onFluidCollision(LivingFluidCollisionEvent event) {
        Entity entity = event.getEntity();
        FluidState fluidState = event.getFluidState();
        if (entity instanceof Player player) {
            if (PlayerHomingData.isBoosting(player) && !player.isUsingItem() && !player.isCrouching() && !player.isInWater() && !player.isSwimming()) {
                if (fluidState.is(FluidTags.LAVA) && !player.fireImmune() && !EnchantmentHelper.hasFrostWalker(player)) {
                    player.hurt(player.damageSources().hotFloor(), 1);
                }
                player.level().addParticle(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 0, 3, 0);
                event.setResult(Event.Result.ALLOW);
            } else
                event.setResult(Event.Result.DENY);
        } else
            event.setResult(Event.Result.DENY);
    }
}
