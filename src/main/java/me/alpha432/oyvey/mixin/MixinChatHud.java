package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.CustomFormatting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.*;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin(ChatHud.class)
public abstract class MixinChatHud<E> {
    @Shadow protected abstract boolean isChatHidden();

    @Shadow public abstract int getVisibleLineCount();

    @Shadow @Final public List<ChatHudLine.Visible> visibleMessages;

    @Shadow public abstract double getChatScale();

    @Shadow public abstract int getWidth();

    @Shadow protected abstract int getMessageIndex(double chatLineX, double chatLineY);

    @Shadow protected abstract double toChatLineX(double x);

    @Shadow protected abstract double toChatLineY(double y);
    @Shadow private static double getMessageOpacityMultiplier(int age) { return 0; }

    @Shadow @Final
    private MinecraftClient client;

    @Shadow protected abstract int getLineHeight();

    @Shadow private int scrolledLines;
    @Shadow
    private boolean hasUnreadNewMessages;


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void render(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused) {
        int i = this.getVisibleLineCount();
        double g = this.client.options.getChatLineSpacing().getValue();
        float f = (float)this.getChatScale();
        int j = this.visibleMessages.size();
        int t;
        int l = context.getScaledWindowHeight();
        int u;
        int v;
        int m = MathHelper.floor((float)(l - 40) / f);
        int x;
        double d = this.client.options.getChatOpacity().getValue() * 0.8999999761581421 + 0.10000000149011612;
        int o = this.getLineHeight();
        int p = (int)Math.round(-8.0 * (g + 1.0) + 4.0 * g);
        int q = 0;
        double e = this.client.options.getTextBackgroundOpacity().getValue();
        int k = MathHelper.ceil((float)this.getWidth() / f);

        for (int r = 0; r + this.scrolledLines < this.visibleMessages.size() && r < i; ++r) {

            context.getMatrices().push();

            int s = r + this.scrolledLines;
            ChatHudLine.Visible visible = this.visibleMessages.get(s);
            if (visible != null) {
                t = currentTick - visible.addedTime();
                if (t < 200 || focused) {
                    double h = focused ? 1.0 : getMessageOpacityMultiplier(t);
                    u = (int) (255.0 * h * (d));
                    v = (int) (255.0 * h * (e));
                    ++q;
                    if (u > 3) {
                        x = m - r * o;
                        int y = x + p;
                        context.fill(-4, x - o, 0 + k + 4 + 4, x,  v << 24);

                        context.getMatrices().push();
                        context.drawTextWithShadow(this.client.textRenderer, processOrderedText(visible.content()), 0, y, 16777215 + (u << 24));
                        context.getMatrices().pop();
                    }
                }
            }
        }
    }

    @Unique
    private OrderedText processOrderedText(OrderedText orderedText) {
        MutableText builder = Text.empty();

        int[] i = {0};
        orderedText.accept((index, style, codePoint) -> {
            MutableText text = Text.empty();

            if (style.getColor() != null && (style.getColor().toString().toLowerCase().equals(CustomFormatting.CLIENT.getName()) || style.getColor().toString().toLowerCase().equals(CustomFormatting.RAINBOW.getName()))) {
                if (style.getColor().toString().toLowerCase().equals(CustomFormatting.CLIENT.getName())) {
                    text.append(Text.literal(String.valueOf(Character.toChars(codePoint))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(OyVey.colorManager.getColor().getRGB()))));
                }

                if (style.getColor().toString().toLowerCase().equals(CustomFormatting.RAINBOW.getName())) {
                    long index1 = (long) i[0] * (10 * 5L);
                    Color color = ColorUtil.getOffsetRainbow(index1);
                    text.append(Text.literal(String.valueOf(Character.toChars(codePoint))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB()))));
                }
            } else {
                text.append(Text.literal(String.valueOf(Character.toChars(codePoint))).setStyle(style));
            }

            builder.append(text);
            i[0]++;

            return true;
        });

        return builder.asOrderedText();
    }
}
