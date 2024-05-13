package me.mfletcher.homing;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import me.mfletcher.homing.mixinaccess.IAbstractClientPlayerMixin;
import me.mfletcher.homing.mixinaccess.IMinecraftMixin;
import me.mfletcher.homing.network.HomingMessages;
import me.mfletcher.homing.network.protocol.AttackC2SPacket;
import me.mfletcher.homing.network.protocol.BoostC2SPacket;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public final class HomingAttack {
    public static final String MOD_ID = "homing";

    public static ModConfig config;

    public static void init() {
        // Write common init code here.
        HomingMessages.init();
        HomingSounds.init();
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientLifecycleEvent.CLIENT_SETUP.register(client -> {
            KeyMappingRegistry.register(HomingConstants.HOMING_KEY);
            KeyMappingRegistry.register(HomingConstants.BOOST_KEY);

            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                    new ResourceLocation(HomingAttack.MOD_ID, "animation"),
                    42,
                    (abstractClientPlayer) -> new ModifierLayer<>());
        });

        ClientTickEvent.CLIENT_LEVEL_POST.register(minecraft -> {
            if (HomingConstants.HOMING_KEY.consumeClick()) {
                Entity entity = ((IMinecraftMixin) Minecraft.getInstance()).getHighlightedEntity();
                if (entity != null) {
                    HomingMessages.sendToServer(new AttackC2SPacket(((IMinecraftMixin) Minecraft.getInstance()).getHighlightedEntity().getId()));
                    ((IMinecraftMixin)Minecraft.getInstance()).setHomingUnready();
                }
            }
            if (HomingConstants.BOOST_KEY.isDown() && !PlayerHomingData.isBoosting(Minecraft.getInstance().player)
                    && Minecraft.getInstance().player.mainSupportingBlockPos.isPresent() && Minecraft.getInstance().player.getFoodData().getFoodLevel() > 6
                    && !Minecraft.getInstance().player.isUsingItem()) {
                HomingMessages.sendToServer(new BoostC2SPacket(true));
                ((IAbstractClientPlayerMixin) Minecraft.getInstance().player).setBoosting(true);
            } else if (PlayerHomingData.isBoosting(Minecraft.getInstance().player)
                    && (!HomingConstants.BOOST_KEY.isDown() || Minecraft.getInstance().player.getFoodData().getFoodLevel() <= 6
                    || Minecraft.getInstance().player.isUsingItem())) {
                HomingMessages.sendToServer(new BoostC2SPacket(false));
                ((IAbstractClientPlayerMixin) Minecraft.getInstance().player).setBoosting(false);
            }
        });
    }
}
