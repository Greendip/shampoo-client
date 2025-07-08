package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.Objects;

public class ChatSuffix extends Module {
    private final Setting<String> messages = this.register(new Setting<>("Message:", "| ShampooClient"));


    public ChatSuffix() {
        super("ChatSuffix", "", Category.MISC, true,false,false);
    }
    private String message;
    @Subscribe
    public void onPacket(PacketEvent event) {
        if (fullNullCheck()) return;
        if (OyVey.moduleManager.getModuleByClass(Spammer.class).isEnabled()) return;
        if (OyVey.moduleManager.getModuleByClass(AntiAFK.class).isEnabled()) return;
        if (event.getPacket() instanceof ChatMessageC2SPacket packet) {
            if (Objects.equals(packet.chatMessage(), message)) {
                return;
            }
            message = packet.chatMessage() + " " + messages.getValue();
            mc.player.networkHandler.sendChatMessage(packet.chatMessage() + " " + messages.getValue());
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
