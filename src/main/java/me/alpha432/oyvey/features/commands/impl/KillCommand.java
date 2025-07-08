package me.alpha432.oyvey.features.commands.impl;

import me.alpha432.oyvey.features.commands.Command;

public class KillCommand extends Command {
    public KillCommand() {
        super("kill");
    }

    @Override
    public void execute(String[] commands) {
        mc.player.networkHandler.sendCommand("kill");
    }
}
