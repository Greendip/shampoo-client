package me.alpha432.oyvey.mixin;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public abstract class EntityVelocityMixin {
    @Accessor("velocityX") @Mutable
    abstract void setVelocityX(int velocityX);
    @Accessor("velocityY") @Mutable
    abstract void setVelocityY(int velocityY);
    @Accessor("velocityZ") @Mutable
    abstract void setVelocityZ(int velocityZ);
}
