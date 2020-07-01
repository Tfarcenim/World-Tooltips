package ninja.worldtooltips;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import ninja.worldtooltips.tooltips.client.config.ClientConfig;
import ninja.worldtooltips.tooltips.client.render.WorldTooltipsRenderType;
import ninja.worldtooltips.utils.ModUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hooks {

	public static final Minecraft mc = Minecraft.getInstance();

	public static void renderItemEntityHook(ItemEntity entity, float p_225623_2_, MatrixStack matrices, IRenderTypeBuffer buffer, int light) {
		double d0 = Minecraft.getInstance().getRenderManager().squareDistanceTo(entity);
		if (d0 <= 4096.0D) {
			float f = entity.getHeight() + 0.5F;
			List<ITextComponent> tooltip = entity.getItem().getTooltip(mc.player, Minecraft.getInstance().
							gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

			tooltip.add(new StringTextComponent(ModUtils.getModName(entity)).applyTextStyles(TextFormatting.BLUE,TextFormatting.ITALIC));
			int i = - 10 * tooltip.size();
			for (int i1 = 0; i1 < tooltip.size(); i1++) {
				ITextComponent string = tooltip.get(i1);

				if (i1 == 0) {
					int count = entity.getItem().getCount();
					if (count > 1)
						string = string.appendSibling(new StringTextComponent(" x " + count));
				}

				matrices.push();
				matrices.translate(0, f, 0);




				ActiveRenderInfo camera = mc.gameRenderer.getActiveRenderInfo();

				matrices.rotate(Vector3f.YP.rotationDegrees(-camera.getYaw()));

				//matrices.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
				float scale = ClientConfig.scale.get().floatValue() * -.025f;

				matrices.scale(scale,scale, scale);				Matrix4f matrix4f = matrices.getLast().getPositionMatrix();
				FontRenderer fontrenderer = Minecraft.getInstance().getRenderManager().getFontRenderer();
				float f2 = -fontrenderer.getStringWidth(string.getFormattedText()) / 2f;

				int alpha = (int) (ClientConfig.opacity.get() * 0xff);

				fontrenderer.renderString(string.getFormattedText(), f2,i , alpha << 24 | 0xffffff, false, matrix4f, buffer, false, 0, light);
				i += 10;
				matrices.pop();
			}
			matrices.push();
			renderTooltip(entity, matrices, buffer, tooltip);
			matrices.pop();
		}
	}

	private static void renderTooltip(ItemEntity entity, MatrixStack matrices, IRenderTypeBuffer buffer, List<ITextComponent> tooltip) {

		IVertexBuilder builder = buffer.getBuffer(WorldTooltipsRenderType.getHealthBarType());
		Matrix4f matrix4f = matrices.getLast().getPositionMatrix();
		float height = tooltip.size() * .25f + .10f;

		List<Integer> list = tooltip.stream().map(ITextComponent::getFormattedText).map(mc.fontRenderer::getStringWidth).collect(Collectors.toList());
		float width = Collections.max(list) * .026f + .12f;

		float f = entity.getHeight() + 0.5F;
		matrices.translate(0,f,0);

		float scale = ClientConfig.scale.get().floatValue();

		matrices.scale(scale,scale,scale);

		ActiveRenderInfo camera = mc.gameRenderer.getActiveRenderInfo();
		matrices.rotate(Vector3f.YP.rotationDegrees(-camera.getYaw()));
		//main background
		int alpha = (int) (ClientConfig.opacity.get() * 0xff);

		float z = 0;
		fill(builder, matrix4f, -width / 2, width, 0, height,z, alpha << 24 | 0x20021C);

		z+= -.001;

		float thickness = .01f;

		float padding = .04f;

		int color = entity.getItem().getRarity().color.getColor();



		//left
		fill(builder,matrix4f,width / 2 - padding,thickness,0,height - padding,z, alpha << 24 | color);

		//right
		fill(builder,matrix4f,-width / 2 + padding,thickness,0, height - padding,z,alpha << 24 | color);

		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding,0,thickness,z,alpha << 24 | color);

		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding, height - padding,thickness,z,alpha << 24 | color);


	}


	public static void fill(IVertexBuilder builder, Matrix4f matrix4f, float u, float width, float v, float height,float z, int aarrggbb) {
		float a = (aarrggbb >> 24 & 0xff) / 255f;
		float r = (aarrggbb >> 16 & 0xff) / 255f;
		float g = (aarrggbb >> 8 & 0xff) / 255f;
		float b = (aarrggbb & 0xff) / 255f;

		fill(builder, matrix4f, u, width, v, height,z, r, g, b, a);
	}

	public static void fill(IVertexBuilder builder, Matrix4f matrix4f, float u, float width, float v, float height,float z, float r, float g, float b, float a) {
		builder.pos(matrix4f, u, v, z).tex(0.0F, 0.0F).color(r, g, b, a).normal(Vector3f.YP.getX(), Vector3f.YP.getY(), Vector3f.YP.getZ()).endVertex();
		builder.pos(matrix4f, u, v + height, z).tex(0.0F, 0.5F).color(r, g, b, a).normal(Vector3f.YP.getX(), Vector3f.YP.getY(), Vector3f.YP.getZ()).endVertex();
		builder.pos(matrix4f, u + width, v + height, z).tex(1.0F, 0.5F).color(r, g, b, a).normal(Vector3f.YP.getX(), Vector3f.YP.getY(), Vector3f.YP.getZ()).endVertex();
		builder.pos(matrix4f, u + width, v, z).tex(1.0F, 0.0F).color(r, g, b, a).normal(Vector3f.YP.getX(), Vector3f.YP.getY(), Vector3f.YP.getZ()).endVertex();
	}
}
