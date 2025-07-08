package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.InteractionUtil;
import me.alpha432.oyvey.util.InteractionUtility;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

public class AutoCrystal extends Module {
    private final Setting<Float> range = num("Range", 4f, 1f, 6f);
    private final Setting<Float> middg = num("MinDMG", 4.0F, 1.0F, 36.0F);
    private final Setting<Float> middgd = num("MaxSelfDMG", 4.0F, 1.0F, 36.0F);
    public Setting<Boolean> rotate = bool("Rotate", true);
    public Setting<Boolean> safety = bool("Safety", true);
    public Setting<Boolean> raytrace = bool("Raytrace", true);
    public AutoCrystal() {
        super("AutoCrystal", "Attacks nearby entities", Category.COMBAT, true, false, false);
    }
    @Override
    public void onTick() {
        if (this.mc.world != null && this.mc.player != null) {
            StreamSupport.stream(this.mc.world.getEntities().spliterator(), false)
                    .filter((entity) -> entity instanceof EndCrystalEntity)
                    .map((entity) -> (EndCrystalEntity)entity)
                    .filter((crystal) -> this.mc.player.distanceTo(crystal) <= this.range.getValue())
                    .forEach((crystal) -> {
                        if (this.rotate.getValue()) {
                            OyVey.rotationManager.rotateVec3d(crystal.getPos());
                        }
                        this.mc.interactionManager.attackEntity(this.mc.player, crystal);
                        this.mc.player.swingHand(Hand.MAIN_HAND);
                    });

            PlayerEntity target = this.findTarget();
            if (target == null) {
                if (this.rotate.getValue()) {
                    OyVey.rotationManager.rotate(this.mc.player.getYaw(), this.mc.player.getPitch());
                }
            } else {
                BlockPos bestPosition = this.findBestPosition(target);
                if (bestPosition != null) {
                    if (this.rotate.getValue()) {
                        Vec3d targetPos = Vec3d.of(bestPosition);
                        OyVey.rotationManager.rotateVec3d(targetPos);
                    }

                    if (this.placeCrystal(bestPosition)) {
                        this.attackCrystal(bestPosition.up());
                    }
                } else if (this.rotate.getValue()) {
                    OyVey.rotationManager.rotate(this.mc.player.getYaw(), this.mc.player.getPitch());
                }
            }
        }
    }

    private PlayerEntity findTarget() {
        return this.mc.world.getPlayers().stream()
                .filter((player) -> player != this.mc.player && !player.isDead() && !player.isCreative())
                .filter((player) -> this.mc.player.distanceTo(player) <= this.range.getValue())
                .min(Comparator.comparingDouble((player) -> (double)this.mc.player.distanceTo(player)))
                .orElse(null);
    }

    private BlockPos findBestPosition(PlayerEntity target) {
        List<BlockPos> positions = this.findPossiblePositions(target);
        return positions.stream()
                .map((pos) -> new PositionData(pos, this.calculateDamage(pos, target), this.calculateSelfDamage(pos)))
                .filter((data) -> data.damage >= this.middg.getValue() &&
                        data.selfDamage <= this.middgd.getValue())
                .filter((data) -> data.selfDamage < data.damage)
                .sorted((data1, data2) -> {
                    if (this.safety.getValue()) {
                        int safetyComparison = Float.compare(data1.selfDamage, data2.selfDamage);
                        if (safetyComparison != 0) {
                            return safetyComparison;
                        }
                    }
                    return Float.compare(data2.damage, data1.damage);
                })
                .map((data) -> data.pos)
                .findFirst()
                .orElse(null);
    }

    private List<BlockPos> findPossiblePositions(PlayerEntity target) {
        BlockPos targetPos = target.getBlockPos();
        int range = (int)Math.ceil((double)this.range.getValue());
        List<BlockPos> positions = new ArrayList<>();

        for(int x = -range; x <= range; ++x) {
            for(int y = -range; y <= range; ++y) {
                for(int z = -range; z <= range; ++z) {
                    BlockPos pos = targetPos.add(x, y, z);
                    if (this.isValidPosition(pos)) {
                        positions.add(pos);
                    }
                }
            }
        }
        return positions;
    }

    private boolean isValidPosition(BlockPos pos) {
        Block block = this.mc.world.getBlockState(pos).getBlock();
        return (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) &&
                this.mc.world.isAir(pos.up());
    }

    private float calculateDamage(BlockPos pos, PlayerEntity target) {
        Vec3d explosionPos = Vec3d.of(pos.up());
        Vec3d targetPos = target.getPos();
        double distance = explosionPos.distanceTo(targetPos);
        if (distance > (double)this.range.getValue()) {
            return 0.0F;
        } else {
            double exposure = 1.0F - distance / (double)this.range.getValue();
            return (float)(Math.pow(exposure, 2.0F) * 12.0F);
        }
    }

    private float calculateSelfDamage(BlockPos pos) {
        Vec3d explosionPos = Vec3d.of(pos.up());
        Vec3d selfPos = this.mc.player.getPos();
        double distance = explosionPos.distanceTo(selfPos);
        if (distance > (double)this.range.getValue()) {
            return 0.0F;
        } else {
            double exposure = 1.0F - distance / (double)this.range.getValue();
            float damage = (float)(Math.pow(exposure, 2.0F) * 12.0F);
            return this.mc.player.getHealth() - damage <= 0.5F ? Float.MAX_VALUE : damage;
        }
    }

    private boolean placeCrystal(BlockPos pos) {
        if (this.mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL) {
            for(int i = 0; i < 9; ++i) {
                ItemStack stack = this.mc.player.getInventory().getStack(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    this.mc.player.getInventory().selectedSlot = i;
                    this.mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(i));
                    break;
                }
            }
        }

        if (this.mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            BlockHitResult hitResult = new BlockHitResult(Vec3d.of(pos), Direction.UP, pos, false);
            if (raytrace.getValue()) {
                InteractionUtility.rayCastBlock(new RaycastContext(mc.player.getEyePos(), pos.toCenterPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player), pos);
            }
            this.mc.interactionManager.interactBlock(this.mc.player, Hand.MAIN_HAND, hitResult);
            return true;
        } else {
            return false;
        }
    }

    private void attackCrystal(BlockPos pos) {
        for(Entity entity : this.mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity && entity.getBlockPos().equals(pos)) {
                if (this.rotate.getValue()) {
                    OyVey.rotationManager.rotateVec3d(entity.getPos());
                }
                if (raytrace.getValue()) InteractionUtility.rayCastBlock(new RaycastContext(mc.player.getEyePos(), pos.toCenterPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player), pos);
                this.mc.interactionManager.attackEntity(this.mc.player, entity);
                this.mc.player.swingHand(Hand.MAIN_HAND);
                return;
            }
        }
    }

    private static class PositionData {
        public final BlockPos pos;
        public final float damage;
        public final float selfDamage;

        public PositionData(BlockPos pos, float damage, float selfDamage) {
            this.pos = pos;
            this.damage = damage;
            this.selfDamage = selfDamage;
        }
    }
}