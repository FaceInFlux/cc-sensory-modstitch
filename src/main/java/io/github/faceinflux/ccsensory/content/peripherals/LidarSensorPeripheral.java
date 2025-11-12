package io.github.faceinflux.ccsensory.content.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import io.github.faceinflux.ccsensory.misc.lidar.EntityRaycastData;
import io.github.faceinflux.ccsensory.misc.lidar.LidarRaycastManager;
import io.github.faceinflux.ccsensory.misc.lidar.LidarScanRequest;
import io.github.faceinflux.ccsensory.misc.lidar.LidarScanResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
//? if >=1.21.7 {
//?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LidarSensorPeripheral implements IPeripheral {
    /** The resolution of the scan in scans per degree */
    public static final float RESOLUTION = 0.75f;
    public static final double RANGE = 30;
    private static final String READY_EVENT_NAME = "lidar_sensor_finished";

    private final LidarSensorBlockEntity blockEntity;

    private IComputerAccess activeComputer;
    private LidarScanResult scanResult;

    public LidarSensorPeripheral(LidarSensorBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public String getType() {
        return CCSensory.ID + ":lidar_sensor";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof LidarSensorPeripheral o && blockEntity == o.blockEntity;
    }

    @LuaFunction
    public MethodResult scan(IComputerAccess computer, ILuaContext context) {
        if (activeComputer != null) {
            return MethodResult.of((Object) null); // If another computer is waiting.
        }

        activeComputer = computer;

        try {
            context.executeMainThreadTask(() -> gatherRequestData(RANGE));

            return getScanCallbackLoop();
        } catch (LuaException e) {
            activeComputer = null;
            scanResult = null;
            return MethodResult.of((Object) null);
        }
    }

    private @NotNull MethodResult getScanCallbackLoop() {
        return MethodResult.pullEvent(READY_EVENT_NAME, args -> {
            if (scanResult != null) {
                LidarScanResult result = scanResult;
                scanResult = null;
                return MethodResult.of(generateLuaOutput(result));
            } else {
                return getScanCallbackLoop();
            }
        });
    }

    public synchronized void pushResult(LidarScanResult result) {
        if (activeComputer != null) {
            this.scanResult = result;
            activeComputer.queueEvent(READY_EVENT_NAME);
            activeComputer = null; // No longer needed
        }
    }

    private @Nullable Object @Nullable [] gatherRequestData(double requestRange) {
        assert blockEntity.getLevel() != null;
        HashMap<BlockPos, BlockState> blocks = new HashMap<>();
        ArrayList<EntityRaycastData> entities = new ArrayList<>();

        // Brute force method to get blocks in sphere (don't murder me please)
        for (int x = (int) -requestRange; x < requestRange; x++) {
            for (int y = (int) -requestRange; y < requestRange; y++) {
                for (int z = (int) -requestRange; z < requestRange; z++) {
                    BlockPos pos = blockEntity.getBlockPos().offset(new BlockPos(x, y, z));
                    if (pos.distSqr(blockEntity.getBlockPos()) <= (Math.pow(requestRange,2))) {
                        blocks.put(pos, blockEntity.getLevel().getBlockState(pos));
                    }
                }
            }
        }

        for (Entity entity : blockEntity.getLevel().getEntities(
                null, new AABB(-requestRange, -requestRange, -requestRange, requestRange, requestRange, requestRange).move(blockEntity.getBlockPos()))) {
            if (entity.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= (Math.pow(requestRange, 2))) {
                entities.add(new EntityRaycastData(entity, entity.getBoundingBox(), entity.getPickRadius()));
            }
        }

        LidarRaycastManager.queueScan(new LidarScanRequest(
                blocks, entities, blockEntity.getBlockPos(), castDirections(), this, requestRange));
        return null; // Apparently this is required for a LuaTask
    }

    private ObjectLuaTable generateLuaOutput(LidarScanResult scanResult) {
        Map<Integer, ObjectLuaTable> blockDataMap = new HashMap<>();
        Map<Integer, ObjectLuaTable> entityDataMap = new HashMap<>();

        int i = 1; // barf
        for (Tuple<BlockPos, BlockState> blockData: scanResult.blocks()) {
            BlockPos blockPos = blockData.getA();
            BlockState block = blockData.getB();
            blockDataMap.put(i, new ObjectLuaTable(Map.of(
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", blockPos.getX(),
                            "y", blockPos.getY(),
                            "z", blockPos.getZ()
                    )),
                    "key", block.getBlock().getDescriptionId(),
                    "color", Integer.toHexString(block.getBlock().defaultMapColor().col)
            )));
            i++;
        }

        i = 1;
        for (Entity entity : scanResult.entities()) {
            Vec3 entityPos = entity.getPosition(0);
            entityDataMap.put(i, new ObjectLuaTable(Map.of(
                    "name", getEntityName(entity),
                    "key", entity.getType().getDescriptionId(),
                    "relativePosition", new ObjectLuaTable(Map.of(
                            "x", entityPos.x,
                            "y", entityPos.y,
                            "z", entityPos.z
                    ))
            )));
            i++;
        }

        return new ObjectLuaTable(Map.of(
                "blocks", blockDataMap,
                "entities", entityDataMap
        ));
    }

    private String getEntityName(Entity entity) {
        if (entity.hasCustomName()) {
            return entity.getCustomName().getString();
        } else if (entity.getType() == EntityType.PLAYER) {
            return entity.getName().getString();
        } else {
            return entity.getType().getDescriptionId();
        }
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
}
