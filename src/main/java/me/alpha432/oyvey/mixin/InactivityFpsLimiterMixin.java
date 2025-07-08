package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.misc.UnfocucedFPS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin(InactivityFpsLimiter.class)
public class InactivityFpsLimiterMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
     private void render(CallbackInfoReturnable<Integer> info) {
        UnfocucedFPS unfocucedFPS = OyVey.moduleManager.getModuleByClass(UnfocucedFPS.class);
        if (unfocucedFPS.isEnabled() && !client.isWindowFocused()) {
            info.setReturnValue(OyVey.moduleManager.getModuleByClass(UnfocucedFPS.class).limit.getValue().intValue());
        }
    }
}
