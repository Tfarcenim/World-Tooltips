package ninja.worldtooltips.tooltips.client.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import ninja.worldtooltips.tooltips.WorldTooltips;
import org.lwjgl.opengl.GL11;

public class WorldTooltipsRenderType extends RenderState {
	public WorldTooltipsRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static RenderType getHealthBarType() {
		RenderType.State renderTypeState = RenderType.State.getBuilder().transparency(TRANSLUCENT_TRANSPARENCY).build(true);
		return RenderType.makeType(WorldTooltips.MODID, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL, GL11.GL_QUADS, 256, true, true, renderTypeState);
	}

}
