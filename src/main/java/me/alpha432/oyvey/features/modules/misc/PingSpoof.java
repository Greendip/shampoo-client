package me.alpha432.oyvey.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSpoof extends Module {
    public Setting<Integer> delay = num("Delay", 10, 1, 2000);

    private final ConcurrentLinkedQueue<DelayedPacket> queue = new ConcurrentLinkedQueue<>();
    public PingSpoof() {
        super("PingSpoof", "More ping", Category.MISC, true, false, false);
    }
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fullNullCheck())return;
        if (event.getPacket() instanceof KeepAliveS2CPacket packet) {
            event.setCancelled(true);
            queue.add(new DelayedPacket(packet, System.currentTimeMillis()));
        }
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return;

        DelayedPacket packet = queue.peek();
        if (packet == null) return;

        if (System.currentTimeMillis() - packet.time() >= delay.getValue().intValue()) {
            sendPacket(new KeepAliveC2SPacket(queue.poll().packet().getId()));
        }
    }
    private record DelayedPacket(KeepAliveS2CPacket packet, long time) {}
}
