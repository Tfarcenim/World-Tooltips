package ninja.worldtooltips.tooltips.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import ninja.worldtooltips.tooltips.client.Tooltip;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.ItemEntity;

import java.util.List;

public class RenderHelper {

	public static final Minecraft mc = Minecraft.getInstance();

	public static void renderTooltip(Tooltip tooltip, double partialTicks, MatrixStack matrices) {
		ActiveRenderInfo camera = mc.gameRenderer.getActiveRenderInfo();
		ItemEntity e = tooltip.getEntity();
		camera.getRenderViewEntity();
		Entity cameraEntity = camera.getRenderViewEntity();

		double viewerPosX = cameraEntity.lastTickPosX + (cameraEntity.getPosX() - cameraEntity.lastTickPosX) * partialTicks;
		double viewerPosY = cameraEntity.lastTickPosY + (cameraEntity.getPosY() - cameraEntity.lastTickPosY) * partialTicks;
		double viewerPosZ = cameraEntity.lastTickPosZ + (cameraEntity.getPosZ() - cameraEntity.lastTickPosZ) * partialTicks;

		float playerViewY = (float) (cameraEntity.prevRotationYaw + (cameraEntity.rotationYaw - cameraEntity.prevRotationYaw) * partialTicks);
		float playerViewX = (float) (cameraEntity.prevRotationPitch + (cameraEntity.rotationPitch - cameraEntity.prevRotationPitch) * partialTicks);

		double interpX = viewerPosX - (e.getPosX() - (e.prevPosX - e.getPosX()) * partialTicks);
		double interpY = viewerPosY - 0.65 - (e.getPosY() - (e.prevPosY - e.getPosY()) * partialTicks);
		double interpZ = viewerPosZ - (e.getPosZ() - (e.prevPosZ - e.getPosZ()) * partialTicks);


		//matrices.rotate(new Quaternion(playerViewY + 180, 0, -1, 0));
		//matrices.rotate(new Quaternion(playerViewX, -1, 0, 0));
		matrices.scale(tooltip.scale, -tooltip.scale, tooltip.scale);

		//renderTooltipTile(tooltip);

		List<ItemEntity> items = mc.player.world.getEntitiesWithinAABB(ItemEntity.class,new AxisAlignedBB(-64,-64,-64,64,64,64));

		IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();


		items.forEach(itemEntity -> renderEntity(matrices,buffer,camera,itemEntity,0xffffff));

		//renderTooltipText(tooltip);

		matrices.scale(1 / tooltip.scale, 1 / -tooltip.scale, 1 / tooltip.scale);
		//matrices.rotate(	new Quaternion(playerViewX, 1, 0, 0));
		//matrices.rotate(new Quaternion(playerViewY - 180, 0, 1, 0));
		matrices.pop();
	}

	private static void renderTooltipTile(Tooltip tooltip) {
		int x = -tooltip.getWidth() / 2;
		int y = -tooltip.getHeight() / 2;
		int w = tooltip.getWidth();
		int h = tooltip.getHeight();
		int c1 = tooltip.colorBackground;
		int c2 = tooltip.colorOutline;
		int c3 = tooltip.colorOutlineShade;
		renderStyle1(x, y, w, h, c1, c2, c3);
	}

	private static void renderStyle1(int x, int y, int w, int h, int c1, int c2, int c3) {
		// Background
		drawRect(x - 3, y - 4 + 0, 0, w + 6, 1 + 0, c1);
		drawRect(x + w + 3, y - 3 + 0, 0, 1 + 0, h + 6, c1);
		drawRect(x - 3 + 0, y + h + 3, 0, w + 6, 1 + 0, c1);
		drawRect(x - 4 + 0, y - 3 + 0, 0, 1 + 0, h + 6, c1);
		drawRect(x - 2 + 0, y - 2 + 0, 0, w + 4, h + 4, c1);
		// Outline
		drawRect(x - 3 + 0, y - 3 + 0, 0, w + 6, 1 + 0, c2);
		drawGradientRect(x + w + 2, y - 2 + 0, 0, 1 + 0, h + 4, c2, c3);
		drawRect(x - 3 + 0, y + h + 2, 0, w + 6, 1 + 0, c3);
		drawGradientRect(x - 3 + 0, y - 2 + 0, 0, 1 + 0, h + 4, c2, c3);
	}

	private static void renderTooltipText(Tooltip tooltip) {
		if ((tooltip.alpha & 0xFC000000) == 0)
			return;
		int x = -tooltip.getWidth() / 2;
		int y = -tooltip.getHeight() / 2;
		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
		for (int i = 0; i < tooltip.getText().size(); i++) {
			ITextComponent s = tooltip.getText().get(i);
			if (i == 0)
				s = s.applyTextStyle(tooltip.formattingColor());
			Minecraft.getInstance().fontRenderer.drawString(s.getFormattedText(), x, y, 0xFFFFFF | tooltip.alpha);
			if (i == 0)
				y += 2;
			y += 10;
		}
		RenderSystem.disableBlend();
		RenderSystem.popMatrix();
	}

	public static void drawRect(double x, double y, double z, double w, double h, int c1) {
		drawGradientRect(x, y, z, w, h, c1, c1, c1, c1);
	}

	public static void drawGradientRect(double x, double y, double z, double w, double h, int c1) {
		int alpha = c1 >> 24 & 0xFF;
		int c2 = ((c1 & 0xFEFEFE) >> 1) | alpha;
		drawGradientRect(x, y, z, w, h, c1, c2, c2, c1);
	}

