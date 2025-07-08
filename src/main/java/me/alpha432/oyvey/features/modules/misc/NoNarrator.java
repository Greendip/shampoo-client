package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.option.NarratorMode;

public class NoNarrator extends Module {
    public NoNarrator(){
        super("NoNarrator","disable narrator",Category.MISC,true,false,false);
    }

    @Override
    public void onTick(){
        if (fullNullCheck()) return;

        mc.options.getNarrator().setValue(NarratorMode.OFF);
    }
}
