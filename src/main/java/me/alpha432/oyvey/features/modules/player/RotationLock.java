package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class RotationLock extends Module {
    public Setting<Integer> yaw = num("Yaw", 90, 1, 180);
    public Setting<Integer> pitch = num("Pitch", 90, 1, 180);
    public RotationLock() {
        super("RotationLock", "", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        if (nullCheck()) return;
      mc.player.setYaw(yaw.getValue());
      mc.player.setPitch(pitch.getValue());

    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
