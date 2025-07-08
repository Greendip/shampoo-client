package me.alpha432.oyvey.mixin;

import com.mojang.datafixers.kinds.IdF;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @ModifyArgs(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Fog;<init>(FFLnet/minecraft/client/render/FogShape;FFFF)V"))
    private static void applyFog(Args args, Camera camera, BackgroundRenderer.FogType fogType, Vector4f originalColor, float viewDistance, boolean thickenFog, float tickDelta) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            NoRender noRender = OyVey.moduleManager.getModuleByClass(NoRender.class);
            if (noRender.isEnabled() && noRender.fog.getValue())
            args.set(0, viewDistance * 4);
            args.set(1, viewDistance * 4.25f);
        }

    }

}
