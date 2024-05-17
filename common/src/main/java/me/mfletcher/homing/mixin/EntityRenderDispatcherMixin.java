package me.mfletcher.homing.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.mfletcher.homing.client.renderer.HomingRenderStateShard;
import me.mfletcher.homing.mixinaccess.IMinecraftMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://github.com/VazkiiMods/Neat/blob/master/Xplat/src/main/java/vazkii/neat/mixin/EntityRenderDispatcherMixin.java
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    public abstract Quaternionf cameraOrientation();

    @Inject(
            method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            shift = At.Shift.AFTER)
    )
    private void onRender(Entity entity, double worldX, double worldY, double worldZ, float entityYRot, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int light, CallbackInfo ci) {
        // https://github.com/VazkiiMods/Neat/blob/master/Xplat/src/main/java/vazkii/neat/HealthBarRenderer.java
        float size = 1;
        if (!(entity instanceof LivingEntity livingEntity) || !((IMinecraftMixin) Minecraft.getInstance()).isHomingReady() || ((IMinecraftMixin) Minecraft.getInstance()).getHighlightedEntity() != livingEntity)
            return;

        LocalPlayer p = Minecraft.getInstance().player;
        float dist = livingEntity.getBbWidth();
        double theta = Math.atan2(p.getX() - livingEntity.getX(), p.getZ() - livingEntity.getZ());
        double dx = dist * Math.sin(theta);
        double dz = dist * Math.cos(theta);

        poseStack.pushPose();
        poseStack.translate(dx, livingEntity.getBbHeight() / 2, dz);
        poseStack.mulPose(cameraOrientation());

        int i = p.tickCount % 6;
        VertexConsumer builder = buffers.getBuffer(HomingRenderStateShard.RETICLE_TYPES[i]);

        builder.vertex(poseStack.last().pose(), -size, -size, 0.01F).color(0, 255, 0, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        builder.vertex(poseStack.last().pose(), -size, size, 0.01F).color(0, 255, 0, 255).uv(0.0F, 1F).uv2(light).endVertex();
        builder.vertex(poseStack.last().pose(), size, size, 0.01F).color(0, 255, 0, 255).uv(1.0F, 1F).uv2(light).endVertex();
        builder.vertex(poseStack.last().pose(), size, -size, 0.01F).color(0, 255, 0, 255).uv(1.0F, 0.0F).uv2(light).endVertex();

        poseStack.popPose();
    }
}
