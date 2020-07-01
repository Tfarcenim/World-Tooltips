package ninja.worldtooltips.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninja.worldtooltips.tooltips.client.Tooltip;
import ninja.worldtooltips.tooltips.client.config.ClientConfig;
import ninja.worldtooltips.tooltips.client.render.RenderHelper;
import ninja.worldtooltips.utils.ModUtils;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(Constants.MODID)
public class WorldTooltips {

	public static WorldTooltips instance;
	private KeyBinding configKey = new KeyBinding(Constants.NAME + " Configuration", GLFW.GLFW_KEY_KP_SUBTRACT, Constants.NAME);

	public WorldTooltips() {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EVENT_BUS.addListener(this::tick);
		iEventBus.addListener(this::pre);
	}

	private LinkedList<Tooltip> tooltips = new LinkedList<>();

	public void tick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START)
			return;
		tooltips.removeIf(Objects::isNull);
		tooltips.removeIf(Tooltip::isDead);
		tooltips.forEach(Tooltip::tick);
		Optional<ItemEntity> mouseOver = ModUtils.getMouseOver();
		if (mouseOver.isPresent()) {
			boolean createTooltip = true;
			ItemEntity entity = mouseOver.get();
			for (Tooltip tooltip : tooltips)
				if (tooltip.getEntity() == entity)
					createTooltip = !tooltip.reset();
			if (createTooltip)
				tooltips.addFirst(new Tooltip(Minecraft.getInstance().player, entity));
		}
		for (int i = ClientConfig.getMaxTooltips(); i < tooltips.size(); i++)
			tooltips.get(i).forceFade();
	}

	public void pre(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(configKey);
		ModUtils.post();
	}
}
