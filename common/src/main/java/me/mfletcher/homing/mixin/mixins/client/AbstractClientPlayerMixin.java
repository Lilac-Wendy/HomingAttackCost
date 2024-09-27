package me.mfletcher.homing.mixin.mixins.client;

import me.mfletcher.homing.HomingAttack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

import class PlayerHomingData {
    private static final HashMap<Player, Boolean> isHoming = new HashMap<>();
    private static final HashMap<Player, Boolean> isBoosting = new HashMap<>();

    private static boolean statsRegistered = false;

    public static void initialize() {
        // Registrar estatísticas personalizadas, se ainda não estiverem registradas
        if (!statsRegistered) {
            CustomStats.register();
            statsRegistered = true;
        }
    }

    public static void setHoming(Player player, boolean isHoming) {
        PlayerHomingData.isHoming.put(player, isHoming);
    }

    public static boolean isHoming(Player player) {
        return isHoming.getOrDefault(player, false);
    }

    public static void setBoosting(Player player, boolean isBoosting) {
        PlayerHomingData.isBoosting.put(player, isBoosting);
    }

    public static boolean isBoosting(Player player) {
        return isBoosting.getOrDefault(player, false);
    }

    // Classe interna para registrar estatísticas personalizadas
    public static class CustomStats {
        public static final ResourceLocation HOMING_ATTACK_STAT = new ResourceLocation(HomingAttack.MOD_ID, "homing_attack_uses");

        public static void register() {
            // Registrar a estatística personalizada
            Registry.register(BuiltInRegistries.CUSTOM_STAT, HOMING_ATTACK_STAT, HOMING_ATTACK_STAT);
            // Tornar a estatística disponível com formatação padrão
            Stats.CUSTOM.get(HOMING_ATTACK_STAT, StatFormatter.DEFAULT);
        }
    }
}