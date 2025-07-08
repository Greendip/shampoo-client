package me.alpha432.oyvey.util;

import me.alpha432.oyvey.features.modules.client.ClickGui;

import java.awt.*;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), ClickGui.getInstance().rainbowSaturation.getValue().floatValue() / 255.0f, ClickGui.getInstance().rainbowBrightness.getValue().floatValue() / 255.0f);
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float) colors[0], (float) colors[1], (float) colors[2], (float) colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    public static Color pulseColor(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % ((long)1230675006 ^ 0x495A9BEEL) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + index / (float)count * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        hsb[2] = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color pulseColor(Color startColor, Color endColor, int index, int count, double speed) {
        double brightness = Math.abs((System.currentTimeMillis() * speed % ((long) 1230675006 ^ 0x495A9BEEL) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (double) index / count * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        //brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        double quad = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return fadeColor(startColor, endColor, quad);
    }

    public static Color fadeColor(Color startColor, Color endColor, double quad) {
        int sR = startColor.getRed();
        int sG = startColor.getGreen();
        int sB = startColor.getBlue();
        int sA = startColor.getAlpha();

        int eR = endColor.getRed();
        int eG = endColor.getGreen();
        int eB = endColor.getBlue();
        int eA = endColor.getAlpha();
        return new Color((int) (sR + (eR - sR) * quad),(int) (sG + (eG - sG) * quad), (int) (sB + (eB - sB) * quad), (int) (sA + (eA - sA) * quad));
    }
    public static Color getOffsetRainbow(long index) {
        return getOffsetRainbow(255, index);
    }
    public static Color getOffsetRainbow(int alpha, long index) {
        return getRainbow(6, 100 / 100.0f, 100 / 100.0f, alpha, index);
    }
    public static Color getRainbow(long speed, float saturation, float brightness, int alpha) {
        return getRainbow(speed, saturation, brightness, alpha, 0);
    }

    public static Color getRainbow(long speed, float saturation, float brightness, int alpha, long index) {
        speed = Math.clamp(speed, 1, 20);

        float hue = ((System.currentTimeMillis() + index) % (10500 - (500 * speed))) / (10500.0f - (500.0f * (float) speed));
        Color color = new Color(Color.HSBtoRGB(Math.clamp(hue, 0.0f, 1.0f), Math.clamp(saturation, 0.0f, 1.0f), Math.clamp(brightness, 0.0f, 1.0f)));

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}