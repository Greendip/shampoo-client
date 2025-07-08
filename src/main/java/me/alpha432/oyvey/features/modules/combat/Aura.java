package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

import me.alpha432.oyvey.util.CaptureMark;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

public class Aura extends Module {
    private final Timer timer = new Timer();
    public Setting<Boolean> players = bool("Players", true);
    public Setting<Boolean> mobs = bool("Mobs", true);
    private final Setting<Float> range = num("Range", 4f, 1f, 6f);
    private PlayerEntity target;

    public Aura() {
        super("Aura", "", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (!fullNullCheck()) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player || !(mc.player.getMainHandStack().getItem() instanceof SwordItem) || entity.getPos().distanceTo(mc.player.getPos()) > range.getValue())
                    continue;
                if (entity instanceof PlayerEntity player && players.getValue()) {
                    target = player;
                    attackEntity(entity);
                }
                if (entity instanceof MobEntity && mobs.getValue()) {
                    MobEntity mob = (MobEntity) entity;
                    attackEntity(mob);
                }
                if (entity instanceof WitherEntity && mobs.getValue()) {
                    MobEntity mob = (MobEntity) entity;
                    attackEntity(mob);
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck() || target == null || target.getPos().distanceTo(mc.player.getPos()) > range.getValue() || !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) return;

        CaptureMark.render(target);
    }

    private void attackEntity(Entity entity) {
        if (timer.passedS(0.8)) {
            if (OyVey.friendManager.isFriend(entity.getName().getString())) return;
            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
            float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), entity.getPos());
            mc.player.headYaw = angle[0];
            timer.reset();
        }
    }
    @Override public String getDisplayInfo() {
        return target != null ? target.getName().getString(): null;
    }
}