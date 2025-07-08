package me.alpha432.oyvey.features.commands.impl;

import me.alpha432.oyvey.features.commands.Command;
import net.minecraft.util.Util;

public class FolderCommand extends Command {
    public FolderCommand() {
        super("folder");
    }
    @Override
    public void execute(String[] var1) {
        Util.getOperatingSystem().open("Impossible");
    }
}
