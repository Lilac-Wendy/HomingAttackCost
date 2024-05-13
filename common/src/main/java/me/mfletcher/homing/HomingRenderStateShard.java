package me.mfletcher.homing;

import com.mojang.blaze3d.vertex.VertexFormat;
import me.mfletcher.homing.mixin.AccessorRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;

public class HomingRenderStateShard extends RenderStateShard {
    public HomingRenderStateShard(String name, Runnable setupState, Runnable clearState) {
        super(name, setupState, clearState);
    }

    // https://github.com/VazkiiMods/Neat/blob/master/Xplat/src/main/java/vazkii/neat/NeatRenderType.java
    public static final ResourceLocation RETICLE_0_TEXTURE = new ResourceLocation(HomingAttack.MOD_ID, "textures/ui/reticle_0.png");
    public static final RenderType RETICLE_0_TYPE = getType(RETICLE_0_TEXTURE);

    public static final ResourceLocation RETICLE_1_TEXTURE = new ResourceLocation(HomingAttack.MOD_ID, "textures/ui/reticle_1.png");
    public static final RenderType RETICLE_1_TYPE = getType(RETICLE_1_TEXTURE);

    public static final ResourceLocation RETICLE_2_TEXTURE = new ResourceLocation(HomingAttack.MOD_ID, "textures/ui/reticle_2.png");
    public static final RenderType RETICLE_2_TYPE = getType(RETICLE_2_TEXTURE);

    private static RenderType getType(ResourceLocation texture) {
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder()
				.setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
				.setTextureState(new TextureStateShard(texture, false, false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setLightmapState(LIGHTMAP)
				.createCompositeState(false);
		return AccessorRenderType.create("homing_reticle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, renderTypeState);
    }
}
