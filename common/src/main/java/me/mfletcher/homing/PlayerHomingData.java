package me.mfletcher.homing;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class PlayerHomingData {
    private static final HashMap<Player, Boolean> isHoming = new HashMap<>();
    private static final HashMap<Player, Boolean> isBoosting = new HashMap<>();

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
}
