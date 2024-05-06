package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import me.mfletcher.homing.mixinaccess.IAbstractClientPlayerMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class BoostS2CPacket {
    private final int boostPlayerId;
    private final boolean isBoosting;

    public BoostS2CPacket(int boostPlayerId, boolean isBoosting) {
        this.boostPlayerId = boostPlayerId;
        this.isBoosting = isBoosting;
    }

    public BoostS2CPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.boostPlayerId);
        buf.writeBoolean(this.isBoosting);
    }

    public void apply(Supplier<NetworkManager.PacketContext> supplier) {
        NetworkManager.PacketContext context = supplier.get();
        context.queue(() -> {
            // Running on client
            Player boostPlayer = (Player) Minecraft.getInstance().level.getEntity(this.boostPlayerId);
            if (boostPlayer == null || Minecraft.getInstance().player == null) return;
            ((IAbstractClientPlayerMixin) boostPlayer).setBoosting(isBoosting);
        });
    }
}
