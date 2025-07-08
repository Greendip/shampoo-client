package me.alpha432.oyvey.event.impl;

import me.alpha432.oyvey.event.Event;

public class RotateEvent extends Event {
    private float yaw;
    private float pitch;
    private boolean modified;
    public RotateEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        modified = true;
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        modified = true;
        this.pitch = pitch;
    }

    public boolean isModified() {
        return modified;
    }
    public void setRotation(final float yaw, final float pitch) {
        this.setYaw(yaw);
        this.setPitch(pitch);
    }

    public void setYawNoModify(float yaw) {
        this.yaw = yaw;
    }

    public void setPitchNoModify(float pitch) {
        this.pitch = pitch;
    }

}
