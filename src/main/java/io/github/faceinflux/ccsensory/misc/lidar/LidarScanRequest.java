package io.github.faceinflux.ccsensory.misc.lidar;

import io.github.faceinflux.ccsensory.content.peripherals.LidarSensorPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class LidarScanRequest {
    public static final double RANGE_DEFAULT = 30;

    public final HashMap<BlockPos, BlockState> blocks;
    // The thread _could_ grab the AABB from the Entity, but I think I wanna avoid the thread
    // touching the entity directly because concurrency be concurring
    public final ArrayList<EntityRaycastData> entities;
    public final BlockPos lidarPos;
    public final ArrayList<Vec3> directions;
    public final double range;
    public final LidarSensorPeripheral consumer;

    public LidarScanRequest(
            HashMap<BlockPos, BlockState> blocks, ArrayList<EntityRaycastData> entities,
            BlockPos lidarPos, ArrayList<Vec3> directions,
            LidarSensorPeripheral consumer, double range
    ) {
        this.blocks = blocks;
        this.entities = entities;
        this.lidarPos = lidarPos;
        this.directions = directions;
        this.range = range;
        this.consumer = consumer;
    }

    public LidarScanRequest(
            HashMap<BlockPos, BlockState> blocks, ArrayList<EntityRaycastData> entities,
            BlockPos lidarPos, ArrayList<Vec3> directions, LidarSensorPeripheral consumer
    ) {
      this(blocks, entities, lidarPos, directions, consumer, RANGE_DEFAULT);
    }
}
