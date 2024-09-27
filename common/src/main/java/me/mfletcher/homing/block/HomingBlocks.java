package me.mfletcher.homing.block;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.item.HomingCreativeTabs;
import me.mfletcher.homing.item.HomingItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class HomingBlocks {
    private static <T extends Block> RegistrySupplier<T> registerBlock(ResourceLocation name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <T extends Block> void registerBlockItem(ResourceLocation name, RegistrySupplier<T> block) {
        HomingItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().arch$tab(HomingCreativeTabs.MY_TAB)));
    }

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(PlayerHomingData.MOD_ID));
    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);

    public static final RegistrySupplier<Block> DASH_PANEL = registerBlock(new ResourceLocation(PlayerHomingData.MOD_ID, "dash_panel"),
            () -> new DashPanelBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.STONE).noCollission()));
    public static final RegistrySupplier<Block> DASH_RING = registerBlock(new ResourceLocation(PlayerHomingData.MOD_ID, "dash_ring"),
            () -> new DashRingBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.STONE).noCollission()));
    public static final RegistrySupplier<Block> SPRING = registerBlock(new ResourceLocation(PlayerHomingData.MOD_ID, "spring"),
            () -> new SpringBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.STONE).noCollission()));

    public static void register() {}
}
