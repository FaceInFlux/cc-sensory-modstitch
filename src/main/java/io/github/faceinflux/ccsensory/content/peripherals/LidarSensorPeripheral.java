package io.github.faceinflux.ccsensory.content.peripherals;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import net.minecraft.world.entity.Entity;
//? if >=1.21.7 {
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity.ENTITY_CAST_INFLATION;
import static io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity.RESOLUTION;

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

    @LuaFunction(mainThread = true)
    public ObjectLuaTable getScanData(LidarSensorBlockEntity blockEntity) {
        return new ObjectLuaTable(Map.of(
                "status", blockEntity.status.ordinal(),
                "blocks", blockEntity.blockDataMap,
                "entities", blockEntity.entityDataMap
        ));
    }

    @LuaFunction(mainThread = true)
    public int getStatus(LidarSensorBlockEntity blockEntity) {
        return blockEntity.status.ordinal();
    }

    @LuaFunction(mainThread = true)
    public void startScan(LidarSensorBlockEntity blockEntity) {
        blockEntity.blockDataMap.clear();
        blockEntity.entityDataMap.clear();
        blockEntity.queuedCastDirections = castDirections();
        blockEntity.status = LidarSensorBlockEntity.STATUS.SCANNING;
    }

    @LuaFunction(mainThread = true)
    public void stopScan(LidarSensorBlockEntity blockEntity) {
        blockEntity.queuedCastDirections = null;
        blockEntity.status = LidarSensorBlockEntity.STATUS.IDLE;
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
