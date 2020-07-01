package ninja.worldtooltips.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import ninja.worldtooltips.tooltips.client.Tooltip;
import ninja.worldtooltips.tooltips.client.config.ClientConfig;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

public class ModUtils {

	private static final Map<TextFormatting, Integer> formatting_color = new HashMap<>();

	public static void post() {
		for (TextFormatting color : TextFormatting.values())
			formatting_color.put(color, color.getColor());
	}

	public static int getRarityColor(TextFormatting format) {
		return formatting_color.getOrDefault(format, ClientConfig.getOutline());
	}

	public static int getRarityColor(Tooltip tooltip) {
		return formatting_color.getOrDefault(tooltip.formattingColor(), ClientConfig.getOutline());
	}

	public static String getModName(ItemEntity entity) {
		return getModName(entity.getItem());
	}


	@Nullable
	private static String getModName(ItemStack itemStack) {
			Item item = itemStack.getItem();
			String modId = item.getCreatorModId(itemStack);
			if (modId != null) {
				return ModList.get().getModContainerById(modId)
								.map(modContainer -> modContainer.getModInfo().getDisplayName())
								.orElse(StringUtils.capitalize(modId));
			}
		return null;
	}


	public static Optional<ItemEntity> getMouseOver() {
		return getMouseOver(Minecraft.getInstance().world, Minecraft.getInstance().player, 0);
	}

	public static Optional<ItemEntity> getMouseOver(World world, Entity player, float partialTicks) {
		if (world == null || player == null)
			return Optional.empty();
		final int range = ClientConfig.getRenderDistance();
		final Vec3d eyes = player.getEyePosition(partialTicks);
		final Vec3d look = player.getLook(partialTicks);
		final Vec3d view = eyes.add(look.x * range, look.y * range, look.z * range);
		double distance = 0;
		ItemEntity out = null;
		List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class,
				player.getBoundingBox().expand(look.x * range, look.y * range, look.z * range).grow(1F, 1F, 1F));
		for (int i = 0; i < list.size(); i++) {
			ItemEntity entity = list.get(i);
			AxisAlignedBB aabb = entity.getBoundingBox().offset(0, 0.25, 0).grow(entity.getCollisionBorderSize() + 0.1);
			Optional<Vec3d> ray = aabb.rayTrace(eyes, view);
			if (aabb.contains(eyes)) {
				if (distance > 0) {
					out = entity;
					distance = 0;
				}
			} else if (ray.isPresent()) {
				double d = eyes.distanceTo(ray.get());
				if (d < distance || distance == 0) {
					out = entity;
					distance = d;
				}
			}
		}
		return Optional.ofNullable(out);
	}
}
