package me.alpha432.oyvey.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.TickEvent;
import me.alpha432.oyvey.features.modules.exploit.MultiTask;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    //
    @Shadow
    public ClientWorld world;
    //
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    protected int attackCooldown;
    @Shadow
    protected abstract void doItemUse();

    /**
     * @return
     */
    @Shadow
    protected abstract boolean doAttack();
    @Unique
    private boolean leftClick;
    // https://github.com/MeteorDevelopment/meteor-client/blob/master/src/main/java/meteordevelopment/meteorclient/mixin/MinecraftClientMixin.java#L54
    @Unique
    private boolean rightClick;
    @Unique
    private boolean doAttackCalled;
    @Unique
    private boolean doItemUseCalled;




    @ModifyExpressionValue(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
    private boolean doItemUseModifyIsBreakingBlock(boolean original) {
        MultiTask multiTask = OyVey.moduleManager.getModuleByClass(MultiTask.class);
        if(multiTask.isEnabled()) return false;
        return original;
    }

    @ModifyExpressionValue(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean handleBlockBreakingModifyIsUsingItem(boolean original) {
        MultiTask multiTask = OyVey.moduleManager.getModuleByClass(MultiTask.class);
        if(multiTask.isEnabled()) return false;
        return original;
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void hookTickPre(CallbackInfo ci)
    {
        doAttackCalled = false;
        doItemUseCalled = false;
        if (player != null && world != null)
        {
            TickEvent tickPreEvent = new TickEvent();
            tickPreEvent.equals(Stage.PRE);
            Util.EVENT_BUS.post(tickPreEvent);
        }

        if (leftClick && !doAttackCalled)
        {
            doAttack();
        }
        if (rightClick && !doItemUseCalled)
        {
            doItemUse();
        }
        leftClick = false;
        rightClick = false;
    }
}
