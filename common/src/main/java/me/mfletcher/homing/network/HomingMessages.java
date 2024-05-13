package me.mfletcher.homing.network;

import dev.architectury.networking.NetworkChannel;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.network.protocol.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HomingMessages {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(HomingAttack.MOD_ID, "networking_channel"));

    public static void init() {
        // Register messages here
        CHANNEL.register(AttackC2SPacket.class, AttackC2SPacket::encode, AttackC2SPacket::new, AttackC2SPacket::apply);
        CHANNEL.register(AttackS2CPacket.class, AttackS2CPacket::encode, AttackS2CPacket::new, AttackS2CPacket::apply);
        CHANNEL.register(BoostC2SPacket.class, BoostC2SPacket::encode, BoostC2SPacket::new, BoostC2SPacket::apply);
        CHANNEL.register(BoostS2CPacket.class, BoostS2CPacket::encode, BoostS2CPacket::new, BoostS2CPacket::apply);
        CHANNEL.register(ConfigSyncS2CPacket.class, ConfigSyncS2CPacket::encode, ConfigSyncS2CPacket::new, ConfigSyncS2CPacket::apply);
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.sendToPlayer(player, message);
    }
}
