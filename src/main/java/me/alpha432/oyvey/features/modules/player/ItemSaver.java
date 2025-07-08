package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;

public class ItemSaver extends Module {
    public ItemSaver() {
        super("ItemSaver", "", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        ItemStack tool = mc.player.getMainHandStack();
        if (!(tool.getItem() instanceof MiningToolItem)) return;
        float dur = tool.getMaxDamage() - tool.getDamage();
        int prch = (int) ((dur / (float) tool.getMaxDamage() * 100));
        if (prch <= 10) {
            mc.player.getInventory().selectedSlot = findSlot();
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    public static int findSlot() {
        int i = mc.player.getInventory().selectedSlot;
        if (i == 8) return 7;
        if (i == 0) return 1;
        return i - 1;
    }
}
