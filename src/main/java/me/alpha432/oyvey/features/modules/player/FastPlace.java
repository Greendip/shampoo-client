package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.item.Items;

public class FastPlace extends Module {
    public Setting<Boolean> exp = bool("Exp", true);
    public Setting<Boolean> crystals = bool("Crystals", true);
    public Setting<Boolean> obsidian = bool("Obsidian", true);

    public FastPlace() {
        super("FastPlace", "Makes you throw exp faster", Category.PLAYER, true, false, false);
    }
    @Override public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.isHolding(Items.END_CRYSTAL) && crystals.getValue()) {
            mc.itemUseCooldown = 0;
        }
        if (mc.player.isHolding(Items.OBSIDIAN) && obsidian.getValue()) {
            mc.itemUseCooldown = 0;
        }
        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE) && exp.getValue()) {
            mc.itemUseCooldown = 0;
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}