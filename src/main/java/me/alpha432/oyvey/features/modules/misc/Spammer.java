package me.alpha432.oyvey.features.modules.misc;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.models.Timer;

import java.util.Random;

public class Spammer extends Module {
    private Setting<Integer> delay = num("Delay:", 5, 1, 30);
    private Timer timer = new Timer();


    public Spammer() {
        super("Spammer", "", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (timer.passedS(delay.getValue())) {
            mc.player.networkHandler.sendChatMessage(walkingmessage());
            timer.reset();
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    private String walkingmessage() {
        String[] walking = {
                "ShampooClient on tope",
        };
        return walking[new Random().nextInt(walking.length)];
    }
}
