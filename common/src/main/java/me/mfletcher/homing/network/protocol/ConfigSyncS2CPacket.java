package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.ModConfig;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class ConfigSyncS2CPacket {
    private final int homingRange;

    public ConfigSyncS2CPacket(ModConfig config) {
        this.homingRange = config.homingRange;
    }

    public ConfigSyncS2CPacket(FriendlyByteBuf buf) {
        homingRange = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(homingRange);
    }

    public void apply(Supplier<NetworkManager.PacketContext> supplier) {
        NetworkManager.PacketContext context = supplier.get();
        context.queue(() -> {
            // Running on client
            HomingAttack.config.homingRange = this.homingRange;
        });
    }
}
