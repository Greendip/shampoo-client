package me.alpha432.oyvey.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import me.alpha432.oyvey.event.impl.PlayerListNameEvent;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

/**
 * @author linus, hockeyl8
 * @since 1.0
 */
@Mixin(PlayerListHud.class)
public abstract class MixinPlayerListHud
{
    @Shadow
    @Final
    private static Comparator<PlayerListEntry> ENTRY_ORDERING;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract List<PlayerListEntry> collectPlayerEntries();

    @Shadow
    protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Inject(method = "getPlayerName", at = @At(value = "HEAD"), cancellable = true)
    private void hookGetPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir)
    {
        Text text;
        if (entry.getDisplayName() != null)
        {
            text = applyGameModeFormatting(entry, entry.getDisplayName().copy());
        }
        else
        {
            text = applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), Text.literal(entry.getProfile().getName())));
        }
        PlayerListNameEvent playerListNameEvent = new PlayerListNameEvent(text, entry.getProfile().getId());
        Util.EVENT_BUS.post(playerListNameEvent);
        if (playerListNameEvent.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(playerListNameEvent.getPlayerName());
        }
    }


}
