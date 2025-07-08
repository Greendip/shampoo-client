package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class FastFall extends Module {
    public FastFall() {
        super("FastFall", "step but reversed..", Category.MOVEMENT, true, false, false);
    }

    @Override public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.isInLava() || mc.player.isTouchingWater() || !mc.player.isOnGround() || mc.player.isInLava() || mc.player.isGliding() || mc.player.getAbilities().flying) return;
        mc.player.addVelocity(0, -1, 0);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
