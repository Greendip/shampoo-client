package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class AutoWalk extends Module {
    private final Setting<Boolean> autoJump = this.register(new Setting<>("AutoJump", false));

    public AutoWalk() {
        super("AutoWalk", "", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        mc.options.forwardKey.setPressed(true);
        if (autoJump.getValue())
        mc.options.jumpKey.setPressed(true);


    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);

    }
}


