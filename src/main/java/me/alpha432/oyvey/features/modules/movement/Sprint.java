package me.alpha432.oyvey.features.modules.movement;


import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;


public class Sprint extends Module {
    public Setting<Integer> motion = num("Motion", 1, 0, 1);

    public Sprint() {
        super("Sprint", "", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (!mc.player.horizontalCollision && mc.player.forwardSpeed > 0 && !mc.player.isSneaking() && !mc.player.isUsingItem()) {
            mc.player.setSprinting(true);

        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}

