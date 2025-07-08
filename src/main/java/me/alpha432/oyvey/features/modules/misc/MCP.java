package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class MCP extends Module {
    private boolean pressed;

    public MCP() {
        super("MCP", "Throw pearl on midlle click", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        int slot = InventoryUtil.findHotbarItem(EnderPearlItem.class);

        int prevslot = mc.player.getInventory().selectedSlot;
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 2) == 1) {
            if (!pressed) {
                InventoryUtil.switchSlot(slot);
                sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
                InventoryUtil.switchSlot(prevslot);
                pressed = true;
            }
        } else pressed = false;
    }
}