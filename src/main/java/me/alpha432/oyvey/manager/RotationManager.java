package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;

public class RotationManager extends Feature {
    private float yaw, pitch;
    private boolean rotating;

    public void init() {
        Util.EVENT_BUS.register(this);
    }

    public void rotateVec3d(Vec3d vec3d) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), vec3d);
        setRotations(angle[0], angle[1]);

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    public void rotateEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), entity.getPos());
        setRotations(angle[0], angle[1]);

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    public void rotateBlockPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), Vec3d.ofCenter(pos));
        setRotations(angle[0], angle[1]);

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    public void rotate(float yaw, float pitch) {
        setRotations(yaw, pitch);

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    public void updateRotations() {
        this.yaw = mc.player.getYaw();
        this.pitch = mc.player.getPitch();
    }

    public void restoreRotations() {
        mc.player.setYaw(yaw);
        mc.player.headYaw = yaw;
        mc.player.setPitch(pitch);
    }

    // setters
    public void setRotations(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setRotating(boolean rotate) {
        this.rotating = rotate;
    }

    //getters
    public boolean isRotating() {
        return this.rotating;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public static int getYaw4D() {
        return MathHelper.floor((double) (mc.player.getYaw() * 4.0f / 360.0f) + 0.5) & 3;
    }
}