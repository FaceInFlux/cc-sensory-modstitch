package io.github.faceinflux.ccsensory.content.peripherals;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.GenericPeripheral;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity.*;

public class LidarSensorPeripheral implements IPeripheral {
    private final LidarSensorBlockEntity blockEntity;

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
    public MethodResult scan() {
        if (!blockEntity.startGatheringRequestData(RANGE, castDirections())) {
            return MethodResult.of(); // If requesting the data gathering failed
        }

        // Made a final array so it can be accessed inside the callback. The linter told me to do this :p
        final Integer[] id = {null};

        ILuaCallback callbackLoop = new ILuaCallback() {
            @Override
            public MethodResult resume(@Nullable Object[] args) throws LuaException {
                id[0] = id[0] == null ? blockEntity.getRequestID() : id[0];

                if (id[0] != null && LidarRaycastManager.isReady(id[0])) {
                    LidarScanResult result = LidarRaycastManager.pullResult(id[0]);
                    return MethodResult.of(generateLuaOutput(result));
                } else {
                    return MethodResult.pullEvent(null, this);
                }
            }
        };

        Supplier<MethodResult> pull = () -> {
            return MethodResult.pullEvent(null, callbackLoop);
        };



        return pull.get();
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
