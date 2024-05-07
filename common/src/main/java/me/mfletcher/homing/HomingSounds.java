package me.mfletcher.homing;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;


public class HomingSounds {
    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(HomingAttack.MOD_ID));
    public static RegistrySupplier<SoundEvent> BOOST;
    public static RegistrySupplier<SoundEvent> HOMING;

    public static void init() {
        Registrar<SoundEvent> soundEvents = MANAGER.get().get(Registries.SOUND_EVENT);
        BOOST = soundEvents.register(new ResourceLocation(HomingAttack.MOD_ID, "boost"), () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HomingAttack.MOD_ID, "boost")));
        HOMING = soundEvents.register(new ResourceLocation(HomingAttack.MOD_ID, "homing"), () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HomingAttack.MOD_ID, "homing")));
    }
}
