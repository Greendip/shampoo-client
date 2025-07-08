package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.player.Velocity;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;




@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> info) {
        NoRender noRender = OyVey.moduleManager.getModuleByClass(NoRender.class);
        Velocity velocity = OyVey.moduleManager.getModuleByClass(Velocity.class);
        if (noRender.isEnabled()  && parameters.getType() == ParticleTypes.EXPLOSION && noRender.explosion.getValue())  {
            if (velocity.isEnabled()  && parameters.getType() == ParticleTypes.EXPLOSION && velocity.explosion.getValue())
                info.cancel();
        };
    }
}
