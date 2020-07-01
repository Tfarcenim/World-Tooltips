package ninja.worldtooltips.tooltips.client.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class WorldTooltipsRenderType extends RenderState {
	public WorldTooltipsRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static RenderType getNoIconType() {
		RenderType.State renderTypeState = RenderType.State.builder().transparency(TransparencyState.TRANSLUCENT_TRANSPARENCY).build(true);
		return RenderType.get("neat_icon", DefaultVertexFormats.POSITION_COLOR, 0, 0, false, false, renderTypeState);
	}

	public static RenderType getIconType(ResourceLocation location) {
		RenderType.State renderTypeState = RenderType.State.builder().texture(BLOCK_SHEET).transparency(TRANSLUCENT_TRANSPARENCY).build(true);
		return RenderType.get("neat_icon", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, true, true, renderTypeState);
	}

	public static RenderType getHealthBarType() {
		RenderType.State renderTypeState = RenderType.State.builder().transparency(TRANSLUCENT_TRANSPARENCY).build(true);
		return RenderType.get("neat_health_bar", DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL, 7, 256, true, true, renderTypeState);
	}

}
