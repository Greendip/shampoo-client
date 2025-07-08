package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class ViewClip extends Module {
    public final Setting<Float> range = num("Range", 3f, 1f, 10f);
    public Setting<Boolean> player = bool("PlayerDistance", true);
    public ViewClip() {
        super("ViewClip", "", Category.RENDER, true,false,false);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
