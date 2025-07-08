package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "", Category.PLAYER, true,false,false);
    }
    @Override
    public void onTick() {
        if (nullCheck())
            return;
        if (mc.currentScreen instanceof DeathScreen)
        {
            mc.player.requestRespawn();

        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}