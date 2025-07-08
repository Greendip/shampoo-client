package me.alpha432.oyvey.mixin;

import com.mojang.authlib.GameProfile;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.Capes;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin(PlayerListEntry.class)
public class MixinPlayerListEntry {
    @Shadow
    @Final
    private GameProfile profile;

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> info) {
        if (((profile.getName().equals(mc.player.getGameProfile().getName()) && profile.getId().equals(mc.player.getGameProfile().getId()))) && OyVey.moduleManager.getModuleByClass(Capes.class).isEnabled() && OyVey.moduleManager.getModuleByClass(Capes.class).getCapeTexture() != null) {
            Identifier identifier = OyVey.moduleManager.getModuleByClass(Capes.class).getCapeTexture();
            SkinTextures texture = info.getReturnValue();

            info.setReturnValue(new SkinTextures(texture.texture(), texture.textureUrl(), identifier, identifier, texture.model(), texture.secure()));
        }
    }
}