package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class TextUtil implements Util {
    public static void drawStringPulse(DrawContext drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter) {
        char[] stringToCharArray = text.toCharArray();
        int index = 0;
        boolean color = false;
        String s = null;
        for (char c : stringToCharArray) {
            if (c == '§') {
                color = true;
                continue;
            }
            if (color) {
                if (c == 'r') {
                    s = null;
                } else {
                    s = "§" + c;
                }
                color = false;
                continue;
            }
            index++;
            if (s != null) {
                drawString(drawContext, s + c, x, y, startColor.getRGB());
            } else {
                drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB());
            }
            x += mc.textRenderer.getWidth(String.valueOf(c));
        }
    }

    public static void drawString(DrawContext drawContext, String text, double x, double y, int color) {
        drawContext.drawText(mc.textRenderer, text, (int) x, (int) y, color, true);
    }
}
