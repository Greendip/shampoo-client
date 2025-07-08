package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    public TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY,float delta, CallbackInfo ci) {
        int i = 0;
        context.drawTextWithShadow(textRenderer, Formatting.WHITE + OyVey.NAME + OyVey.VERSION + " " + "- 09/06/2025 23:25", 1, height - (client.textRenderer.fontHeight * 60) - -1, 1  | i);
    }
}

