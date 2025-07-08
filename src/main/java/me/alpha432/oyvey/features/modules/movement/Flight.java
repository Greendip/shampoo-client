package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.util.math.Vec3d;


public class Flight extends Module {
    private final Setting<Float> horizontal = num("Vertical", 4f, 1f, 6f);
    private final Setting<Float> vertical = num("Horizontal", 4f, 1f, 6f);
    public Setting<Flight.Mod> mode = this.register(new Setting<>("Mode", Mod.Vanilla));
    private final Setting<Integer> speed = num("Speed:", 4, 1, 6);
    public enum Mod {
        Vanilla, Static
    }
    public Flight() {
        super("Flight", "", Category.MOVEMENT, true, false, false);

    }
    @Override public void onTick() {
        switch (mode.getValue()) {
            case Vanilla -> {
                Vec3d move = Vec3d.ZERO;
                Float y = 0f;
                if (mc.options.forwardKey.isPressed()) {
                    move = move.add(Vec3d.fromPolar(0, mc.player.getYaw()).normalize());
                }
                if (mc.options.backKey.isPressed()) {
                    move = move.add(Vec3d.fromPolar(0, mc.player.getYaw()).normalize());
                }
                if (mc.options.rightKey.isPressed()) {
                    move = move.add(Vec3d.fromPolar(0, mc.player.getYaw()).normalize());
                }
                if (mc.options.leftKey.isPressed()) {
                    move = move.add(Vec3d.fromPolar(0, mc.player.getYaw()).normalize());
                }
                if (mc.options.jumpKey.isPressed()) {
                    y = vertical.getValue();

                }
                if (mc.options.sneakKey.isPressed()) {
                    y = -vertical.getValue();

                }
                move = move.normalize().multiply(horizontal.getValue());
                mc.player.setVelocity(move.x, y, move.z);
            }
            case Static -> {
                mc.player.getAbilities().setFlySpeed((float) (speed.getValue()));
                mc.player.getAbilities().flying = true;
                if (mc.options.jumpKey.isPressed()) mc.player.getAbilities().setFlySpeed((float) (speed.getValue() / 4));
                if (mc.options.sneakKey.isPressed()) mc.player.getAbilities().setFlySpeed((float) (speed.getValue() / 4));
                mc.player.setVelocity(0, 0, 0);
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