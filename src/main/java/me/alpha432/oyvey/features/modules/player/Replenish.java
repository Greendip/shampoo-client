package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class Replenish extends Module {
    public Setting<Float> delay = this.register(new Setting<>("Delay", 0f, 0.1f, 2f));
    public Setting<Integer> min = this.register(new Setting<>("Min", 1, 50, 64));
    public Setting<Float> forceDelay = this.register(new Setting<>("ForceDelay", 0.2f, 0.1f, 0.4f));
    public Setting<Integer> forceMin = this.register(new Setting<>("ForceMin", 1, 16, 64));
    private final Timer timer = new Timer();
    public Replenish(){
        super("Replenish","",Category.PLAYER,true,false,false);
    }
    @Override
    public void onUpdate() {
        /*        if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGuiScreen)) return;*/
        for (int i = 0; i < 9; ++i) {
            if (replenish(i)) {
                timer.reset();
                return;
            }
        }
    }

    private boolean replenish(int slot) {
        ItemStack stack = mc.player.getInventory().getStack(slot);

        if (stack.isEmpty()) return false;
        if (!stack.isStackable()) return false;
        if (stack.getCount() > min.getValue()) return false;
        if (stack.getCount() == stack.getMaxCount()) return false;

        for (int i = 9; i < 36; ++i) {
            ItemStack item = mc.player.getInventory().getStack(i);
            if (item.isEmpty() || !canMerge(stack, item)) continue;
            if (stack.getCount() > forceMin.getValue()) {
                if (!timer.passedS(delay.getValue())) {
                    return false;
                }
            } else {
                if (!timer.passedS(forceDelay.getValue())) {
                    return false;
                }
            }
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
            return true;
        }
        return false;
    }

    private boolean canMerge(ItemStack source, ItemStack stack) {
        return source.getItem() == stack.getItem() && source.getName().equals(stack.getName());
    }
}
