package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import me.mfletcher.homing.client.animation.HomingAnimation;
import me.mfletcher.homing.mixin.access.IAbstractClientPlayerMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class DashRingAnimS2CPacket {
    private final int playerId;

    public DashRingAnimS2CPacket(int PlayerId) {
        this.playerId = PlayerId;
    }

    public DashRingAnimS2CPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.playerId);
    }

    public void apply(Supplier<NetworkManager.PacketContext> supplier) {
        NetworkManager.PacketContext context = supplier.get();
        context.queue(() -> {
            // Running on client
            assert Minecraft.getInstance().player != null;
            Player player = (Player) Minecraft.getInstance().player.level().getEntity(this.playerId);

            if (player instanceof AbstractClientPlayer) {
                HomingAnimation.playAnimation(((IAbstractClientPlayerMixin) player).homing$getAnimationLayer(), HomingAnimation.DASH_RING_ANIMATION);
            }
        });
    }
}
