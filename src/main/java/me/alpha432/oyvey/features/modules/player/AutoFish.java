package me.alpha432.oyvey.features.modules.player;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish", "", Category.PLAYER, true,false,false);
    }
    @Subscribe public void onPacket(PacketEvent.Send event) {
        if (fullNullCheck()) return;
        if (event.getPacket()instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
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
