package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.BlockRenderUtil;
import me.alpha432.oyvey.util.InteractionUtil;
import me.alpha432.oyvey.util.InteractionUtility;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class AutoBuilder extends Module {
    public Setting<Mode> mode = mode("Mode", Mode.Pisun);
    public Setting<Boolean> rotate = bool("Rotate",false);
    public Setting<Boolean> airplace = bool("AirPlace",false);
    private Setting<Float> bps = num("BPS",5f,0f,20f);
    public Setting<Boolean> render = bool("Render",false);
    private List<BlockPos> positions = new ArrayList<>();

    public enum Mode {
        Svaston,
        Portal,
        Pisun
    }

    private boolean isSucces;
    private int blocksInTick = 0;
    public AutoBuilder(){
        super("AutoBuilder","Build differnt structures",Category.MISC,true,false,false);
    }

    @Override
    public void onEnable(){
        setPositions();
    }

    @Override
    public void onDisable(){
        positions.clear();
        isSucces = false;
        blocksInTick = 0;
    }

    @Override
    public void onUpdate() {
        if (!fullNullCheck()) {
            for (BlockPos pos : positions){
                if (!positions.isEmpty()  && !isSucces) {

                    if (blocksInTick < bps.getValue()){
                        blocksInTick++;
                        return;
                    }

                    int oldslot = mc.player.getInventory().selectedSlot;
                    int slot = InventoryUtil.findHotbarItem(BlockItem.class);
                    if (slot == -1) break;
                    InventoryUtil.switchSlot(slot);
                    InteractionUtility.placeBlock(pos, InteractionUtility.Interact.Vanilla, InteractionUtility.PlaceMode.Packet,true);
                    BlockRenderUtil.renderBlock(pos,OyVey.colorManager.getColor(), 2, OyVey.colorManager.getColor(), BlockRenderUtil.BlockAnimationMode.Fade, BlockRenderUtil.BlockRenderMode.All);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    InventoryUtil.switchSlot(oldslot);
                    blocksInTick = 0;

                }
            }

            isSucces = true;

            if (isSucces) disable();
        }
    }

    @Override
    public void onRender3D(Render3DEvent event){
        if (render.getValue()) BlockRenderUtil.onRender(event.getMatrix());
    }

    private void setPositions(){
        PlayerEntity player = mc.player;
        switch (mode.getValue()){
            case Pisun -> {
                switch (GetFacing()){
                    case East -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).east().east();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.north());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                    }
                    case West -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).west().west();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.north());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                    }
                    case North -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).north().north();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.east());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                    }
                    case South -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).south().south();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.east());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                    }
                }
            }
            case Portal -> {
                switch (GetFacing()){
                    case East -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).east().east();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.south().south());
                        positions.add(pos.north());
                        positions.add(pos.south().south().up());
                        positions.add(pos.south().south().up().up());
                        positions.add(pos.south().south().up().up().up());
                        positions.add(pos.south().south().up().up().up().up());
                        positions.add(pos.south().south().up().up().up().up().north());
                        positions.add(pos.north().up());
                        positions.add(pos.north().up().up());
                        positions.add(pos.north().up().up().up());
                        positions.add(pos.north().up().up().up().up());
                        positions.add(pos.north().up().up().up().up().south());
                    }
                    case West -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).west().west();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.south().south());
                        positions.add(pos.north());
                        positions.add(pos.south().south().up());
                        positions.add(pos.south().south().up().up());
                        positions.add(pos.south().south().up().up().up());
                        positions.add(pos.south().south().up().up().up().up());
                        positions.add(pos.south().south().up().up().up().up().north());
                        positions.add(pos.north().up());
                        positions.add(pos.north().up().up());
                        positions.add(pos.north().up().up().up());
                        positions.add(pos.north().up().up().up().up());
                        positions.add(pos.north().up().up().up().up().south());
                    }
                    case North -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).north().north();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.west().west());
                        positions.add(pos.east());
                        positions.add(pos.west().west().up());
                        positions.add(pos.west().west().up().up());
                        positions.add(pos.west().west().up().up().up());
                        positions.add(pos.west().west().up().up().up().up());
                        positions.add(pos.west().west().up().up().up().up().east());
                        positions.add(pos.east().up());
                        positions.add(pos.east().up().up());
                        positions.add(pos.east().up().up().up());
                        positions.add(pos.east().up().up().up().up());
                        positions.add(pos.east().up().up().up().up().west());
                    }
                    case South -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).south().south();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.west().west());
                        positions.add(pos.east());
                        positions.add(pos.west().west().up());
                        positions.add(pos.west().west().up().up());
                        positions.add(pos.west().west().up().up().up());
                        positions.add(pos.west().west().up().up().up().up());
                        positions.add(pos.west().west().up().up().up().up().east());
                        positions.add(pos.east().up());
                        positions.add(pos.east().up().up());
                        positions.add(pos.east().up().up().up());
                        positions.add(pos.east().up().up().up().up());
                        positions.add(pos.east().up().up().up().up().west());
                    }
                }
            }
            case Svaston -> {
                switch (GetFacing()){
                    case East -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).east().east();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.south().south());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                        positions.add(pos.up().up().north());
                        positions.add(pos.up().up().north().north());
                        positions.add(pos.up().up().north().north().down());
                        positions.add(pos.up().up().north().north().down().down());
                        positions.add(pos.up().up().up());
                        positions.add(pos.up().up().south());
                        positions.add(pos.up().up().south().south());
                        positions.add(pos.up().up().south().south().up());
                        positions.add(pos.up().up().south().south().up().up());
                        positions.add(pos.up().up().up().up());
                        positions.add(pos.up().up().up().up().north());
                        positions.add(pos.up().up().up().up().north().north());
                    }
                    case West -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).west().west();
                        positions.add(pos);
                        positions.add(pos.south());
                        positions.add(pos.south().south());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                        positions.add(pos.up().up().north());
                        positions.add(pos.up().up().north().north());
                        positions.add(pos.up().up().north().north().down());
                        positions.add(pos.up().up().north().north().down().down());
                        positions.add(pos.up().up().up());
                        positions.add(pos.up().up().south());
                        positions.add(pos.up().up().south().south());
                        positions.add(pos.up().up().south().south().up());
                        positions.add(pos.up().up().south().south().up().up());
                        positions.add(pos.up().up().up().up());
                        positions.add(pos.up().up().up().up().north());
                        positions.add(pos.up().up().up().up().north().north());
                    }
                    case North -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).north().north();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.west().west());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                        positions.add(pos.up().up().east());
                        positions.add(pos.up().up().east().east());
                        positions.add(pos.up().up().east().east().down());
                        positions.add(pos.up().up().east().east().down().down());
                        positions.add(pos.up().up().up());
                        positions.add(pos.up().up().west());
                        positions.add(pos.up().up().west().west());
                        positions.add(pos.up().up().west().west().up());
                        positions.add(pos.up().up().west().west().up().up());
                        positions.add(pos.up().up().up().up());
                        positions.add(pos.up().up().up().up().east());
                        positions.add(pos.up().up().up().up().east().east());
                    }
                    case South -> {
                        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()).south().south();
                        positions.add(pos);
                        positions.add(pos.west());
                        positions.add(pos.west().west());
                        positions.add(pos.up());
                        positions.add(pos.up().up());
                        positions.add(pos.up().up().east());
                        positions.add(pos.up().up().east().east());
                        positions.add(pos.up().up().east().east().down());
                        positions.add(pos.up().up().east().east().down().down());
                        positions.add(pos.up().up().up());
                        positions.add(pos.up().up().west());
                        positions.add(pos.up().up().west().west());
                        positions.add(pos.up().up().west().west().up());
                        positions.add(pos.up().up().west().west().up().up());
                        positions.add(pos.up().up().up().up());
                        positions.add(pos.up().up().up().up().east());
                        positions.add(pos.up().up().up().up().east().east());
                    }
                }
            }
        }
    }

    @Override
    public String getDisplayInfo(){
        return mode.getValue().toString();
    }

    public static FacingDirection GetFacing() {
        switch (MathHelper.floor((double) (mc.player.getYaw() * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
            case 1:
                return FacingDirection.South;
            case 2:
            case 3:
                return FacingDirection.West;
            case 4:
            case 5:
                return FacingDirection.North;
            case 6:
            case 7:
                return FacingDirection.East;
        }
        return FacingDirection.North;
    }
    public enum FacingDirection {
        North,
        South,
        East,
        West,
    }
}