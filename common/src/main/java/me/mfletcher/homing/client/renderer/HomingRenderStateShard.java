package me.mfletcher.homing.client.renderer;

import com.mojang.blaze3d.vertex.VertexFormat;
import me.mfletcher.homing.PlayerHomingData;
import me.mfletcher.homing.mixin.mixins.client.AccessorRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;

public class HomingRenderStateShard extends RenderStateShard {
    public HomingRenderStateShard(String name, Runnable setupState, Runnable clearState) {
        super(name, setupState, clearState);
    }

    // https://github.com/VazkiiMods/Neat/blob/master/Xplat/src/main/java/vazkii/neat/NeatRenderType.java
    public static final ResourceLocation RETICLE_0_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_0.png");
    public static final ResourceLocation RETICLE_1_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_1.png");
    public static final ResourceLocation RETICLE_2_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_2.png");
    public static final ResourceLocation RETICLE_3_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_3.png");
    public static final ResourceLocation RETICLE_4_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_4.png");
    public static final ResourceLocation RETICLE_5_TEXTURE = new ResourceLocation(PlayerHomingData.MOD_ID, "textures/ui/reticle_5.png");
    public static final RenderType[] RETICLE_TYPES = new RenderType[] {
            getType(RETICLE_0_TEXTURE),
            getType(RETICLE_1_TEXTURE),
            getType(RETICLE_2_TEXTURE),
            getType(RETICLE_3_TEXTURE),
            getType(RETICLE_4_TEXTURE),
            getType(RETICLE_5_TEXTURE)
    };

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
