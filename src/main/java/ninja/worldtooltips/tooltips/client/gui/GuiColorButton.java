package ninja.worldtooltips.tooltips.client.gui;

/*public class GuiColorButton extends Button {

	public String value;
	public String defaultV;

	public GuiColorButton(int buttonId, int x, int y, Property property) {
		this(buttonId, x, y, property.getName(), property.getDefault());
	}

	public GuiColorButton(int buttonId, int x, int y, String value, String defaultV) {
		super(buttonId, x, y, 20, 20, value);
		setValues(value, defaultV);
	}

	public void setValues(String value, String defaultV) {
		this.value = value;
		this.defaultV = defaultV;
	}

	public void update(String value) {
		this.value = value;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
		if (!this.visible)
			return;
		int color = 0;
		try {
			color = Integer.decode(value);
		} catch (NumberFormatException e) {
			color = Integer.decode(defaultV);
		}
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		int i = this.getHoverState(this.hovered);
		mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
		blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
		blit(this.x + 2, this.y + 2, this.x + 20 - 2, this.y + 20 - 2, color | 0xFF000000, color | 0xFF000000);
		//this.mouseDragged(mc, mouseX, mouseY);
	}
}*/
