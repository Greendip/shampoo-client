package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class MiddleClick extends Module {
    public Setting<Boolean> friend = bool("Friend", true);
    public Setting<Boolean> pearl = bool("Pearl", true);
    public Setting<Boolean> fifework = bool("Fifework", true);
    private boolean pressed;

    public MiddleClick() {
        super("MiddleClick", "Middle click friend", Category.MISC, true, false, false);
    }

    @Override public void onTick() {
        if (mc.currentScreen instanceof ChatScreen) return;
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 2) == 1) {
            if (!pressed) {
                if (pearl.getValue()) {
                    int oldslot = mc.player.getInventory().selectedSlot;
                    int slot = InventoryUtil.findHotbarItem(EnderPearlItem.class);
                    if (slot != -1) {
                        InventoryUtil.switchSlot(slot);
                        sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
                        InventoryUtil.switchSlot(oldslot);
                    }
                }
                if (fifework.getValue()) {
                    int oldslot = mc.player.getInventory().selectedSlot;
                    int slot = InventoryUtil.findHotbarItem(FireworkRocketItem.class);
                    if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
                        if (slot != -1) {
                            InventoryUtil.switchSlot(slot);
                            sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
                            InventoryUtil.switchSlot(oldslot);
                        }
                    }
                }
                if (friend.getValue()) {
                    Entity targetedEntity = mc.targetedEntity;
                    if (!(targetedEntity instanceof PlayerEntity)) return;
                    String name = ((PlayerEntity) targetedEntity).getGameProfile().getName();

                    if (OyVey.friendManager.isFriend(name)) {
                        OyVey.friendManager.removeFriend(name);
                        Command.sendMessage(Formatting.RED + name + Formatting.RED + " has been unfriended.");
                    } else {
                        OyVey.friendManager.addFriend(name);
                        Command.sendMessage(Formatting.AQUA + name + Formatting.AQUA + " has been friended.");
                    }
                }

                pressed = true;
            }
        } else {
            pressed = false;
        }
    }
}