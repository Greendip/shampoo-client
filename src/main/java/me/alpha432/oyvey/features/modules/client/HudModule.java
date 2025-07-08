package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.Collectors;

public class HudModule extends Module {
    private final Setting<Boolean> watermark = bool("WaterMark",true);
    private final Setting<Boolean> uid = bool("UID",true);
    private final Setting<Boolean> greter = bool("Greater",true);
    private final Setting<Boolean> arraylist = bool("ArrayList",true);
    private final Setting<Boolean> gradient = bool("Gradient",true);
    private final Setting<Color> color = color("Color ",new Color(-1));
    private final Setting<Color> color1 = color("ColorSecondary",new Color(-1));
    public HudModule() {
        super("HUD", "hud", Category.CLIENT, true, false, false);
    }

    @Override public void onRender2D(Render2DEvent event) { //оооооооооо
        int width = mc.getWindow().getScaledWidth(); // ширина
        int height = mc.getWindow().getScaledHeight(); // высота
        boolean nether = mc.world.getRegistryKey() == World.NETHER;
        int y = 0;
        int j = (mc.currentScreen instanceof ChatScreen) ? 13 : 0;
        if (watermark.getValue()) {
            if (gradient.getValue()) TextUtil.drawStringPulse(event.getContext(), OyVey.NAME + Formatting.WHITE + OyVey.VERSION, 2, 2,color.getValue(),color1.getValue(),1,10);
            else event.getContext().drawTextWithShadow(mc.textRenderer,OyVey.NAME + Formatting.WHITE + OyVey.VERSION, 2, 2,color1.getValue().getRGB());
        }
        if (uid.getValue()) {
            String uid =  String.valueOf(0);

            if (mc.player.getName().getString().equals("4asik1488") || mc.player.getName().getString().equals("4asik")) uid =  String.valueOf(1);

            if (mc.player.getName().getString().equals("kormax")) uid =  String.valueOf(2);

            if (mc.player.getName().getString().equals("remykarl")) uid =  String.valueOf(3);

            if (mc.player.getName().getString().equals("SelfOffensive")) uid =  String.valueOf(4);

            if (mc.player.getName().getString().equals("mioclientuser")) uid =  String.valueOf(5);

            if (mc.player.getName().getString().equals("volk58")) uid =  String.valueOf(6);

            if (mc.player.getName().getString().equals("cattyyyn")) uid = "❄";

            if (gradient.getValue()) TextUtil.drawStringPulse(event.getContext(), "UID " + Formatting.WHITE + uid, 2, 13, color.getValue(),color1.getValue(),1,10);
            else event.getContext().drawTextWithShadow(mc.textRenderer,"UID " + Formatting.WHITE + uid, 2, 13,color1.getValue().getRGB());
        }

        if (greter.getValue()) {
            if (gradient.getValue()) TextUtil.drawStringPulse(event.getContext(), "good to see you, " + Formatting.WHITE + mc.player.getName().getString(), (width / 2) - mc.textRenderer.getWidth("good to see you, " + mc.player.getName().getString()) / 2, 2, color.getValue(), color1.getValue(), 1, 10);
            else event.getContext().drawTextWithShadow(mc.textRenderer,"good to see you, " + Formatting.WHITE + mc.player.getName().getString(),(width / 2) - mc.textRenderer.getWidth("good to see you, " + mc.player.getName().getString()) / 2, 2,color1.getValue().getRGB());
        }

        if (arraylist.getValue()) {
            for (Module module : OyVey.moduleManager.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> mc.textRenderer.getWidth(module.getFullArrayString()) * -1)).collect(Collectors.toList())) {
                if (!module.isDrawn()) continue;
                String str = module.getName() + Formatting.GRAY + (module.getDisplayInfo() != null ? " [" + Formatting.WHITE + module.getDisplayInfo() + Formatting.GRAY + "]" : "");
                if (gradient.getValue()) TextUtil.drawStringPulse(event.getContext(), str, (int) ((width - mc.textRenderer.getWidth(str) - 2f)), (2 + y * 10), color.getValue(), color1.getValue(), 1, 10);
                else event.getContext().drawTextWithShadow(mc.textRenderer,str,(int) ((width - mc.textRenderer.getWidth(str) - 2f)), (2 + y * 10),color1.getValue().getRGB());
                y++;
            }
        }
    }

    public String getDirection4D() {
        int yaw = getYaw4D();

        if (yaw == 0) {
            return "South (+Z)";
        }
        if (yaw == 1) {
            return "West (-X)";
        }
        if (yaw == 2) {
            return "North (-Z)";
        }
        if (yaw == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }

    private int getYaw4D() {
        return MathHelper.floor((double) (mc.player.getYaw() * 4.0f / 360.0f) + 0.5) & 3;
    }
}