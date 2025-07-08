package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

import static net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.getServer;
import static org.apache.logging.log4j.LogManager.getLogger;

public class NolikIq extends Module {
    public NolikIq() {
        super("NolikIq", "", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable(){
        if (fullNullCheck())return;
        mc.player.networkHandler.sendChatMessage("Айкью нолика равно ццшко");
        disable();
    }
}
