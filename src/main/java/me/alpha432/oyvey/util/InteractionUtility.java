package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InteractionUtility implements Util {
    public static Map<BlockPos, Long> awaiting = new HashMap<>();

    private static final List<Block> SHIFT_BLOCKS = Arrays.asList(
            Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
            Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
            Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );
    public static boolean placeBlock(BlockPos bp, Interact interact, PlaceMode mode, boolean ignoreEntities) {
        BlockHitResult result = getPlaceResult(bp, interact, ignoreEntities);
        if (result == null || mc.world == null || mc.interactionManager == null || mc.player == null) return false;

        boolean sprint = mc.player.isSprinting();
        boolean sneak = needSneak(mc.world.getBlockState(result.getBlockPos()).getBlock()) && !mc.player.isSneaking();

        if (sprint)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        if (sneak)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

        if (mode == PlaceMode.Normal)
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result);

        if (mode == PlaceMode.Packet)
            mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, 0));

        awaiting.put(bp, System.currentTimeMillis());


        if (sneak)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

        if (sprint)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

        mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        return true;
    }

    @Nullable
    public static BlockHitResult getPlaceResult(@NotNull BlockPos bp, Interact interact, boolean ignoreEntities) {
        if (!ignoreEntities)
            for (Entity entity : new ArrayList<>(mc.world.getNonSpectatingEntities(Entity.class, new Box(bp))))
                if (!(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrbEntity))
                    return null;

        if (!mc.world.getBlockState(bp).isReplaceable())
            return null;

        if (interact == Interact.AirPlace)
            return rayCastBlock(new RaycastContext(mc.player.getEyePos(), bp.toCenterPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player), bp);

        ArrayList<BlockPosWithFacing> supports = getSupportBlocks(bp);
        for (BlockPosWithFacing support : supports) {
            if (interact != Interact.Vanilla) {
                @NotNull List<Direction> dirs = getStrictDirections(bp);
                if (dirs.isEmpty())
                    return null;

                if (!dirs.contains(support.facing))
                    continue;
            }
            BlockHitResult result = null;
            if (interact == Interact.Legit) {
                Vec3d p = getVisibleDirectionPoint(support.facing, support.position, 0, 6); //TODO Implement Range
                if (p != null)
                    return new BlockHitResult(p, support.facing, support.position, false);
            } else {
                Vec3d directionVec = new Vec3d(support.position.getX() + 0.5 + support.facing.getVector().getX() * 0.5, support.position.getY() + 0.5 + support.facing.getVector().getY() * 0.5, support.position.getZ() + 0.5 + support.facing.getVector().getZ() * 0.5);
                result = new BlockHitResult(directionVec, support.facing, support.position, false);
            }
            return result;
        }
        return null;
    }


    public static @NotNull List<Direction> getStrictDirections(@NotNull BlockPos bp) {
        List<Direction> visibleSides = new ArrayList<>();
        Vec3d positionVector = bp.toCenterPos();

        double westDelta = mc.player.getEyePos().x - (positionVector.add(0.5, 0, 0).x);
        double eastDelta =mc.player.getEyePos().x - (positionVector.add(-0.5, 0, 0).x);
        double northDelta = mc.player.getEyePos().z - (positionVector.add(0, 0, 0.5).z);
        double southDelta = mc.player.getEyePos().z - (positionVector.add(0, 0, -0.5).z);
        double upDelta = mc.player.getEyePos().y - (positionVector.add(0, 0.5, 0).y);
        double downDelta = mc.player.getEyePos().y - (positionVector.add(0, -0.5, 0).y);

        if (westDelta > 0 && isSolid(bp.west()))
            visibleSides.add(Direction.EAST);
        if (westDelta < 0 && isSolid(bp.east()))
            visibleSides.add(Direction.WEST);
        if (eastDelta < 0 && isSolid(bp.east()))
            visibleSides.add(Direction.WEST);
        if (eastDelta > 0 && isSolid(bp.west()))
            visibleSides.add(Direction.EAST);

        if (northDelta > 0 && isSolid(bp.north()))
            visibleSides.add(Direction.SOUTH);
        if (northDelta < 0 && isSolid(bp.south()))
            visibleSides.add(Direction.NORTH);
        if (southDelta < 0 && isSolid(bp.south()))
            visibleSides.add(Direction.NORTH);
        if (southDelta > 0 && isSolid(bp.north()))
            visibleSides.add(Direction.SOUTH);

        if (upDelta > 0 && isSolid(bp.down()))
            visibleSides.add(Direction.UP);
        if (upDelta < 0 && isSolid(bp.up()))
            visibleSides.add(Direction.DOWN);
        if (downDelta < 0 && isSolid(bp.up()))
            visibleSides.add(Direction.DOWN);
        if (downDelta > 0 && isSolid(bp.down()))
            visibleSides.add(Direction.UP);

        return visibleSides;
    }

    public static boolean isSolid(BlockPos bp) {
        return mc.world.getBlockState(bp).isSolid() || awaiting.containsKey(bp);
    }

    public static @NotNull ArrayList<BlockPosWithFacing> getSupportBlocks(@NotNull BlockPos bp) {
        ArrayList<BlockPosWithFacing> list = new ArrayList<>();

        if (mc.world.getBlockState(bp.add(0, -1, 0)).isSolid() || awaiting.containsKey(bp.add(0, -1, 0)))
            list.add(new BlockPosWithFacing(bp.add(0, -1, 0), Direction.UP));

        if (mc.world.getBlockState(bp.add(0, 1, 0)).isSolid() || awaiting.containsKey(bp.add(0, 1, 0)))
            list.add(new BlockPosWithFacing(bp.add(0, 1, 0), Direction.DOWN));

        if (mc.world.getBlockState(bp.add(-1, 0, 0)).isSolid() || awaiting.containsKey(bp.add(-1, 0, 0)))
            list.add(new BlockPosWithFacing(bp.add(-1, 0, 0), Direction.EAST));

        if (mc.world.getBlockState(bp.add(1, 0, 0)).isSolid() || awaiting.containsKey(bp.add(1, 0, 0)))
            list.add(new BlockPosWithFacing(bp.add(1, 0, 0), Direction.WEST));

        if (mc.world.getBlockState(bp.add(0, 0, 1)).isSolid() || awaiting.containsKey(bp.add(0, 0, 1)))
            list.add(new BlockPosWithFacing(bp.add(0, 0, 1), Direction.NORTH));

        if (mc.world.getBlockState(bp.add(0, 0, -1)).isSolid() || awaiting.containsKey(bp.add(0, 0, -1)))
            list.add(new BlockPosWithFacing(bp.add(0, 0, -1), Direction.SOUTH));

        return list;
    }


    public static BlockHitResult rayCastBlock(RaycastContext context, BlockPos block) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (raycastContext, blockPos) -> {
            BlockState blockState;

            if (!blockPos.equals(block)) blockState = Blocks.AIR.getDefaultState();
            else blockState = Blocks.OBSIDIAN.getDefaultState();

            Vec3d vec3d = raycastContext.getStart();
            Vec3d vec3d2 = raycastContext.getEnd();
            VoxelShape voxelShape = raycastContext.getBlockShape(blockState, mc.world, blockPos);
            BlockHitResult blockHitResult = mc.world.raycastBlock(vec3d, vec3d2, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = VoxelShapes.empty();
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, blockPos);

            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult2.getPos());

            return d <= e ? blockHitResult : blockHitResult2;
        }, (raycastContext) -> {
            Vec3d vec3d = raycastContext.getStart().subtract(raycastContext.getEnd());
            return BlockHitResult.createMissed(raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(raycastContext.getEnd()));
        });
    }

    public static @Nullable Vec3d getVisibleDirectionPoint(@NotNull Direction dir, @NotNull BlockPos bp, float wallRange, float range) {
        Box brutBox = getDirectionBox(dir);

        // EAST, WEST
        if (brutBox.maxX - brutBox.minX == 0)
            for (double y = brutBox.minY; y < brutBox.maxY; y += 0.1f)
                for (double z = brutBox.minZ; z < brutBox.maxZ; z += 0.1f) {
                    Vec3d point = new Vec3d(bp.getX() + brutBox.minX, bp.getY() + y, bp.getZ() + z);

                    if (shouldSkipPoint(point, bp, dir, wallRange, range))
                        continue;

                    return point;
                }


        // DOWN, UP
        if (brutBox.maxY - brutBox.minY == 0)
            for (double x = brutBox.minX; x < brutBox.maxX; x += 0.1f)
                for (double z = brutBox.minZ; z < brutBox.maxZ; z += 0.1f) {
                    Vec3d point = new Vec3d(bp.getX() + x, bp.getY() + brutBox.minY, bp.getZ() + z);

                    if (shouldSkipPoint(point, bp, dir, wallRange, range))
                        continue;

                    return point;
                }


        // NORTH, SOUTH
        if (brutBox.maxZ - brutBox.minZ == 0)
            for (double x = brutBox.minX; x < brutBox.maxX; x += 0.1f)
                for (double y = brutBox.minY; y < brutBox.maxY; y += 0.1f) {
                    Vec3d point = new Vec3d(bp.getX() + x, bp.getY() + y, bp.getZ() + brutBox.minZ);

                    if (shouldSkipPoint(point, bp, dir, wallRange, range))
                        continue;

                    return point;
                }


        return null;
    }

    private static boolean shouldSkipPoint(Vec3d point, BlockPos bp, Direction dir, float wallRange, float range) {
        RaycastContext context = new RaycastContext(mc.player.getEyePos(), point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player);
        BlockHitResult result = mc.world.raycast(context);

        float dst = InteractionUtility.squaredDistanceFromEyes(point);

        if (result != null
                && result.getType() == HitResult.Type.BLOCK
                && !result.getBlockPos().equals(bp)
                && dst > wallRange * wallRange)
            return true;

        return dst > range * range;
    }

    public static float squaredDistanceFromEyes(@NotNull Vec3d vec) {
        double d0 = vec.x - mc.player.getX();
        double d1 = vec.z - mc.player.getZ();
        double d2 = vec.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        return (float) (d0 * d0 + d1 * d1 + d2 * d2);
    }

    private static @NotNull Box getDirectionBox(Direction dir) {
        return switch (dir) {
            case UP -> new Box(.15f, 1f, .15f, .85f, 1f, .85f);
            case DOWN -> new Box(.15f, 0f, .15f, .85f, 0f, .85f);

            case EAST -> new Box(1f, .15f, .15f, 1f, .85f, .85f);
            case WEST -> new Box(0f, .15f, .15f, 0f, .85f, .85f);

            case NORTH -> new Box(.15f, .15f, 0f, .85f, .85f, 0f);
            case SOUTH -> new Box(.15f, .15f, 1f, .85f, .85f, 1f);
        };
    }

    public static boolean needSneak(Block in) {
        return SHIFT_BLOCKS.contains(in);
    }

    public record BlockPosWithFacing(BlockPos position, Direction facing) {
    }

    public enum PlaceMode {
        Packet,
        Normal
    }

    public enum Interact {
        Vanilla,
        Strict,
        Legit,
        AirPlace
    }
}
