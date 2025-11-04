package io.github.faceinflux.ccsensory.content.peripherals;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
//? if >=1.21.7 {
import net.minecraft.world.entity.EntitySpawnReason;
//?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class LidarSensorPeripheral implements GenericPeripheral {
    /** The resolution of the scan in scans per degree */
    private static final float RESOLUTION = 1f;
    private static final double RANGE = 30;
    /** The offset when raycasting for blocks to prevent self collision */
    private static final double BLOCK_START_OFFSET = 0.91;

    // just in case
    private static boolean registered = false;
    public static void register() {
        if (!registered) {
            ComputerCraftAPI.registerGenericSource(new LidarSensorPeripheral());
            registered = true;
        }
    }

    @Override
    public String id() {
        return CCSensory.ID + ":lidar_sensor";
    }

    @LuaFunction(mainThread = true)
    public ObjectLuaTable scan(LidarSensorBlockEntity blockEntity) {
        assert blockEntity.getLevel() != null;
        long time = blockEntity.getLevel().getGameTime();
        if (blockEntity.getCooldownTick() > time) {
            return null;
        }
        blockEntity.setCooldownTick(time + 20 * 10);

        ArrayList<Tuple<BlockPos, BlockState>> blocks = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>();

        Vec3 blockCenter = Vec3.atCenterOf(blockEntity.getBlockPos());

        Entity tempEntity = EntityType.ARMOR_STAND.create(
                blockEntity.getLevel()
                //? if >=1.21.7 {
                , EntitySpawnReason.MOB_SUMMONED
                //?}
        );
        assert tempEntity != null;
        tempEntity.setPos(blockCenter);
        tempEntity.setInvisible(true);
        tempEntity.setInvulnerable(true);
        tempEntity.setNoGravity(true);
        blockEntity.getLevel().addFreshEntity(tempEntity);
        for (Vec3 direction : castDirections()) {
            Vec3 castStart = blockCenter.add(direction.scale(BLOCK_START_OFFSET));
            Vec3 castEnd = blockCenter.add(direction.scale(RANGE));

            BlockHitResult blockRaycastResult = blockEntity.getLevel().clip(
                    new ClipContext(
                            castStart,
                            castEnd,
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                            tempEntity
                    )
            );

            // Get the squared dist for performance (doesn't need to do sqrt)
            double blockDist = blockRaycastResult.getType() == HitResult.Type.MISS
                    ? Double.MAX_VALUE
                    : blockRaycastResult.getLocation().distanceToSqr(blockCenter);

            EntityHitResult entityRaycastResult = entityRaycast(
                    tempEntity,
                    blockEntity.getLevel(),
                    blockCenter,
                    castEnd,
                    e -> e != tempEntity
            );

            double entityDist = entityRaycastResult == null
                ? Double.MAX_VALUE
                : entityRaycastResult.getLocation().distanceToSqr(blockCenter);

            if (blockRaycastResult.getType() == HitResult.Type.BLOCK && blockDist < entityDist) {
                BlockPos pos = blockRaycastResult.getBlockPos();
                boolean addToList = true;

                for (Tuple<BlockPos, BlockState> data : blocks) {
                    if (
                            data.getA().distSqr(pos) == 0 // Check for duplicate
                    ) {
                        addToList = false;
                        break;
                    }
                }

                if (blockRaycastResult.getBlockPos().distSqr(blockEntity.getBlockPos()) == 0) {
                    addToList = false;
                    CCSensory.LOGGER.warn("LIDAR self collided!");
                }

                if (addToList) {
                    blocks.add(new Tuple<>(pos, blockEntity.getLevel().getBlockState(pos)));
                    // FIXME For TESTING ONLY
                    blockEntity.getLevel().setBlockAndUpdate(pos, Blocks.AMETHYST_BLOCK.defaultBlockState());
                }
            } else if (entityRaycastResult != null && entityDist < blockDist) {
                if (!entities.contains(entityRaycastResult.getEntity())) { // Prevent duplicates
                    entities.add(entityRaycastResult.getEntity());
                }
            }
        }

        tempEntity.remove(Entity.RemovalReason.DISCARDED);

        Map<Integer, ObjectLuaTable> blockDataMap = new HashMap<>(Map.of());

        int i = 1; // barf
        for (Tuple<BlockPos, BlockState> blockData: blocks) {
            BlockPos pos = blockData.getA();
            BlockState block = blockData.getB();
            blockDataMap.put(i, new ObjectLuaTable(Map.of(
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", pos.getX(),
                            "y", pos.getY(),
                            "z", pos.getZ()
                    )),
                    "id", block.getBlock().getName().getString()
            )));
            i++;
        }

        Map<Integer, ObjectLuaTable> entityDataMap = new HashMap<>(Map.of());

        i = 1;
        for (Entity entity : entities) {
            Vec3 pos = entity.getPosition(0);
            entityDataMap.put(i, new ObjectLuaTable(Map.of(
                    "name", entity.getName().toString(),
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", pos.x,
                            "y", pos.y,
                            "z", pos.z
                    ))
            )));
            i++;
        }

        return new ObjectLuaTable(Map.of(
                "blocks", blockDataMap,
                "entities", entityDataMap
        ));
    }

    @LuaFunction(mainThread = true)
    public int getCooldownTime(LidarSensorBlockEntity blockEntity) {
        assert blockEntity.getLevel() != null;
        return Math.toIntExact(Math.max(
                        blockEntity.getCooldownTick() - blockEntity.getLevel().getGameTime(),
                        0));
    }

    private ArrayList<Vec3> castDirections() {
        ArrayList<Vec3> list = new ArrayList<>();

        int i1 = 0;
        while (i1 <= 180 * RESOLUTION) {
            float xRot = i1 / RESOLUTION - 90f;
            int i2 = 0;
            while (i2 <= 360 * RESOLUTION) {
                float yRot = i2 / RESOLUTION;

                list.add(Vec3.directionFromRotation(xRot, yRot));
                i2++;
            }
            i1++;
        }

        return list;
    }

    // Yoinked from ProjectileUtil w/ some modification
    public static EntityHitResult entityRaycast(Entity sourceEntity, Level level, Vec3 rayStart, Vec3 rayEnd, Predicate<Entity> filter) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        Vec3 vec3 = null;
        AABB rayBox = new AABB(rayStart, rayEnd);

        for(Entity entity1 : level.getEntities(sourceEntity, rayBox, filter)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(rayStart, rayEnd);
            if (aabb.contains(rayStart)) {
                if (d0 >= (double)0.0F) {
                    entity = entity1;
                    vec3 = (Vec3)optional.orElse(rayStart);
                    d0 = (double)0.0F;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = (Vec3)optional.get();
                double d1 = rayStart.distanceToSqr(vec31);
                if (d1 < d0 || d0 == (double)0.0F) {
                    entity = entity1;
                    vec3 = vec31;
                    d0 = d1;
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }
}
