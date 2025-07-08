package me.alpha432.oyvey.util;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;


public class InventoryUtil implements Util {
    public static void switchSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static int findHotbarItem(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }
    public static int count(Item item)
    {
        if (mc.player == null)
        {
            return 0;
        }
        ItemStack offhandStack = mc.player.getOffHandStack();
        int itemCount = offhandStack.getItem() == item ? offhandStack.getCount() : 0;
        for (int i = 0; i < 36; i++)
        {
            ItemStack slot = mc.player.getInventory().getStack(i);
            if (slot.getItem() == item)
            {
                itemCount += slot.getCount();
            }
        }
        return itemCount;
    }
    public static boolean hasItemInInventory(final Item item, final boolean hotbar)
    {
        final int startSlot = hotbar ? 0 : 9;
        for (int i = startSlot; i < 36; ++i)
        {
            final ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == item)
            {
                return true;
            }
        }
        return false;
    }
    public static void setClientSlot(final int barSlot)
    {
        if (mc.player.getInventory().selectedSlot != barSlot
                && PlayerInventory.isValidHotbarIndex(barSlot))
        {
            mc.player.getInventory().selectedSlot = barSlot;
            setSlotForced(barSlot);
        }
    }
    public static void setSlotForced(final int barSlot)
    {
      mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(barSlot));
    }
}
