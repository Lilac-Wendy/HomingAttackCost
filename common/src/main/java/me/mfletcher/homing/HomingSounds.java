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
    public static ResourceLocation BOOST_SOUND_LOCATION = new ResourceLocation(HomingAttack.MOD_ID, "boost");
    public static ResourceLocation HOMING_SOUND_LOCATION = new ResourceLocation(HomingAttack.MOD_ID, "homing");
    public static ResourceLocation RETICLE_SOUND_LOCATION = new ResourceLocation(HomingAttack.MOD_ID, "reticle");

    public static RegistrySupplier<SoundEvent> boostSound;
    public static RegistrySupplier<SoundEvent> homingSound;
    public static RegistrySupplier<SoundEvent> reticleSound;

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(HomingAttack.MOD_ID));

    public static void init() {
        Registrar<SoundEvent> sounds = MANAGER.get().get(Registries.SOUND_EVENT);
        boostSound = sounds.register(BOOST_SOUND_LOCATION, () -> SoundEvent.createVariableRangeEvent(BOOST_SOUND_LOCATION));
        homingSound = sounds.register(HOMING_SOUND_LOCATION, () -> SoundEvent.createVariableRangeEvent(HOMING_SOUND_LOCATION));
        reticleSound = sounds.register(RETICLE_SOUND_LOCATION, () -> SoundEvent.createVariableRangeEvent(RETICLE_SOUND_LOCATION));
    }
}
