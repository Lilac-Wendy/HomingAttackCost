package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import me.mfletcher.homing.mixinaccess.IAbstractClientPlayerMixin;
import me.mfletcher.homing.mixinaccess.IMinecraftMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class AttackS2CPacket {
    private final int homingPlayerId;
    private final boolean isHoming;

    public AttackS2CPacket(int homingPlayerId, boolean isHoming) {
        this.homingPlayerId = homingPlayerId;
        this.isHoming = isHoming;
    }

    public AttackS2CPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.homingPlayerId);
        buf.writeBoolean(this.isHoming);
    }

    public void apply(Supplier<NetworkManager.PacketContext> supplier) {
        NetworkManager.PacketContext context = supplier.get();
        context.queue(() -> {
            // Running on client
            assert Minecraft.getInstance().level != null;
            Player homingPlayer = (Player) Minecraft.getInstance().level.getEntity(this.homingPlayerId);

            if (homingPlayer == null || Minecraft.getInstance().player == null) return;
            if (Minecraft.getInstance().player.equals(homingPlayer) && !isHoming)
                ((IMinecraftMixin) Minecraft.getInstance()).homing$setHomingReady();

            if (isHoming) ((IAbstractClientPlayerMixin) homingPlayer).homing$startHomingAnimation();
            else
                ((IAbstractClientPlayerMixin) homingPlayer).homing$stopAnimations();

        });
    }
}
