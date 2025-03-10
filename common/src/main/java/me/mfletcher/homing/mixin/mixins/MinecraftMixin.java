package me.mfletcher.homing.mixin.mixins;

import com.mojang.blaze3d.platform.WindowEventHandler;
import me.mfletcher.homing.HomingAttack;
import me.mfletcher.homing.mixin.access.IMinecraftMixin;
import me.mfletcher.homing.sounds.HomingSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler, IMinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    public MinecraftMixin(String string) {
        super(string);
    }

    @Shadow
    public Entity getCameraEntity() {
        return null;
    }

    @Shadow
    public SoundManager getSoundManager() {
        return null;
    }


//    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
//    public void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
//        if (entity.equals(getHighlightedEntity())) cir.setReturnValue(true);
//    }

    @Unique
    private Entity homing$highlightedEntity;

    @Unique
    private boolean homing$homingReady;

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        if (player == null) return;
        if (!player.isSpectator() && !player.isPassenger()) {
            if (!player.onGround()) {
                if (homing$homingReady) {
                    Entity entityLooking = homing$getEntityLooking();
                    if (entityLooking != null && !entityLooking.equals(homing$getHighlightedEntity()) && HomingAttack.config.reticleVolume > 0) {
                        getSoundManager().play(new SimpleSoundInstance(HomingSounds.RETICLE.get(), SoundSource.PLAYERS, HomingAttack.config.reticleVolume / 100f, 1, SoundInstance.createUnseededRandom(), player.blockPosition()));
                    }
                    homing$setHighlightedEntity(entityLooking);
                } else
                    homing$setHighlightedEntity(null);
            } else {
                homing$setHighlightedEntity(null);
                homing$setHomingReady();
            }
        } else
            homing$setHighlightedEntity(null);
    }

    @Unique
    private Entity homing$getEntityLooking() {
        // This function is "heavily inspired" by GameRenderer#updateTargetedEntity
        float homingRange = HomingAttack.config.homingRange;

        Entity camera = getCameraEntity();
        Vec3 vec32 = camera.getViewVector(1.0f);
        Vec3 vec3 = camera.getEyePosition(1.0f);
        Vec3 vec33 = vec3.add(vec32.x * homingRange, vec32.y * homingRange, vec32.z * homingRange);
        AABB box = camera.getBoundingBox().expandTowards(vec32.scale(homingRange)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(camera, vec3, vec33, box,
                entity -> !entity.isSpectator() && entity.isPickable(), homingRange * homingRange);
        if (entityHitResult != null && entityHitResult.getEntity().isAlive()) {
            assert player != null;
            if (player.hasLineOfSight(entityHitResult.getEntity())) {
                return entityHitResult.getEntity();
            }
        }
        return null;
    }

    @Unique
    public Entity homing$getHighlightedEntity() {
        return homing$highlightedEntity;
    }

    @Unique
    public void homing$setHighlightedEntity(Entity highlightedEntity) {
        this.homing$highlightedEntity = highlightedEntity;
    }

    @Unique
    public void homing$setHomingUnready() {
        homing$homingReady = false;
        homing$setHighlightedEntity(null);
    }

    @Unique
    public void homing$setHomingReady() {
        homing$homingReady = true;
    }

    @Unique
    public boolean homing$isHomingReady() {
        return homing$homingReady;
    }

}
