package ninja.worldtooltips.tooltips;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninja.worldtooltips.tooltips.client.config.ClientConfig;
import ninja.worldtooltips.utils.ModUtils;

@Mod(WorldTooltips.MODID)
public class WorldTooltips {

	public static final String MODID = "worldtooltips";

	public WorldTooltips() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
		IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		iEventBus.addListener(this::pre);
	}

	public void pre(FMLClientSetupEvent event) {
		//ClientRegistry.registerKeyBinding(configKey);
		ModUtils.post();
	}
}
