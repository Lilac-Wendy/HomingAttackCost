package me.mfletcher.homing.client.ability;

import me.mfletcher.homing.client.KeyMappings;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.mixin.access.IAbstractClientPlayerMixin;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.BoostC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class BoostAbility {
    public static void handleBoost() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (shouldStartBoost(player)) {
            HomingMessages.sendToServer(new BoostC2SPacket(true));
            ((IAbstractClientPlayerMixin) player).homing$setBoosting(true);
        } else if (shouldStopBoost(player)) {
            HomingMessages.sendToServer(new BoostC2SPacket(false));
            ((IAbstractClientPlayerMixin) player).homing$setBoosting(false);
        }
    }

    private static boolean shouldStartBoost(@NotNull LocalPlayer player) {
        return KeyMappings.BOOST_KEY.isDown() && !PlayerHomingData.isBoosting(player)
                && player.mainSupportingBlockPos.isPresent() && player.getFoodData().getFoodLevel() > 6
                && !player.isUsingItem();
    }

    private static boolean shouldStopBoost(@NotNull LocalPlayer player) {
        return PlayerHomingData.isBoosting(player)
                && (!KeyMappings.BOOST_KEY.isDown() || player.getFoodData().getFoodLevel() <= 6
                || player.isUsingItem());
    }

}
