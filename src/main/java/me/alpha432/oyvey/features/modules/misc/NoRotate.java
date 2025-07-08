package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.mixin.PlayerPositionMixin;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", "", Category.MISC, true, false, false);
    }

    @Subscribe
    public void onPacket(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            ((PlayerPositionMixin) (Object) packet.change()).setYaw(mc.player.getYaw());
            ((PlayerPositionMixin) (Object) packet.change()).setPitch(mc.player.getPitch());
            packet.relatives().remove(PositionFlag.X_ROT);
            packet.relatives().remove(PositionFlag.Y_ROT);


        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
