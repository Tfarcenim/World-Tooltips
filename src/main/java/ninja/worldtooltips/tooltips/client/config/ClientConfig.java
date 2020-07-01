package ninja.worldtooltips.tooltips.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;


public class ClientConfig {

	public static final String category_general = "General";
	public static final String category_appearance = "Appearance";
	public static final String category_behavior = "Behavior";

	public static ForgeConfigSpec.IntValue render_distance;
	public static ForgeConfigSpec.IntValue max_tooltips;
	public static ForgeConfigSpec.IntValue show_time;
	public static ForgeConfigSpec.IntValue fade_time;
	public static ForgeConfigSpec.BooleanValue hide_outline_color;
	public static ForgeConfigSpec.BooleanValue hide_mod_name;
	public static ForgeConfigSpec.DoubleValue opacity;
	public static ForgeConfigSpec.DoubleValue scale;
	public static ForgeConfigSpec.ConfigValue<String> outline_color;
	public static ForgeConfigSpec.ConfigValue<String> background_color;



	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("general");
		render_distance = builder.comment("Sets the maximum distance that tooltips will be displayed.")
						.defineInRange("render_distance",12,1,Integer.MAX_VALUE);
		max_tooltips = builder.comment("Sets the maximum number of tooltips shown on screen at once.")
						.defineInRange("max_tooltips",4,0,Integer.MAX_VALUE);
		show_time = builder.comment("Sets the number of ticks to show the tooltips before they fade.")
						.defineInRange("ticks_to_show",40, 0, 1000);
		fade_time = builder.comment("Sets the duration in ticks for the fading process.")
						.defineInRange("fade_duration", 10, 0, 1000);
		hide_outline_color = builder.comment("Use the custom outline color instead.")
						.define("override_outline_color", false);
		hide_mod_name = builder.comment("Hide mod names on tooltips. Enable this if you see two mod names.")
						.define("hide_mod_name", false);
		opacity = builder.comment("Sets the opacity for the tooltips; 0 being completely invisible and 1 being completely opaque.")
						.defineInRange("tooltip_opacity", 0.75, 0.0, 1.0);
		scale = builder.comment("Sets the scale for the tooltips; 0.1 being one thenth the size and 4 being four times the size.")
						.defineInRange( "tooltip_scale", 1.0,0.1, 4.0);
		outline_color = builder.comment("Choose a color using the gui by clicking the color button or type in a color manually.")
						.define("Outline Color", "0x5000FF");
		background_color = builder.comment("Choose a color using the gui by clicking the color button or type in a color manually.")
						.define("background_color", "0x100010");

		builder.pop();
	}

	public static int getRenderDistance() {
		return render_distance.get();
	}

	public static int getMaxTooltips() {
		return max_tooltips.get();
	}

	public static int getShowTime() {
		return show_time.get();
	}

	public static int getFadeTime() {
		return fade_time.get();
	}

	public static boolean isOverridingOutline() {
		return hide_outline_color.get();
	}

	public static boolean isHidingModName() {
		return hide_mod_name.get();
	}

	public static double getOpacity() {
		return opacity.get();
	}

	public static double getScale() {
		return scale.get();
	}

	public static int getOutline() {
		return Integer.decode(outline_color.get());
	}

	public static int getBackground() {
		return Integer.decode(background_color.get());
	}
}
