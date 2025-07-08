package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InteractionUtility;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AutoConcrete extends Module {
    private final Setting<Float> range = register(new Setting<>("Range",4.5f,1f,6f));
    private final Setting<Boolean> airplace = register(new Setting<>("AirPlace",true));
    private boolean succes;
    public AutoConcrete(){
        super("AutoConcrete","",Category.COMBAT,true,false,false);
    }

    @Override
    public void onEnable(){
        if (nullCheck() && fullNullCheck()) return;
        succes = false;
        int result = InventoryUtil.findHotbarItem(ConcretePowderBlock.class);
        int oldslot = mc.player.getInventory().selectedSlot;
        if (result == -1) disable();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getBlockStateAtPos().isAir()) {
                if (!succes) {
                    if (player == mc.player || OyVey.friendManager.isFriend(player.getName().getString()) || mc.player.distanceTo(player) > range.getValue()) continue;
                    InventoryUtil.switchSlot(result);
                    BlockPos pos = player.getBlockPos().up().up();
                    placeConcrete(pos);
                    InventoryUtil.switchSlot(oldslot);
                    succes = true;
                }
                else disable();
            }
        }
    }
    private void placeConcrete(BlockPos pos){
        InteractionUtility.placeBlock(pos, airplace.getValue() ? InteractionUtility.Interact.AirPlace : InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Normal, false);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}