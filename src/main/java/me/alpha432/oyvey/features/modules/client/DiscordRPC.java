package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.discord.DiscordEventHandlers;
import me.alpha432.oyvey.util.discord.DiscordRichPresence;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public class DiscordRPC extends Module {
    public static me.alpha432.oyvey.util.discord.DiscordRPC rpc = me.alpha432.oyvey.util.discord.DiscordRPC.INSTANCE;
    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static boolean started;
    public static Thread thread;
    public DiscordRPC(){
        super("DiscordRPC","Display status in Discord",Category.CLIENT,true,false,false);
    }

    @Override
    public void onDisable() {
        started = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    @Override
    public void onUpdate() {
        startRpc();
    }

    public void startRpc() {
        if (isDisabled()) return;
        if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1382009325945487420", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = OyVey.NAME + " " + OyVey.VERSION;
            rpc.Discord_UpdatePresence(presence);
            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();
                    presence.details = getDetails();
                    presence.state = OyVey.NAME + " " + OyVey.VERSION;
                    presence.largeImageKey = "https://i.postimg.cc/MHnpPwP3/223-20250621044828.png";
                    rpc.Discord_UpdatePresence(presence);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Handler");
            thread.start();
        }
    }

    private String getDetails() {
        String result = "";
        if (mc.currentScreen instanceof TitleScreen) {
            result = "In Title Screen";
        } else if (mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof AddServerScreen) {
            result = "Picks a server";
        } else if (mc.getCurrentServerEntry() != null) {
            result = "Playing on " + mc.getCurrentServerEntry().address;
        } else if (mc.isInSingleplayer()) {
            result = "In SinglePlayer";
        }
        return result;
    }
}
