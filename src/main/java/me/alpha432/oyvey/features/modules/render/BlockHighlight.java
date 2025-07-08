package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;

public class BlockHighlight extends Module {
    public Setting<Float> lines = this.register(new Setting<>("Line:", 1.5f, 0.1f, 5f));
    public Setting<Color> color = color("Color",new Color(1));


    public BlockHighlight() {
        super("BlockHighlight", "Draws box at the block that you are looking at", Category.RENDER, true, false, false);
    }

    @Subscribe public void onRender3D(Render3DEvent event) {
        if (mc.crosshairTarget instanceof BlockHitResult result) {
            VoxelShape shape = mc.world.getBlockState(result.getBlockPos()).getOutlineShape(mc.world, result.getBlockPos());
            if (shape.isEmpty()) return;
            Box box = shape.getBoundingBox();
            box = box.offset(result.getBlockPos());
            RenderUtil.drawBox(event.getMatrix(), box, color.getValue(), lines.getValue());
            RenderUtil.drawBoxFilled(event.getMatrix(), box, color.getValue());
        }
    }
}
