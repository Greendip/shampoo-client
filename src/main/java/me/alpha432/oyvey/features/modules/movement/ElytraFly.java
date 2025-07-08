package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;


public class ElytraFly extends Module {
    public Setting<ElytraFly.Mod> mode = this.register(new Setting<>("Mode", Mod.Control));

    public enum Mod {
        Control, Bounce
    }

    private final Setting<Float> horizontal = num("Vertical", 4f, 1f, 6f);
    private final Setting<Float> vertical = num("Horizontal", 4f, 1f, 6f);

    public ElytraFly() {
        super("ElytraFly", "", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mode.getValue() == Mod.Control) {
            if (mc.player.isGliding()) {
                Vec3d moveVec = Vec3d.ZERO;
                Float y = 0f;

                if (mc.options.forwardKey.isPressed()) {
                    moveVec = moveVec.add(Vec3d.fromPolar(0, mc.player.getYaw()).normalize());
                }
                if (mc.options.backKey.isPressed()) {
                    moveVec = moveVec.add(Vec3d.fromPolar(0, mc.player.getYaw() + 180).normalize());
                }

                if (mc.options.rightKey.isPressed()) {
                    moveVec = moveVec.add(Vec3d.fromPolar(0, mc.player.getYaw() + 90).normalize());
                }
                if (mc.options.leftKey.isPressed()) {
                    moveVec = moveVec.add(Vec3d.fromPolar(0, mc.player.getYaw() - 90).normalize());
                }

                if (mc.options.jumpKey.isPressed()) {
                    y = vertical.getValue();
                }

                if (mc.options.sneakKey.isPressed()) {
                    y = -vertical.getValue();
                }


                moveVec = moveVec.normalize().multiply(horizontal.getValue());
                mc.player.setVelocity(moveVec.x, y, moveVec.z);

            }
        }
        if (mode.getValue() == Mod.Bounce) {
            if (mc.player.isGliding() && mc.player.isOnGround()) {
                mc.player.startGliding();
                mc.player.jump();
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
        }
    }
    @Override public String getDisplayInfo() {
        return mode.getValue().toString();
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
