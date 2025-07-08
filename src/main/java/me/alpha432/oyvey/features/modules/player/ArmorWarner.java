package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class ArmorWarner extends Module {
    private final Setting<Integer> armorThreshold = num("Armor%", 20, 1, 100);
    private final Setting<Boolean> notifySelf = bool("Self", true);
    private final Setting<Boolean> notification = bool("Friends", true);

    private final Map<PlayerEntity, Integer> entityArmorArraylist = new HashMap<>();
    public ArmorWarner(){
        super("ArmorWarner","Warns you and your friends of the strength of the armor",Category.PLAYER,true,false,false);
    }
    @Override
    public void onUpdate() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.isDead() || !OyVey.friendManager.isFriend(player.getName().getString())) continue;

            for (ItemStack stack : player.getInventory().armor) {
                if (stack == ItemStack.EMPTY) continue;

                int percent = getDamagePercent(stack);

                if (percent <= armorThreshold.getValue() && !entityArmorArraylist.containsKey(player)) {

                    if (player == mc.player && notifySelf.getValue()) {
                        Command.sendMessage(Formatting.RED + "Your " + getArmorPieceName(stack) + " low durability!");
                    }

                    if (OyVey.friendManager.isFriend(player.getName().getString()) && notification.getValue() && player != mc.player) {
                        mc.player.networkHandler.sendChatCommand("msg " + player.getName().getString() + " Yo, " + player.getName().getString() + ", ur " + getArmorPieceName(stack) + " low durability!");
                    }

                    entityArmorArraylist.put(player, player.getInventory().armor.indexOf(stack));
                }
                if (!entityArmorArraylist.containsKey(player) || entityArmorArraylist.get(player) != player.getInventory().armor.indexOf(stack) || percent <= armorThreshold.getValue()) continue;

                entityArmorArraylist.remove(player);
            }
            if (!entityArmorArraylist.containsKey(player) || player.getInventory().armor.get(entityArmorArraylist.get(player)) != ItemStack.EMPTY) continue;

            entityArmorArraylist.remove(player);
        }
    }

    public static int getDamagePercent(ItemStack stack) {
        return (int) ((stack.getMaxDamage() - stack.getDamage()) / Math.max(0.1, stack.getMaxDamage()) * 100.0f);
    }


    private String getArmorPieceName(ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET
                || stack.getItem() == Items.GOLDEN_HELMET
                || stack.getItem() == Items.IRON_HELMET
                || stack.getItem() == Items.CHAINMAIL_HELMET
                || stack.getItem() == Items.LEATHER_HELMET) {

            return "helmet is";
        }

        if (stack.getItem() == Items.DIAMOND_CHESTPLATE
                || stack.getItem() == Items.GOLDEN_CHESTPLATE
                || stack.getItem() == Items.IRON_CHESTPLATE
                || stack.getItem() == Items.CHAINMAIL_CHESTPLATE
                || stack.getItem() == Items.LEATHER_CHESTPLATE) {

            return "chest is";
        }

        if (stack.getItem() == Items.DIAMOND_LEGGINGS
                || stack.getItem() == Items.GOLDEN_LEGGINGS
                || stack.getItem() == Items.IRON_LEGGINGS
                || stack.getItem() == Items.CHAINMAIL_LEGGINGS
                || stack.getItem() == Items.LEATHER_LEGGINGS) {

            return "leggings are";
        }

        return "boots are";
    }
}