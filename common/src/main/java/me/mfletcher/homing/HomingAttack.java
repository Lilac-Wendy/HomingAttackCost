package me.mfletcher.homing;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.mfletcher.homing.block.HomingBlocks;
import me.mfletcher.homing.client.animation.HomingAnimation;
import me.mfletcher.homing.item.HomingCreativeTabs;
import me.mfletcher.homing.item.HomingItems;
import me.mfletcher.homing.mixin.access.IAbstractClientPlayerMixin;
import me.mfletcher.homing.mixin.access.IMinecraftMixin;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.AttackC2SPacket;
import me.mfletcher.homing.network.protocol.BoostC2SPacket;
import me.mfletcher.homing.network.protocol.ConfigSyncS2CPacket;
import me.mfletcher.homing.sounds.HomingSounds;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

public final class HomingAttack {
    public static final String MOD_ID = "homing";

    public static ModConfig config;

    public static void init() {
        // Write common init code here.
        HomingMessages.register();
        HomingSounds.register();
        HomingBlocks.register();
        HomingItems.register();
        HomingCreativeTabs.register();
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientLifecycleEvent.CLIENT_SETUP.register(client -> {
            KeyMappingRegistry.register(HomingConstants.HOMING_KEY);
            KeyMappingRegistry.register(HomingConstants.BOOST_KEY);

            HomingAnimation.register();
        });

        PlayerEvent.PLAYER_JOIN.register(localPlayer -> HomingMessages.sendToPlayer(new ConfigSyncS2CPacket(config), localPlayer));

        ClientTickEvent.CLIENT_LEVEL_POST.register(minecraft -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            if (HomingConstants.HOMING_KEY.consumeClick()) {
                Entity entity = ((IMinecraftMixin) Minecraft.getInstance()).homing$getHighlightedEntity();
                if (entity != null) {
                    HomingMessages.sendToServer(new AttackC2SPacket(((IMinecraftMixin) Minecraft.getInstance()).homing$getHighlightedEntity().getId()));
                    ((IMinecraftMixin) Minecraft.getInstance()).homing$setHomingUnready();
                }
            }
            if (HomingConstants.BOOST_KEY.isDown() && !PlayerHomingData.isBoosting(player)
                    && player.mainSupportingBlockPos.isPresent() && player.getFoodData().getFoodLevel() > 6
                    && !player.isUsingItem()) {
                HomingMessages.sendToServer(new BoostC2SPacket(true));
                ((IAbstractClientPlayerMixin) player).homing$setBoosting(true);
            } else if (PlayerHomingData.isBoosting(player)
                    && (!HomingConstants.BOOST_KEY.isDown() || player.getFoodData().getFoodLevel() <= 6
                    || player.isUsingItem())) {
                HomingMessages.sendToServer(new BoostC2SPacket(false));
                ((IAbstractClientPlayerMixin) player).homing$setBoosting(false);
            }
        });
    }
}
