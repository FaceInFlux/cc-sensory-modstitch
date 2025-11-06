package io.github.faceinflux.ccsensory.content.blockentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import dan200.computercraft.api.lua.ObjectLuaTable;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.util.blockentities.MultiVersionBlockEntity;
import io.github.faceinflux.ccsensory.util.nbt.NBTDataEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.7 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.*;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class LidarSensorBlockEntity extends MultiVersionBlockEntity {
    /** The resolution of the scan in scans per degree */
    public static final float RESOLUTION = 0.75f;
    private static final double RANGE = 30;
    /** The offset when raycasting for blocks to prevent self collision */
    private static final double BLOCK_START_OFFSET = 0.91;
    public static final double ENTITY_CAST_INFLATION = 0.5;
    // For reference, TNT does like 1,000 in a tick
    // NOTE: I'm doing both block & entity casts separately so this is really half the casts/tick
    private static final int CASTS_PER_TICK = 500;

    /** The tick at which the cooldown will end */
    private Long cooldownTick;
    /** Get the tick at which the cooldown will end */
    public Long getCooldownTick() {return cooldownTick;} // I miss C# properties TwT
    /** Set the tick at which the cooldown will end and mark the chunk dirty */
    public void setCooldownTick(Long value) {
        cooldownTick = value;
        this.setChanged();
    }

    // TEMPORARY STATE STUFF
    // This should _probably_ be stored as NBT, but I don't wanna deal with serializing/deserializing
    // the data, and it's also a lot of data that we maybe don't wanna save.
    // CC loses state when chunks are unloaded anyways so
    public enum STATUS { // Could be a bool but I may wanna expand later
        /** Scanner is idle */
        IDLE,
        /** Scanner is currently running */
        SCANNING,
    }

    public STATUS status = STATUS.IDLE;
    public ArrayList<Vec3> queuedCastDirections;
    public Map<Integer, ObjectLuaTable> blockDataMap = new HashMap<>();
    public Map<Integer, ObjectLuaTable> entityDataMap = new HashMap<>();

    public LidarSensorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get(), blockPos, blockState);

        entries.put("cooldownTick", new NBTDataEntry<>(
                () -> cooldownTick,
                (value) -> {
                    this.cooldownTick = (Long) value;
                    CCSensory.LOGGER.info("Bweep bwomp!!!");
                    return null;
                },
                (long) 0L
        ));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LidarSensorBlockEntity e) {
        if (e.status != STATUS.SCANNING) {
            return;
        }

        ArrayList<Vec3> castDirections = new ArrayList<>();

        for (int i = 0; i < CASTS_PER_TICK; i++) {
            if (e.queuedCastDirections.size() - 1 < i) {
                break;
            }

            castDirections.add(e.queuedCastDirections.removeFirst());
        }

        ArrayList<Tuple<BlockPos, BlockState>> blocks = new ArrayList<>();
        ArrayList<Entity> entities = new ArrayList<>();

        Vec3 blockCenter = Vec3.atCenterOf(e.getBlockPos());

        Entity tempEntity = EntityType.ARMOR_STAND.create(
                level
                //? if >=1.21.7 {
                , EntitySpawnReason.MOB_SUMMONED
                //?}
        );
        assert tempEntity != null;
        tempEntity.setPos(blockCenter);
        tempEntity.setInvisible(true);
        tempEntity.setInvulnerable(true);
        tempEntity.setNoGravity(true);
        level.addFreshEntity(tempEntity);
        for (Vec3 direction : castDirections) {
            Vec3 castStart = blockCenter.add(direction.scale(BLOCK_START_OFFSET));
            Vec3 castEnd = blockCenter.add(direction.scale(RANGE));

            BlockHitResult blockRaycastResult = e.getLevel().clip(
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
                    e.getLevel(),
                    blockCenter,
                    castEnd,
                    entity -> entity != tempEntity
            );

            double entityDist = entityRaycastResult == null
                    ? Double.MAX_VALUE
                    : entityRaycastResult.getLocation().distanceToSqr(blockCenter);

            if (blockRaycastResult.getType() == HitResult.Type.BLOCK && blockDist < entityDist) {
                BlockPos blockPos = blockRaycastResult.getBlockPos();
                boolean addToList = true;

                for (Tuple<BlockPos, BlockState> data : blocks) {
                    if (
                            data.getA().distSqr(blockPos) == 0 // Check for duplicate
                    ) {
                        addToList = false;
                        break;
                    }
                }

                if (blockRaycastResult.getBlockPos().distSqr(e.getBlockPos()) == 0) {
                    addToList = false;
                    CCSensory.LOGGER.warn("LIDAR self collided!");
                }

                if (addToList) {
                    blocks.add(new Tuple<>(blockPos, e.getLevel().getBlockState(blockPos)));
                    // FIXME For TESTING ONLY
//                    e.getLevel().setBlockAndUpdate(blockPos, Blocks.AMETHYST_BLOCK.defaultBlockState());
                }
            } else if (entityRaycastResult != null && entityDist < blockDist) {
                if (!entities.contains(entityRaycastResult.getEntity())) { // Prevent duplicates
                    entities.add(entityRaycastResult.getEntity());
                }
            }
        }

        tempEntity.remove(Entity.RemovalReason.DISCARDED);

        int i = 1; // barf
        for (Tuple<BlockPos, BlockState> blockData: blocks) {
            BlockPos blockPos = blockData.getA();
            BlockState block = blockData.getB();
            e.blockDataMap.put(i, new ObjectLuaTable(Map.of(
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", blockPos.getX(),
                            "y", blockPos.getY(),
                            "z", blockPos.getZ()
                    )),
                    "id", block.getBlock().getName().getString()
            )));
            i++;
        }

        i = 1;
        for (Entity entity : entities) {
            Vec3 entityPos = entity.getPosition(0);
            e.entityDataMap.put(i, new ObjectLuaTable(Map.of(
                    "name", entity.getName().toString(),
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", entityPos.x,
                            "y", entityPos.y,
                            "z", entityPos.z
                    ))
            )));
            i++;
        }

        if (e.queuedCastDirections.isEmpty()) {
            e.status = STATUS.IDLE;
        }
    }

    // Yoinked from ProjectileUtil w/ some modification
    public static EntityHitResult entityRaycast(Entity sourceEntity, Level level, Vec3 rayStart, Vec3 rayEnd, Predicate<Entity> filter) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        Vec3 vec3 = null;
        AABB rayBox = new AABB(rayStart, rayEnd).inflate(ENTITY_CAST_INFLATION);

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
