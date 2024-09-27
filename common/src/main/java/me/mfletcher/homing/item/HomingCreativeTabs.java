package me.mfletcher.homing.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.mfletcher.homing.PlayerHomingData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class HomingCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(PlayerHomingData.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MY_TAB = TABS.register(
            "main",
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.homing.main"),
                    () -> new ItemStack(HomingItems.CHAOS_EMERALDS.get())
            )
    );

    public static void register() {
        TABS.register();
    }
}

