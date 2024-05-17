package me.mfletcher.homing.block;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.item.HomingCreativeTabs;
import me.mfletcher.homing.item.HomingItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class HomingBlocks {
    private static <T extends Block> RegistrySupplier<T> registerBlock(ResourceLocation name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<Item> registerBlockItem(ResourceLocation name, RegistrySupplier<T> block) {
        return HomingItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().arch$tab(HomingCreativeTabs.MY_TAB)));
    }

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(HomingAttack.MOD_ID));
    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);

    public static final RegistrySupplier<Block> DASH_PANEL = registerBlock(new ResourceLocation(HomingAttack.MOD_ID, "dash_panel"),
            () -> new DashPanelBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.STONE).noCollission()));

    public static void register() {}
}
