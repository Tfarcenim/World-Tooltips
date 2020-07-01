package ninja.worldtooltips.tooltips.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import ninja.worldtooltips.tooltips.client.config.ClientConfig;
import ninja.worldtooltips.utils.ModUtils;

import java.util.ArrayList;
import java.util.List;

public class Tooltip implements Comparable<Tooltip> {

	private static final Minecraft mc = Minecraft.getInstance();
	private ItemEntity entity;
	private PlayerEntity player;
	private TextFormatting textFormatting;
	private List<ITextComponent> text = new ArrayList<>();
	private int width, height;
	private int tickCount;
	private int fadeCount;
	public double distanceToPlayer;
	public float scale;
	public int alpha;
	public int colorBackground;
	public int colorOutline;
	public int colorOutlineShade;
	private boolean forceFade;
	private boolean countDown = true;

	public Tooltip(PlayerEntity player, ItemEntity entity) {
		this.player = player;
		this.entity = entity;
		textFormatting = entity.getItem().getRarity().color;
		generateTooltip(player);
		calculateSize();
		fadeCount = ClientConfig.getFadeTime();
		tickCount = ClientConfig.getShowTime() + fadeCount;
	}

	private void generateTooltip(PlayerEntity player) {
		boolean advanced = mc.gameSettings.advancedItemTooltips;
		text = entity.getItem().getTooltip(player, advanced ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
		if (!modsAreLoaded() && !ClientConfig.isHidingModName())
		text.add(new StringTextComponent(ModUtils.getModName(entity)).applyTextStyles(TextFormatting.BLUE,TextFormatting.ITALIC,TextFormatting.RESET));
		if (entity.getItem().getCount() > 1)
			text.set(0,new StringTextComponent( entity.getItem().getCount() + " x " + text.get(0)));
	}

	private void calculateSize() {
		int max = 0;
		for (int line = 0; line < text.size(); line++) {
			int tmp = mc.fontRenderer.getStringWidth(text.get(line).getFormattedText());
			if (tmp > max)
				max = tmp;
		}
		width = max;
		height = 8;
		if (size() > 1)
			height += 2 + (size() - 1) * 10;
	}

	public void tick() {
		if (entity == null)
			tickCount = 0;
		if (countDown)
			tickCount--;
		else
			tickCount += ClientConfig.getFadeTime() / 4;
		if (tickCount < 0)
			tickCount = 0;
		if (tickCount > ClientConfig.getShowTime() + fadeCount)
			tickCount = ClientConfig.getShowTime() + fadeCount;
		generateTooltip(player);
		calculateSize();
		distanceToPlayer = entity.getDistance(player);
		scale = (float) (distanceToPlayer / ((6 - Minecraft.getInstance().getMainWindow().getGuiScaleFactor()) * 160));
		if (scale < 0.01)
			scale = 0.01f;
		scale *= ClientConfig.getScale();
		if (getFade() > ClientConfig.getOpacity())
			alpha = ((int) (ClientConfig.getOpacity() * 0xFF) & 0xFF) << 24;
		else
			alpha = ((int) (getFade() * 0xFF) & 0xFF) << 24;
		colorBackground = ClientConfig.getBackground() | alpha;
		colorOutline = ((ClientConfig.isOverridingOutline() ? ClientConfig.getOutline() : ModUtils.getRarityColor(this)) | alpha) & 0xFFE0E0E0;
		colorOutlineShade = ((colorOutline & 0xFEFEFE) >> 1) | alpha;
		countDown = true;
	}

	public double getFade() {
		if (tickCount > fadeCount)
			return 1D;
		return Math.abs(Math.pow(-1, 2) * ((double) tickCount / (double) fadeCount));
	}

	public void forceFade() {
		if (forceFade)
			return;
		tickCount = 10;
		fadeCount = 10;
		forceFade = true;
	}

	private boolean modsAreLoaded() {
		return false;//ModList.get().isLoaded("waila") | Loader.isModLoaded("nei") | Loader.isModLoaded("hwyla");
	}

	@Override
	public int compareTo(Tooltip o) {
		return (int) (o.distanceToPlayer * 10000 - distanceToPlayer * 10000);
	}

	public boolean reset() {
		if (forceFade)
			return false;
		countDown = false;
		return true;
	}

	public ItemEntity getEntity() {
		return entity;
	}

	public int getTickCount() {
		return tickCount;
	}

	public boolean isDead() {
		return tickCount <= 0;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int size() {
		return text.size();
	}

	public List<ITextComponent> getText() {
		return text;
	}

	public TextFormatting formattingColor() {
		return textFormatting;
	}
}
