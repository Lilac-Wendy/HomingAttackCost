package me.mfletcher.homing.client.ability;

import me.mfletcher.homing.client.KeyMappings;
import me.mfletcher.homing.mixin.access.IMinecraftMixin;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.AttackC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class HomingAbility {
    public static void handleHoming() {
        if (KeyMappings.HOMING_KEY.consumeClick()) {
            Entity entity = ((IMinecraftMixin) Minecraft.getInstance()).homing$getHighlightedEntity();
            if (entity != null) {
                HomingMessages.sendToServer(new AttackC2SPacket(((IMinecraftMixin) Minecraft.getInstance()).homing$getHighlightedEntity().getId()));
                ((IMinecraftMixin) Minecraft.getInstance()).homing$setHomingUnready();
            }
        }
    }
}
