package ninja.worldtooltips.utils;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModUtils {

	private static final Map<TextFormatting, Integer> formatting_color = new HashMap<>();

	public static void post() {
		for (TextFormatting color : TextFormatting.values())
			formatting_color.put(color, color.getColor());
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


}