	public static void drawGradientRect(double x, double y, double z, double w, double h, int c1, int c2) {
		drawGradientRect(x, y, z, w, h, c1, c2, c2, c1);
	}

	public static void drawGradientRect(double x, double y, double z, double w, double h, int c1, int c2, int c3, int c4) {
		int a1 = c1 >> 24 & 0xFF;
		int r1 = c1 >> 16 & 0xFF;
		int g1 = c1 >> 8 & 0xFF;
		int b1 = c1 & 0xFF;
		int a2 = c2 >> 24 & 0xFF;
		int r2 = c2 >> 16 & 0xFF;
		int g2 = c2 >> 8 & 0xFF;
		int b2 = c2 & 0xFF;
		int a3 = c3 >> 24 & 0xFF;
		int r3 = c3 >> 16 & 0xFF;
		int g3 = c3 >> 8 & 0xFF;
		int b3 = c3 & 0xFF;
		int a4 = c4 >> 24 & 0xFF;
		int r4 = c4 >> 16 & 0xFF;
		int g4 = c4 >> 8 & 0xFF;
		int b4 = c4 & 0xFF;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		RenderSystem.color4f(1, 1, 1, 1);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder bb = tess.getBuffer();
		bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		bb.pos(x + 0, y + 0, z).color(r1, g1, b1, a1).endVertex();
		bb.pos(x + 0, y + h, z).color(r2, g2, b2, a2).endVertex();
		bb.pos(x + w, y + h, z).color(r3, g3, b3, a3).endVertex();
		bb.pos(x + w, y + 0, z).color(r4, g4, b4, a4).endVertex();
		tess.draw();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	private static void renderEntity(MatrixStack matrices, IRenderTypeBuffer.Impl buffer, ActiveRenderInfo renderInfo, ItemEntity entity, int light) {

		matrices.push();
		matrices.translate(mc.player.getPosX() - entity.getPosX(),
						mc.player.getPosY()-entity.getPosY(), mc.player.getPosZ()-entity.getPosZ());
		Quaternion rotation = renderInfo.getRotation().copy();
		rotation.multiply(-1.0F);
		matrices.rotate(rotation);
		float scale = 0.026666672F;
		matrices.scale(-scale, -scale, scale);
		float size = 20;//ClientConfig.scale.get().floatValue();
		float textScale = 0.5F;

		String name = (entity.hasCustomName() ? entity.getCustomName().applyTextStyle(TextFormatting.ITALIC) : entity.getDisplayName()).getFormattedText();
		float namel = RenderHelper.mc.fontRenderer.getStringWidth(name) * textScale;
		if (namel + 20 > size * 2) {
			size = namel / 2.0F + 10.0F;
		}
		MatrixStack.Entry entry = matrices.getLast();
		Matrix4f modelViewMatrix = entry.getPositionMatrix();
		Vector3f normal = new Vector3f(0.0F, 1.0F, 0.0F);
		normal.transform(entry.getNormalMatrix());
		IVertexBuilder builder = buffer.getBuffer(WorldTooltipsRenderType.getHealthBarType());
		float padding = 100;//NeatConfig.backgroundPadding;
		int bgHeight = 100;
		//NeatConfig.backgroundHeight;
		int barHeight = 100;
		//NeatConfig.barHeight;

		// Background
		builder.pos(modelViewMatrix, -size - padding, -bgHeight, 0.0F).tex(0.0F, 0.0F).color(0, 0, 0, 64).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, -size - padding, barHeight + padding, 0.0F).tex(0.0F, 0.5F).color(0, 0, 0, 64).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, size + padding, barHeight + padding, 0.0F).tex(1.0F, 0.5F).color(0, 0, 0, 64).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, size + padding, -bgHeight, 0.0F).tex(1.0F, 0.0F).color(0, 0, 0, 64).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();

		// Gray Space
		builder.pos(modelViewMatrix, -size, 0, -0.001F).tex(0.0F, 0.5F).color(0, 0, 0, 127).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, -size, barHeight, -0.001F).tex(0.0F, 0.75F).color(0, 0, 0, 127).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, size, barHeight, -0.001F).tex(1.0F, 0.75F).color(0, 0, 0, 127).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
		builder.pos(modelViewMatrix, size, 0, -0.001F).tex(1.0F, 0.5F).color(0, 0, 0, 127).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();

		int white = 0xFFFFFF;
		int black = 0x000000;
		matrices.translate(-size, -4.5F, 0F);
		matrices.scale(textScale, textScale, textScale);
		modelViewMatrix = matrices.getLast().getPositionMatrix();
		RenderHelper.mc.fontRenderer.renderString(name, 0, 0, white, false, modelViewMatrix, buffer, false, black, light);

		float s1 = 0.75F;
		matrices.push();
		matrices.scale(s1, s1, s1);
		modelViewMatrix = matrices.getLast().getPositionMatrix();

		int h = 10;//NeatConfig.hpTextHeight;
		String maxHpStr = "100";

		RenderHelper.mc.fontRenderer.renderString(maxHpStr, (int) (size / (textScale * s1) * 2) - 2 - RenderHelper.mc.fontRenderer.getStringWidth(maxHpStr), h, white, false, modelViewMatrix, buffer, false, black, light);
		matrices.pop();
	}
}
