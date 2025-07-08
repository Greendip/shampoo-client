package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.ChatEvent;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.Objects; // я хуй знает эта хуета не работает (почти)

public class BetterChat extends Module {
    private Setting<Boolean> suffix = bool("Suffix",true);
    private Setting<String> message = str("Message","| shampooclient");
    private String ss;
    public BetterChat(){
        super("BetterChat","Chat Tweaks",Category.MISC,true,false,false);
        message.setVisibility(v-> suffix.getValue());
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event){
        if (nullCheck()) return;

        if (event.getPacket() instanceof ChatMessageC2SPacket packet){
            if (Objects.equals(packet.chatMessage(), ss)) return;

            if (packet.chatMessage().startsWith("/") || packet.chatMessage().startsWith(OyVey.commandManager.getPrefix()) || !suffix.getValue()) return;

            ss = packet.chatMessage() + " " + message.getValue();
            mc.player.networkHandler.sendChatMessage(packet.chatMessage() + " " + message.getValue());
            event.cancel();
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
