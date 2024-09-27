package me.mfletcher.homing.sounds;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import me.mfletcher.homing.PlayerHomingData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;


public class HomingSounds {
    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(PlayerHomingData.MOD_ID));
    public static final Registrar<SoundEvent> SOUND_EVENTS = MANAGER.get().get(Registries.SOUND_EVENT);


    public static RegistrySupplier<SoundEvent> BOOST;
    public static RegistrySupplier<SoundEvent> BOOST_2;
    public static RegistrySupplier<SoundEvent> HOMING;
    public static RegistrySupplier<SoundEvent> RETICLE;
    public static RegistrySupplier<SoundEvent> DASH_PANEL;
    public static RegistrySupplier<SoundEvent> DASH_RING;
    public static RegistrySupplier<SoundEvent> SPRING;

    public static void register() {
        BOOST = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "boost"));
        BOOST_2 = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "boost_2"));
        HOMING = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "homing"));
        RETICLE = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "reticle"));
        DASH_PANEL = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "dash_panel"));
        DASH_RING = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "dash_ring"));
        SPRING = registerSound(new ResourceLocation(PlayerHomingData.MOD_ID, "spring"));
    }

    private static RegistrySupplier<SoundEvent> registerSound(ResourceLocation id) {
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
