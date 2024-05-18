package me.mfletcher.homing.network.protocol;

import dev.architectury.networking.NetworkManager;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.PlayerHomingData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
            Player player = context.getPlayer();

            var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(HomingAttack.MOD_ID, "animation"));
            if (animation != null) {
                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(HomingAttack.MOD_ID, "dash_ring_spin")))
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
            }
        });
    }
}
