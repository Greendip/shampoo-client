package me.alpha432.oyvey.features.modules.client;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = str("Prefix", ".");
    public Setting<Boolean> gear = bool("Gear", false);
    public Setting<Boolean> catcount = bool("CategoryCount", false);
    public Setting<Boolean> desk = bool("Desс", false);
    public Setting<Boolean> outline = bool("Outline", false);
    public final Setting<Integer> height = register(new Setting<>("ButtonHeight", 4, 1, 5));
    public Setting<Color> color = color("Color",new Color(121,135,242,128)).hideAlpha();
    public Setting<Color> catcolor = color("CategoryColor",new Color(121,135,242,128)).hideAlpha();
    public Setting<Integer> alpha = num("HoverAlpha", 240, 0, 255);
    public Setting<Integer> alpha1 = num("Alpha", 150, 0, 255);
    public Setting<Boolean> rainbow = bool("Rainbow", false);

    public Setting<Integer> rainbowHue = num("Delay", 240, 0, 600);
    public Setting<Float> rainbowBrightness = num("Brightness ", 150.0f, 1.0f, 255.0f);
    public Setting<Float> rainbowSaturation = num("Saturation", 150.0f, 1.0f, 255.0f);
 //   private OyVeyGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
        rainbowHue.setVisibility(v -> rainbow.getValue());
        rainbowBrightness.setVisibility(v -> rainbow.getValue());
        rainbowSaturation.setVisibility(v -> rainbow.getValue());
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Subscribe
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + Formatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.color.getValue().getAlpha());
        }
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        mc.setScreen(OyVeyGui.getClickGui());
    }

    @Override public void onDisable() {
        mc.setScreen(null);
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.color.getValue().getAlpha());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }
    }
    public int getButtonHeight() {
        return 11 + height.getValue();
    }
}