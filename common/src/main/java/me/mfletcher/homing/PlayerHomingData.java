package me.mfletcher.homing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class PlayerHomingData {
    private static final HashMap<Player, Boolean> isHoming = new HashMap<>();
    private static final HashMap<Player, Boolean> isBoosting = new HashMap<>();

    private static boolean statsRegistered = false;

    public static void initialize() {
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

    public static class CustomStats {
        public static final ResourceLocation HOMING_ATTACK_STAT = new ResourceLocation(HomingAttack.MOD_ID, "homing_attack_uses");

        public static void register() {
            Registry.register(BuiltInRegistries.CUSTOM_STAT, HOMING_ATTACK_STAT, HOMING_ATTACK_STAT);
            Stats.CUSTOM.get(HOMING_ATTACK_STAT, StatFormatter.DEFAULT);
        }
    }
}