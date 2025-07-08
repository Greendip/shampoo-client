package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.InteractionUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;


public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", "", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        if (fullNullCheck()) return;
        BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
        if (mc.options.attackKey.isPressed()) {
            InteractionUtil.breakBlock(pos);
        }
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
