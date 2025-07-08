package me.alpha432.oyvey.features.gui.items.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Objects;

public class ColorPicker extends Button{
    Setting setting;
    private Color finalColor;
    boolean pickingColor;
    boolean pickingHue;
    boolean pickingAlpha;

    public ColorPicker(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        finalColor = (Color)setting.getValue();
    }

    @Override
    public void drawScreen(DrawContext context,int mouseX, int mouseY, float partialTicks) {


        RenderUtil.rect(context.getMatrices(),x, y, x + (float) width + 7.4f, y + (float) height - 0.5f, OyVey.colorManager.getColorAsInt());

        try {
            RenderUtil.rect(context.getMatrices(),x - 1.5f + (float) width + 0.6f - 0.5f, y + 5.0f, x + (float) width + 7.0f - 2.5f, y + (float) height - 4.0f, finalColor.getRGB());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        drawString(getName(), x + 2.3f, y - 1.7f - (float) OyVeyGui.getInstance().getTextOffset(), -1);

        if (setting.open) {
            drawPicker(setting, (int) x, (int) y + 15, (int) x, setting.hideAlpha ? (int) y + 100 : (int) y + 103, (int) x, (int) y + 95, mouseX, mouseY);
            drawString("copy", x + 2.3f, y + 113.0f, isInsideCopy(mouseX, mouseY) ? -1 : -5592406);
            drawString("paste", x + (float) width - 2.3f - mc.textRenderer.getWidth("paste") + 11.7f - 4.6f, y + 113.0f, isInsidePaste(mouseX, mouseY) ? -1 : -5592406);
            setting.setValue(finalColor);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
            boolean bl = setting.open = !setting.open;
        }
        if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
            setting.booleanValue = !setting.booleanValue;
        }
        if (mouseButton == 0 && isInsideCopy(mouseX, mouseY) && setting.open) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
            String hex = String.format("#%02x%02x%02x%02x", finalColor.getAlpha(), finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue());
            mc.keyboard.setClipboard(hex);
            Command.sendMessage("Copied the color to your clipboard.");
        }
        if (mouseButton == 0 && isInsidePaste(mouseX, mouseY) && setting.open) {
            try {
                if (mc.keyboard.getClipboard() != null) {
                    if (Objects.requireNonNull(mc.keyboard.getClipboard()).startsWith("#")) {
                        String hex = Objects.requireNonNull(mc.keyboard.getClipboard());

                        int a = Integer.valueOf(hex.substring(1, 3), 16);
                        int r = Integer.valueOf(hex.substring(3, 5), 16);
                        int g = Integer.valueOf(hex.substring(5, 7), 16);
                        int b = Integer.valueOf(hex.substring(7, 9), 16);

                        if (setting.hideAlpha) {
                            setting.setValue(new Color(r, g, b));
                        } else {
                            setting.setValue(new Color(r, g, b, a));
                        }
                    } else {
                        String[] color = mc.keyboard.getClipboard().split(",");
                        setting.setValue(new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
                    }
                }
            }
            catch (NumberFormatException e) {
                Command.sendMessage("Bad color format! Use Hex (#FFFFFFFF)");
            }
        }
    }

    @Override
    public void update() {
        setHidden(!setting.isVisible());
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        pickingAlpha = false;
        pickingHue = false;
        pickingColor = false;
    }

    public boolean isInsideCopy(int mouseX, int mouseY) {
        return mouseOver((int) ((int) x + 2.3f), (int) y + 113, (int) ((int) x + 2.3f) + mc.textRenderer.getWidth("copy"), (int)(y + 112.0f) + mc.textRenderer.fontHeight, mouseX, mouseY);
    }

    public boolean isInsidePaste(int mouseX, int mouseY) {
        return mouseOver((int) (x + (float) width - 2.3f - mc.textRenderer.getWidth("paste") + 11.7f - 4.6f), (int) y + 113, (int) (x + (float) width - 2.3f - mc.textRenderer.getWidth("paste") + 11.7f - 4.6f) + mc.textRenderer.getWidth("paste"), (int)(y + 112.0f) + mc.textRenderer.fontHeight, mouseX, mouseY);
    }

