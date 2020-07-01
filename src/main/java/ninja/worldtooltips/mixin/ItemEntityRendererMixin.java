package ninja.worldtooltips.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.entity.item.ItemEntity;
import ninja.worldtooltips.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemEntityRendererMixin {
	@Inject(method = "render",at = @At("RETURN"))
	private void renderInWorldTooltips(ItemEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_, CallbackInfo ci){
		Hooks.renderItemEntityHook( p_225623_1_, p_225623_2_, p_225623_4_, p_225623_5_, p_225623_6_);
	}
}
