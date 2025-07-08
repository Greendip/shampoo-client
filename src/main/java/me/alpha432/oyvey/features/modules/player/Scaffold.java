package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InteractionUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Scaffold extends Module {
    BlockPos pos;

    public Scaffold() {
        super("Scaffold", "", Category.PLAYER, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        int oldslot = mc.player.getInventory().selectedSlot;
        int slot = InventoryUtil.findHotbarItem(BlockItem.class);
        pos = mc.player.getBlockPos().down();
        if (slot != -1) {
            InventoryUtil.switchSlot(slot);
            InteractionUtil.placeblock(pos, false);
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
