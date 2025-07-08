package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class AutoLog extends Module {
    private final Setting<Integer> hp = num("Healths:", 2, 1, 36);
    private final Setting<Integer> totem = num("Totems:", 2, 1, 36);



    public AutoLog() {
        super("AutoLog", "", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (mc.player.getHealth() <= hp.getValue()) {
            mc.getNetworkHandler().getConnection().disconnect(Text.of("У вас маленькое количество здоровья! " + (int) mc.player.getHealth()));
            disable();
        }
        if (mc.player.getInventory().count(Items.TOTEM_OF_UNDYING) == totem.getValue()) {
            mc.getNetworkHandler().getConnection().disconnect(Text.of("У вас маленькое количество тотемов!"));
            disable();
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

}


