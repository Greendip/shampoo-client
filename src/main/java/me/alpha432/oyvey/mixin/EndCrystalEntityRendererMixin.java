package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.CrystalModel;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EndCrystalEntityRenderer.class)
public class EndCrystalEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void aVoid(EndCrystalEntityRenderState endCrystalEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        NoRender noRender = OyVey.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled()&& noRender.crystals.getValue())
            ci.cancel();
    }
    @ModifyArgs(method = "render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0))
    private void render$scale(Args args) {
        if (OyVey.moduleManager.getModuleByClass(CrystalModel.class).isEnabled() ) {
            args.set(0, 2.0F * OyVey.moduleManager.getModuleByClass(CrystalModel.class).scale.getValue().floatValue());
            args.set(1, 2.0F * OyVey.moduleManager.getModuleByClass(CrystalModel.class).scale.getValue().floatValue());
            args.set(2, 2.0F * OyVey.moduleManager.getModuleByClass(CrystalModel.class).scale.getValue().floatValue());
        }
    }

    @Inject(method = "getYOffset", at = @At(value = "HEAD"), cancellable = true)
    private static void getYOffset(float f, CallbackInfoReturnable<Float> info) {
        if (OyVey.moduleManager.getModuleByClass(CrystalModel.class).isEnabled() ) {
            float bounce = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
            bounce = (bounce * bounce + bounce) * 0.4F * OyVey.moduleManager.getModuleByClass(CrystalModel.class).bounce.getValue().floatValue();

            info.setReturnValue(bounce - 1.4F);
        }
    }

}
