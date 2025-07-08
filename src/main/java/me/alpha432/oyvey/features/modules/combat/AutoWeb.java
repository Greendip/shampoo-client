package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InteractionUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AutoWeb extends Module {
    private final Setting<Float> range = num("Range", 4f, 1f, 6f);
    public AutoWeb() {
        super("AutoWeb", "", Category.COMBAT, true,false,false);
    }
    @Override public void onTick() {
        if (fullNullCheck()) return;
        int oldslot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtil.findHotbarItem(CobwebBlock.class);
        for (PlayerEntity player: mc.world.getPlayers()) {
            if (player == mc.player || OyVey.friendManager.isFriend(player.getName().getString())) continue;
            if (mc.player.distanceTo(player) <= range.getValue()) {
                BlockPos pos = player.getBlockPos();
                if (slot != -1) {
                    InventoryUtil.switchSlot(slot);
                    InteractionUtil.placeblock(pos, false);
                    InteractionUtil.placeblock(pos.up(1), false);
                    InventoryUtil.switchSlot(oldslot);
                }
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
