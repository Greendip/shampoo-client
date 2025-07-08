package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.BlockUtils;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.block.entity.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ESP extends Module {
    public Setting<Boolean> players = this.register(new Setting<>("Players", false));
    public Setting<Boolean> items = this.register(new Setting<>("Items", false));
    public Setting<Boolean> chests = this.register(new Setting<>("Chests", false));
    public Setting<Boolean> echests = this.register(new Setting<>("Echests", false));
    public Setting<Boolean> shulkers = this.register(new Setting<>("Shulkers", false));
    public Setting<Boolean> pearls = this.register(new Setting<>("Pearls", false));
    public Setting<Float> range = this.register(new Setting<>("Range", 100f,10f,500f, v -> this.items.getValue()));
    public Setting<Double> lineThickness = this.register(new Setting<>("Line", 2.0,0.0,5.0));
    public ESP(){
        super("ESP","",Category.RENDER,true,false,false);
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        if (players.getValue()) {
            for (AbstractClientPlayerEntity entity : mc.world.getPlayers()) {
                if (entity != mc.player) {
                    RenderUtil.drawBox(event.getMatrix(), entity.getBoundingBox(), Color.green, lineThickness.getValue());
                }
            }
        }
        if (items.getValue()){
            Vec3d playerPos = mc.player.getPos();
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Vec3d itemPos = entity.getPos();
                    if (playerPos.distanceTo(itemPos) <= range.getValue()) {
                        RenderUtil.drawBox(event.getMatrix(), entity.getBoundingBox(), Color.white,
                                lineThickness.getValue().floatValue());
                    }
                }
            }
        }
        if (chests.getValue()){
            ArrayList<BlockEntity> blockEntities = BlockUtils.getTileEntities()
                    .collect(Collectors.toCollection(ArrayList::new));
            for (BlockEntity blockEntity : blockEntities) {
                if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity
                        || blockEntity instanceof BarrelBlockEntity) {
                    Box box = new Box(blockEntity.getPos());
                    RenderUtil.drawBox(event.getMatrix(), box, Color.ORANGE, lineThickness.getValue());
                }
            }
        }
        if (shulkers.getValue()){
            ArrayList<BlockEntity> blockEntities = BlockUtils.getTileEntities()
                    .collect(Collectors.toCollection(ArrayList::new));
            for (BlockEntity blockEntity : blockEntities) {
                if (blockEntity instanceof ShulkerBoxBlockEntity) {
                    Box box = new Box(blockEntity.getPos());
                    RenderUtil.drawBox(event.getMatrix(), box, Color.RED, lineThickness.getValue());
                }
            }
        }
        if (echests.getValue()){
            ArrayList<BlockEntity> blockEntities = BlockUtils.getTileEntities()
                    .collect(Collectors.toCollection(ArrayList::new));
            for (BlockEntity blockEntity : blockEntities) {
                if (blockEntity instanceof EnderChestBlockEntity) {
                    Box box = new Box(blockEntity.getPos());
                    RenderUtil.drawBox(event.getMatrix(), box, Color.MAGENTA, lineThickness.getValue());
                }
            }
        }
    }
}
