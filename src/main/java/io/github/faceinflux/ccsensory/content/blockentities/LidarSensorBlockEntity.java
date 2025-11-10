package io.github.faceinflux.ccsensory.content.blockentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dan200.computercraft.api.lua.ObjectLuaTable;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.misc.lidar.EntityRaycastData;
import io.github.faceinflux.ccsensory.misc.lidar.LidarRaycastManager;
import io.github.faceinflux.ccsensory.misc.lidar.LidarScanRequest;
import io.github.faceinflux.ccsensory.util.blockentities.MultiVersionBlockEntity;
import io.github.faceinflux.ccsensory.util.nbt.NBTDataEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;
//? if >=1.21.7 {

//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class LidarSensorBlockEntity extends MultiVersionBlockEntity {
    // FIXME Constants are wack; put all these in reasonable location(s)
    /** The resolution of the scan in scans per degree */
    public static final float RESOLUTION = 0.75f;
    public static final double RANGE = 30;
    /** The offset when raycasting for blocks to prevent self collision */
    private static final double BLOCK_START_OFFSET = 0.91;
    public static final double ENTITY_CAST_INFLATION = 0.5;
    // For reference, TNT does like 1,000 in a tick
    // NOTE: I'm doing both block & entity casts separately so this is really half the casts/tick
    private static final int CASTS_PER_TICK = 500;
    private static final long COOLDOWN_TIME = 10 * 20;

    /** The tick at which the cooldown will end */
    private Long cooldownTick = 0L;
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
    private boolean onCooldown = false;

    // REQUEST GENERATION
    private boolean requestDataGatherRunning = false;
    private Double requestRange;
    private ArrayList<Vec3> requestDirections;
    private Integer requestID;

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

    public synchronized boolean startGatheringRequestData(double range, ArrayList<Vec3> directions) {
        if (onCooldown || requestDataGatherRunning) {
            return false;
        } else {
            requestID = null;
            requestRange = range;
            requestDirections = directions;
            requestDataGatherRunning = true;
            return true;
        }
    }

    public synchronized @Nullable Integer getRequestID() {
        Integer id = requestID;
        requestID = null;
        return id;
    }

    private synchronized void resetRequestDataInput() {
        requestDataGatherRunning = false;
        requestRange = null;
        requestDirections = null;
    }

    private void gatherRequestData() {
        assert level != null;
        HashMap<BlockPos, BlockState> blocks = new HashMap<>();
        ArrayList<EntityRaycastData> entities = new ArrayList<>();

        // Brute force method to get blocks in sphere (don't murder me please)
        for (int x = (int) -requestRange; x < requestRange; x++) {
            for (int y = (int) -requestRange; y < requestRange; y++) {
                for (int z = (int) -requestRange; z < requestRange; z++) {
                    BlockPos pos = getBlockPos().offset(new BlockPos(x, y, z));
                    if (pos.distSqr(getBlockPos()) <= (Math.pow(requestRange,2))) {
                        blocks.put(pos, level.getBlockState(pos));
                    }
                }
            }
        }

        for (Entity entity : level.getEntities(
                null, new AABB(-requestRange, -requestRange, -requestRange, requestRange, requestRange, requestRange).move(getBlockPos()))) {
            if (entity.distanceToSqr(getBlockPos().getCenter()) <= (Math.pow(requestRange, 2))) {
                entities.add(new EntityRaycastData(entity, entity.getBoundingBox(), entity.getPickRadius()));
            }
        }

        requestID = LidarRaycastManager.queueScan(new LidarScanRequest(blocks, entities, getBlockPos(), requestDirections, requestRange));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LidarSensorBlockEntity e) {
        e.onCooldown = e.getCooldownTick() > level.getGameTime();

        if (e.requestDataGatherRunning) {
            e.setCooldownTick(level.getGameTime() + COOLDOWN_TIME);
            e.gatherRequestData();
            e.resetRequestDataInput();
        }
    }
}
