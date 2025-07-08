package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class Notifications extends Module {
    private final ArrayList<PlayerEntity> rangeplayers = new ArrayList<>();
    public Setting<Boolean> visualrange = bool("VisualRange", true);

    public Notifications() {
        super("Notifications", "", Category.CLIENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (visualrange.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity player) || entity == mc.player) continue;
                if (!rangeplayers.contains(player)) {
                    rangeplayers.add(player);
                    String message = Formatting.WHITE + player.getName().getString() + Formatting.RED + " has entered your visual range!";
                    Command.sendMessage1(ChatUtil.getRainbow() + OyVey.commandManager.getClientMessage() + " " + message);
                }
            }
            if (!rangeplayers.isEmpty()) {
                for (PlayerEntity player : new ArrayList<>(rangeplayers)) {
                    if (!mc.world.getPlayers().contains(player)) {
                        rangeplayers.remove(player);
                        String message = Formatting.WHITE + player.getName().getString() + Formatting.RED + " has left your visual range!";
                        Command.sendMessage1(ChatUtil.getRainbow() + OyVey.commandManager.getClientMessage() + " " + message);
                    }
                }
            }
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}