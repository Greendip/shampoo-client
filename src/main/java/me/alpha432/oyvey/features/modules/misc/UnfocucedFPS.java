package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class UnfocucedFPS extends Module {
    public final Setting<Integer> limit = num("Limit", 5, 1, 30);
    public UnfocucedFPS() {
        super("UnfocucedFPS", "", Category.MISC, true,false,false);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
