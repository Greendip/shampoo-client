package me.alpha432.oyvey.features.modules.player;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.mixin.EntityVelocityUpdateS2CPacketAccessor;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;

public class Velocity extends Module {
    private final Setting<Integer> hort = num("Vertical", 0, 0, 100);
    private final Setting<Integer> vert = num("Horizontal", 0, 0, 100);
    public Setting<Boolean> blockPush = bool("BlockPush", true);
    public Setting<Boolean> entityPush = bool("EntityPush", true);
    public Setting<Boolean> explosion = bool("Explosion", true);

    public Velocity() {
        super("Velocity", "Removes velocity from explosions and entities", Category.PLAYER, true, false, false);
    }

    @Subscribe
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.getEntityId() == mc.player.getId()) {
            Vec3d velocity = mc.player.getVelocity();
            double x = (packet.getVelocityX() / 8000d) - (velocity.x * hort.getValue());
            double y = (packet.getVelocityY() / 8000d) - (velocity.y * vert.getValue());
            double z = (packet.getVelocityZ() / 8000d) - (velocity.z * hort.getValue());

            ((EntityVelocityUpdateS2CPacketAccessor) packet).setX((int) ((x + velocity.x) * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setY((int) ((y + velocity.y) * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setZ((int) ((z + velocity.z) * 8000));
        }
    }

    @Override
    public String getDisplayInfo() {
        return "H: " + hort.getValue() + " V: " + vert.getValue();
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}






