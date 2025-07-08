package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class AutoExp extends Module {

    public AutoExp() {
        super("AutoExp", "", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        int oldslot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtil.findHotbarItem(ExperienceBottleItem.class);
        if (slot != -1)  {
            InventoryUtil.switchSlot(slot);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            InventoryUtil.switchSlot(oldslot);
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
