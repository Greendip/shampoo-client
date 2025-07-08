package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AutoWin extends Module {
    public AutoWin() {
        super("AutoWin","",Category.MISC,true,false,false);
    }
    public Setting<Integer> time = num("Minutes:", 1, 1, 5);

    @Subscribe
    public void onEnable() {
        int minutes = time.getValue() ;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                ProcessBuilder processBuilder = new ProcessBuilder("shutdown",
                        "/s");
                try {
                    processBuilder.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }, minutes * 60 * 1000);

        Command.sendMessage(Formatting.DARK_GREEN + "Win in " + minutes + " minutes");
        disable();
    }
}
