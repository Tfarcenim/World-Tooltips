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
		iEventBus.addListener(this::pre);
	}

	public void pre(FMLClientSetupEvent event) {
		//ClientRegistry.registerKeyBinding(configKey);
		ModUtils.post();
	}
}