    public void drawPicker(Setting setting, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
        float restrictedX;
        float[] color = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        try {
            color = new float[]{Color.RGBtoHSB(((Color)setting.getValue()).getRed(), ((Color)setting.getValue()).getGreen(), ((Color)setting.getValue()).getBlue(), null)[0], Color.RGBtoHSB(((Color)setting.getValue()).getRed(), ((Color)setting.getValue()).getGreen(), ((Color)setting.getValue()).getBlue(), null)[1], Color.RGBtoHSB(((Color)setting.getValue()).getRed(), ((Color)setting.getValue()).getGreen(), ((Color)setting.getValue()).getBlue(), null)[2], (float)((Color)setting.getValue()).getAlpha() / 255.0f};
        }
        catch (Exception exception) {
            OyVey.LOGGER.info("bad color!");
        }
        int pickerWidth = (int) (width + 7.4f);
        int pickerHeight = 78;
        int hueSliderWidth = pickerWidth + 3;
        int hueSliderHeight = 7;
        int alphaSliderHeight = 7;
        if (!(!pickingColor || GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY))) {
            pickingColor = false;
        }
        if (!(!pickingHue || GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY))) {
            pickingHue = false;
        }
        if (!(!pickingAlpha || GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))) {
            pickingAlpha = false;
        }
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
            pickingColor = true;
        }
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
            pickingHue = true;
        }
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1 && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY)) {
            pickingAlpha = true;
        }
        if (pickingHue) {
            restrictedX = Math.min(Math.max(hueSliderX, mouseX), hueSliderX + hueSliderWidth);
            color[0] = (restrictedX - (float)hueSliderX) / (float)hueSliderWidth;
        }
        if (pickingAlpha && !setting.hideAlpha) {
            restrictedX = Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
            color[3] = 1.0f - (restrictedX - (float)alphaSliderX) / (float)pickerWidth;
        }
        if (pickingColor) {
            restrictedX = Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float)pickerX) / (float)pickerWidth;
            color[2] = 1.0f - (restrictedY - (float)pickerY) / (float)pickerHeight;
        }
        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);
        float selectedRed = (float)(selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (float)(selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (float)(selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, color[3]);
        drawHueSlider(hueSliderX, hueSliderY, pickerWidth + 1, hueSliderHeight, color[0]);

        int cursorX = (int)((float)pickerX + color[1] * (float)pickerWidth);
        int cursorY = (int)((float)(pickerY + pickerHeight) - color[2] * (float)pickerHeight);

        if (pickingColor) {
            RenderUtil.renderCircle(context.getMatrices(),(cursorX), (cursorY), 6.4f, Color.BLACK.getRGB());
            RenderUtil.renderCircle(context.getMatrices(),(cursorX), (cursorY), 6, new Color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), 255).getRGB());
        } else {
            RenderUtil.renderCircle(context.getMatrices(),(cursorX), (cursorY), 3.4f, Color.BLACK.getRGB());
            RenderUtil.renderCircle(context.getMatrices(),(cursorX), (cursorY), 3, Color.WHITE.getRGB());
        }

        if (!setting.hideAlpha) {
            drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth - 1, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, color[3]);
        }
        finalColor = getColor(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
    }

    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    public static Color getColor(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        return new Color(red, green, blue, alpha);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,VertexFormats.POSITION_COLOR);

        // Первый квад - горизонтальный градиент (белый -> выбранный цвет)
        buffer.vertex(pickerX, pickerY, 0).color(1.0f, 1.0f, 1.0f, 1.0f);
        buffer.vertex(pickerX, pickerY + pickerHeight, 0).color(1.0f, 1.0f, 1.0f, 1.0f);
        buffer.vertex(pickerX + pickerWidth, pickerY, 0).color(red, green, blue, alpha);
        buffer.vertex(pickerX + pickerWidth, pickerY + pickerHeight, 0).color(red, green, blue, alpha);

        // Второй квад - вертикальный градиент (прозрачный -> черный)
        RenderSystem.disableDepthTest();
        buffer.vertex(pickerX, pickerY, 0).color(0.0f, 0.0f, 0.0f, 0.0f);
        buffer.vertex(pickerX, pickerY + pickerHeight, 0).color(0.0f, 0.0f, 0.0f, 1.0f);
        buffer.vertex(pickerX + pickerWidth, pickerY, 0).color(0.0f, 0.0f, 0.0f, 0.0f);
        buffer.vertex(pickerX + pickerWidth, pickerY + pickerHeight, 0).color(0.0f, 0.0f, 0.0f, 1.0f);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.enableDepthTest();


        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtil.rect(context.getMatrices(),x, y, x + width, y + 4, -65536);
            y += 4;
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float) step / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6.0f, 1.0f, 1.0f);
                drawGradientRect(x, (float) y + (float) step * ((float) height / 6.0f), x + width, (float) y + (float) (step + 1) * ((float) height / 6.0f), previousStep, nextStep, false);
                ++step;
            }
            int sliderMinY = (int) ((float) y + (float) height * hue) - 4;
            RenderUtil.rect(context.getMatrices(),x, sliderMinY - 1, x + width, sliderMinY + 1, -1);
            drawOutlineRect(x, sliderMinY - 1, x + width, sliderMinY + 1, Color.BLACK, 1.0f);
        } else {
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float) step / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6.0f, 1.0f, 1.0f);
                gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6) + 3, y + height, previousStep, nextStep, true);
                ++step;
            }
            int sliderMinX = (int) ((float) x + (float) width * hue);

            RenderUtil.rect(context.getMatrices(),sliderMinX - 1, y - 1.2f, sliderMinX + 1, y + height + 1.2f, -1);
            drawOutlineRect(sliderMinX - 1.2, y - 1.2, sliderMinX + 1.2, y + height + 1.2, Color.BLACK, 0.1f);

        }
    }
    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
        boolean left = true;
        int checkerBoardSquareSize = height / 2;
        for (int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtil.rect(context.getMatrices(),x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, -1);
                RenderUtil.rect(context.getMatrices(),x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, -7303024);
                if (squareIndex < width - checkerBoardSquareSize) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtil.rect(context.getMatrices(),minX, y, maxX, y + height, -7303024);
                    RenderUtil.rect(context.getMatrices(),minX, y + checkerBoardSquareSize, maxX, y + height, -1);
                }
            }
            left = !left;
        }
        drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1.0f).getRGB(), 0);
        int sliderMinX = (int)((float)(x + width) - (float)width * alpha);
        RenderUtil.rect(context.getMatrices(),sliderMinX - 1, y - 1.2f, sliderMinX + 1, y + height + 1.2f, -1);
        drawOutlineRect(sliderMinX - 1.2, y - 1.2, sliderMinX + 1.2, y + height + 1.2, Color.BLACK, 0.1f);
    }

    public static void drawGradientRect(double leftpos, double top, double right, double bottom, int col1, int col2) {
        // Извлекаем компоненты цвета (ARGB)
        float a1 = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float r1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float g1 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float b1 = (float)(col1 & 0xFF) / 255.0f;

        float a2 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(col2 & 0xFF) / 255.0f;

        // Настройка рендера
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.disableCull();

        // Создаём буфер
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,VertexFormats.POSITION_COLOR);

        // Рисуем градиентный прямоугольник
        buffer.vertex((float) leftpos, (float)top, 0).color(r1, g1, b1, a1);
        buffer.vertex((float)leftpos,(float) bottom, 0).color(r1, g1, b1, a1);
        buffer.vertex((float)right,(float) top, 0).color(r2, g2, b2, a2);
        buffer.vertex((float)right, (float)bottom, 0).color(r2, g2, b2, a2);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Восстанавливаем настройки
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        // Extract color components
        float startA = (float)(startColor >> 24 & 255) / 255.0f;
        float startR = (float)(startColor >> 16 & 255) / 255.0f;
        float startG = (float)(startColor >> 8 & 255) / 255.0f;
        float startB = (float)(startColor & 255) / 255.0f;

        float endA = (float)(endColor >> 24 & 255) / 255.0f;
        float endR = (float)(endColor >> 16 & 255) / 255.0f;
        float endG = (float)(endColor >> 8 & 255) / 255.0f;
        float endB = (float)(endColor & 255) / 255.0f;

        // Setup rendering
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.disableCull();

        // Draw the gradient
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,VertexFormats.POSITION_COLOR);

        buffer.vertex(right, top, 0).color(endR, endG, endB, endA);
        buffer.vertex(left, top, 0).color(startR, startG, startB, startA);
        buffer.vertex(right, bottom, 0).color(endR, endG, endB, endA);
        buffer.vertex(left, bottom, 0).color(startR, startG, startB, startA);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Clean up
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            // Extract color components
            float startA = (float)(startColor >> 24 & 0xFF) / 255.0f;
            float startR = (float)(startColor >> 16 & 0xFF) / 255.0f;
            float startG = (float)(startColor >> 8 & 0xFF) / 255.0f;
            float startB = (float)(startColor & 0xFF) / 255.0f;
            float endA = (float)(endColor >> 24 & 0xFF) / 255.0f;
            float endR = (float)(endColor >> 16 & 0xFF) / 255.0f;
            float endG = (float)(endColor >> 8 & 0xFF) / 255.0f;
            float endB = (float)(endColor & 0xFF) / 255.0f;

            // Set up rendering
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.disableCull();

            // Draw the gradient
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,VertexFormats.POSITION_COLOR);

            buffer.vertex(minX, minY, 0).color(startR, startG, startB, startA);
            buffer.vertex(minX, maxY, 0).color(startR, startG, startB, startA);
            buffer.vertex(maxX, minY, 0).color(endR, endG, endB, endA);
            buffer.vertex(maxX, maxY, 0).color(endR, endG, endB, endA);
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            // Clean up
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        } else {
            drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
        }
    }

    public static int gradientColor(int color, int percentage) {
        int r = ((color & 0xFF0000) >> 16) * (100 + percentage) / 100;
        int g = ((color & 0xFF00) >> 8) * (100 + percentage) / 100;
        int b = (color & 0xFF) * (100 + percentage) / 100;
        return new Color(r, g, b).hashCode();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
        // Adjust colors if hovered
        if (hovered) {
            startColor = gradientColor(startColor, -20);
            endColor = gradientColor(endColor, -20);
        }

        // Extract color components (ARGB format)
        float alpha1 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float red1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float green1 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float blue1 = (float)(startColor & 0xFF) / 255.0f;

        float alpha2 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float red2 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float green2 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float blue2 = (float)(endColor & 0xFF) / 255.0f;

        // Set up rendering
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.disableCull();

        // Build the gradient rectangle
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP,VertexFormats.POSITION_COLOR);

        buffer.vertex(right, top, 0).color(red1, green1, blue1, alpha1);
        buffer.vertex(left, top, 0).color(red1, green1, blue1, alpha1);
        buffer.vertex(right, bottom, 0).color(red2, green2, blue2, alpha2);
        buffer.vertex(left, bottom, 0).color(red2, green2, blue2, alpha2);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Clean up
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    public static void drawOutlineRect(double left, double top, double right, double bottom, Color color, float lineWidth) {
        // Swap coordinates if needed
        if (left < right) {
            double temp = left;
            left = right;
            right = temp;
        }
        if (top < bottom) {
            double temp = top;
            top = bottom;
            bottom = temp;
        }

        // Extract color components
        float alpha = (float)(color.getRGB() >> 24 & 0xFF) / 255.0f;
        float red = (float)(color.getRGB() >> 16 & 0xFF) / 255.0f;
        float green = (float)(color.getRGB() >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color.getRGB() & 0xFF) / 255.0f;

        // Setup rendering
        RenderSystem.enableBlend();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();

        // Draw the outline rectangle
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP,VertexFormats.POSITION_COLOR);

        buffer.vertex((float) left, (float) top, 0).color(red, green, blue, alpha);
        buffer.vertex((float) right,(float)  top, 0).color(red, green, blue, alpha);
        buffer.vertex((float) right,(float)  bottom, 0).color(red, green, blue, alpha);
        buffer.vertex((float) left, (float) bottom, 0).color(red, green, blue, alpha);
        buffer.vertex((float) left, (float) top, 0).color(red, green, blue, alpha); // Close the loop
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Clean up
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}