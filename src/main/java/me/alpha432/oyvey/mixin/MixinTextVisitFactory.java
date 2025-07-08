package me.alpha432.oyvey.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.alpha432.oyvey.util.CustomFormatting;
import me.alpha432.oyvey.util.FormattingUtils;
import net.minecraft.text.Style;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextVisitFactory.class)
public class MixinTextVisitFactory {
    @WrapOperation(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private static char visitFormatted(String instance, int index, Operation<Character> original, @Local(ordinal = 2) LocalRef<Style> style) {
        CustomFormatting customFormatting = CustomFormatting.byCode(instance.charAt(index));
        if (customFormatting != null) style.set(FormattingUtils.withExclusiveFormatting(style.get(), customFormatting));

        return original.call(instance, index);
    }
}
