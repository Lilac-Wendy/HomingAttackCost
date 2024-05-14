package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import me.mfletcher.homing.mixinaccess.IServerPlayerMixin;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class BoostC2SPacket {
    private final boolean isBoosting;

    public BoostC2SPacket(boolean isBoosting) {
        this.isBoosting = isBoosting;
    }

    public BoostC2SPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isBoosting);
    }

    public void apply(Supplier<NetworkManager.PacketContext> supplier) {
        NetworkManager.PacketContext context = supplier.get();
        context.queue(() -> {
            // Running on server
            ((IServerPlayerMixin) context.getPlayer()).homing$setBoosting(isBoosting);
        });
    }
}
