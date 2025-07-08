package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.HoleUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", "", Category.RENDER, true, false, false);
    }

    private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.Fade));
    private final Setting<Integer> rangeXZ = register(new Setting<>("Range XY", 10, 1, 128));
    private final Setting<Integer> rangeY = register(new Setting<>("Range Y", 5, 1, 128));
    private final Setting<Float> height = register(new Setting<>("Height", 1f, 0.01f, 5f));
    private final Setting<Float> lineWith = register(new Setting<>("Line Width", 0.5f, 0.01f, 5f));
    //public final Setting<Boolean> culling = new Setting<>("Culling", true, v -> mode.getValue() == Mode.Fade || mode.getValue() == Mode.Fade2);

    private final Timer logicTimer = new Timer();
    private final java.util.List<BoxWithColor> positions = new CopyOnWriteArrayList<>();

    @Override
    public void onDisable() {
        positions.clear();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (positions.isEmpty()) return;

        for (BoxWithColor pwc : positions) {
            switch (mode.getValue()) {
                case Fade -> renderFade(pwc, event.getMatrix());
                case Fade2 -> renderFade2(pwc, event.getMatrix());
                case CubeFill -> renderFill(pwc, event.getMatrix());
                case CubeBoth -> {
                    renderOutline(pwc, event.getMatrix());
                    renderFill(pwc, event.getMatrix());
                }
            }
        }
    }

    public void renderFade(@NotNull HoleESP.BoxWithColor posWithColor, MatrixStack stack) {
        RenderUtil.drawBoxFilled(stack, posWithColor.box, getColor(posWithColor.box, posWithColor.color(), 60));
        RenderUtil.drawBox(stack, posWithColor.box, getColor(posWithColor.box, posWithColor.color(), posWithColor.color.getAlpha()), lineWith.getValue());
    }

    public void renderFade2(@NotNull HoleESP.BoxWithColor boxWithColor, MatrixStack stack) {
        RenderUtil.drawBoxFilled(stack, boxWithColor.box, getColor(boxWithColor.box, boxWithColor.color(), 60));
        RenderUtil.drawBox(stack, boxWithColor.box, getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()), lineWith.getValue());
        RenderUtil.drawBoxFilled(stack, new Box(boxWithColor.box.minX, boxWithColor.box.minY, boxWithColor.box.minZ, boxWithColor.box.maxX, boxWithColor.box.minY + 0.01f, boxWithColor.box.maxZ), getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()));
    }

    private Color getColor(Box box, Color color, int alpha) {
        float dist = squaredDistance2d(box.getCenter().getX(), box.getCenter().getZ());
        float factor = dist / (rangeXZ.getPow2Value());

        factor = 1f - easeOutExpo(factor);

        factor = MathUtil.clamp(factor, 0f, 1f);

        return injectAlpha(color, (int) (factor * alpha));
    }

    public static Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathHelper.clamp(alpha, 0, 255));
    }

    private float easeOutExpo(float x) {
        return x == 1f ? 1f : (float) (1f - Math.pow(2f, -10f * x));
    }

    public void renderOutline(@NotNull HoleESP.BoxWithColor boxWithColor, MatrixStack stack) {
        RenderUtil.drawBox(stack, boxWithColor.box, getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()), lineWith.getValue());
    }

    public void renderFill(@NotNull HoleESP.BoxWithColor boxWithColor, MatrixStack stack) {
        RenderUtil.drawBoxFilled(stack, boxWithColor.box(), getColor(boxWithColor.box, boxWithColor.color(), boxWithColor.color.getAlpha()));
    }

    @Override
    public void onTick() {
        if (fullNullCheck() || !logicTimer.passedMs(500))
            return;
        findHoles();
        logicTimer.reset();
    }

    private void findHoles() {
        ArrayList<BoxWithColor> blocks = new ArrayList<>();
        if (mc.world == null || mc.player == null) {
            positions.clear();
            return;
        }
        BlockPos centerPos = BlockPos.ofFloored(mc.player.getPos());
        List<Box> boxes = new ArrayList<>();

        for (int i = centerPos.getX() - rangeXZ.getValue(); i < centerPos.getX() + rangeXZ.getValue(); i++) {
            for (int j = centerPos.getY() - rangeY.getValue(); j < centerPos.getY() + rangeY.getValue(); j++) {
                for (int k = centerPos.getZ() - rangeXZ.getValue(); k < centerPos.getZ() + rangeXZ.getValue(); k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + height.getValue(), pos.getZ() + 1);
                    Color color = new Color(0x8EFF0000, true);
                    if (HoleUtil.validIndestructible(pos)) {

                    } else if (HoleUtil.validBedrock(pos)) {
                        color = new Color(0x9000FF00, true);
                    } else if (HoleUtil.validTwoBlockBedrock(pos)) {
                        boolean east = mc.world.isAir(pos.offset(Direction.EAST));
                        boolean south = mc.world.isAir(pos.offset(Direction.SOUTH));
                        box = new Box(box.minX, box.minY, box.minZ, box.maxX + (east ? 1 : 0), box.maxY, box.maxZ + (south ? 1 : 0));
                        color = new Color(0x9000FF00, true);
                    } else if (HoleUtil.validTwoBlockIndestructible(pos)) {
                        boolean east = mc.world.isAir(pos.offset(Direction.EAST));
                        boolean south = mc.world.isAir(pos.offset(Direction.SOUTH));
                        box = new Box(box.minX, box.minY, box.minZ, box.maxX + (east ? 1 : 0), box.maxY, box.maxZ + (south ? 1 : 0));
                    } else if (HoleUtil.validQuadBedrock(pos)) {
                        box = new Box(box.minX, box.minY, box.minZ, box.maxX + 1, box.maxY, box.maxZ + 1);
                        color = new Color(0x9000FF00, true);
                    } else if (HoleUtil.validQuadIndestructible(pos)) {
                        box = new Box(box.minX, box.minY, box.minZ, box.maxX + 1, box.maxY, box.maxZ + 1);
                    } else {
                        continue;
                    }

                    boolean skip = false;
                    for (Box boxOffset : boxes) {
                        if (boxOffset.intersects(box))
                            skip = true;
                    }

                    if (skip)
                        continue;

                    blocks.add(new BoxWithColor(box, color));
                    boxes.add(box);
                }
            }
        }
        positions.clear();
        positions.addAll(blocks);
    }

    public static float squaredDistance2d(double x, double z) {
        if (mc.player == null) return 0f;

        double d = mc.player.getX() - x;
        double f = mc.player.getZ() - z;
        return (float) (d * d + f * f);
    }

    public record BoxWithColor(Box box, Color color) {
    }

    private enum Mode {
        Fade,
        Fade2,
        CubeFill,
        CubeBoth
    }
}