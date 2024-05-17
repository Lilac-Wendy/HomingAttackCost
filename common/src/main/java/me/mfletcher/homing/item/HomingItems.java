package me.mfletcher.homing.item;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import me.mfletcher.homing.HomingAttack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class HomingItems {
    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(HomingAttack.MOD_ID));
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);

    public static final RegistrySupplier<Item> CHAOS_EMERALDS = ITEMS.register(new ResourceLocation(HomingAttack.MOD_ID, "chaos_emeralds"),
            () -> new Item(new Item.Properties().arch$tab(HomingCreativeTabs.MY_TAB)));

    public static void register() {}
}
