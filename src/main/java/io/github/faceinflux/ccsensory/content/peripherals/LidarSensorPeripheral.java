package io.github.faceinflux.ccsensory.content.peripherals;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.GenericPeripheral;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity.*;

public class LidarSensorPeripheral implements GenericPeripheral {
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

//    @LuaFunction(mainThread = true)
//    public ObjectLuaTable getScanData(LidarSensorBlockEntity blockEntity) {
//        return new ObjectLuaTable(Map.of(
//                "status", blockEntity.status.ordinal(),
//                "blocks", blockEntity.blockDataMap,
//                "entities", blockEntity.entityDataMap
//        ));
//    }

//    @LuaFunction(mainThread = true)
//    public int getStatus(LidarSensorBlockEntity blockEntity) {
//        return blockEntity.status.ordinal();
//    }

//    @LuaFunction(mainThread = true)
//    public void startScan(LidarSensorBlockEntity blockEntity) {
//        blockEntity.blockDataMap.clear();
//        blockEntity.entityDataMap.clear();
//        blockEntity.queuedCastDirections = castDirections();
//        blockEntity.status = LidarSensorBlockEntity.STATUS.SCANNING;
//    }
//
//    @LuaFunction(mainThread = true)
//    public void stopScan(LidarSensorBlockEntity blockEntity) {
//        blockEntity.queuedCastDirections = null;
//        blockEntity.status = LidarSensorBlockEntity.STATUS.IDLE;
//    }

    @LuaFunction(mainThread = true)
    public MethodResult scan(LidarSensorBlockEntity blockEntity) {
        assert blockEntity.getLevel() != null;
        Level level = blockEntity.getLevel();
        HashMap<BlockPos, BlockState> blocks = new HashMap<>();
        ArrayList<EntityRaycastData> entities = new ArrayList<>();

        // Brute force method to get blocks in sphere (don't murder me please)
        for (int x = (int) -RANGE; x < RANGE; x++) {
            for (int y = (int) -RANGE; y < RANGE; y++) {
                for (int z = (int) -RANGE; z < RANGE; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (pos.distSqr(blockEntity.getBlockPos()) <= (Math.pow(RANGE,2))) {
                        blocks.put(pos, level.getBlockState(pos));
                    }
                }
            }
        }

        for (Entity entity : level.getEntities(null, new AABB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE))) {
            if (entity.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= (Math.pow(RANGE, 2))) {
                entities.add(new EntityRaycastData(entity, entity.getBoundingBox(), entity.getPickRadius()));
            }
        }

        int requestId = LidarRaycastManager.queueScan(new LidarScanRequest(blocks, entities, blockEntity.getBlockPos(), castDirections(), RANGE));

        return MethodResult.pullEvent(null, new ILuaCallback() {
            @Override
            public MethodResult resume(@Nullable Object[] args) throws LuaException {
                if (LidarRaycastManager.isReady(requestId)) {
                    LidarScanResult result = LidarRaycastManager.pullResult(requestId);
                    return MethodResult.of(generateLuaOutput(result));
                } else {
                    return MethodResult.pullEvent(null, this);
                }
            }
        });
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
                    "id", block.getBlock().getName().getString()
            )));
            i++;
        }

        i = 1;
        for (Entity entity : scanResult.entities()) {
            Vec3 entityPos = entity.getPosition(0);
            entityDataMap.put(i, new ObjectLuaTable(Map.of(
                    "name", entity.getName().toString(),
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
