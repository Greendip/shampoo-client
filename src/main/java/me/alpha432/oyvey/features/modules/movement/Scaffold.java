package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InteractionUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class Scaffold extends Module {

    public Setting<Boolean> airPlace = bool("AirPlace",true);

    public Scaffold(){
        super("Scaffold","Places Blocks down",Category.MOVEMENT,true,false,false);
    }

    @Override
    public void onTick(){
        if (!fullNullCheck()) {

            int oldSlot = mc.player.getInventory().selectedSlot;
            int slot = InventoryUtil.findHotbarItem(BlockItem.class);
            BlockPos pos = mc.player.getBlockPos().down();

            if (slot == -1) return;
            if (mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
                InventoryUtil.switchSlot(slot);

                InteractionUtil.place(pos, airPlace.getValue());

                InventoryUtil.switchSlot(oldSlot);
            }
        }
    }
}
