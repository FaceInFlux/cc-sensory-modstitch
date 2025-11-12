// WARNING: THIS CODE IS EXTREMELY "a little silly"! READ AT THE RISK OF YOUR SANITY.
package io.github.faceinflux.ccsensory.misc.lidar;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

// Can only be accessed from classes in the `lidar` package (I think)
class LidarRaycastThread extends Thread {
    private static final float RAY_INCREMENT = 0.2f;
    private static final float RAY_START_OFFSET = 0.91f;

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                handleRequest(LidarRaycastManager.dequeueRequest());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void handleRequest(LidarScanRequest request) {
        HashSet<Tuple<BlockPos, BlockState>> hitBlocks = new HashSet<>();
        HashSet<Entity> hitEntities = new HashSet<>();

        for (Vec3 direction : request.directions) {
            Tuple<BlockPos, BlockState> block = blockRaycast(request, direction);
            EntityHitResult entity = entityRaycast(request, direction);

            double blockDistanceSqr = block != null
                    ? block.getA().distSqr(request.lidarPos)
                    : Double.MAX_VALUE;

            double entityDistanceSqr = entity != null
                    ? entity.getLocation().distanceToSqr(request.lidarPos.getCenter())
                    : Double.MAX_VALUE;

            if (block != null && blockDistanceSqr < entityDistanceSqr) {
                hitBlocks.add(block);
            } else if (entity != null && entityDistanceSqr < blockDistanceSqr) {
                hitEntities.add(entity.getEntity());
            }
        }

        request.consumer.pushResult(new LidarScanResult(
                new ArrayList<>(hitBlocks),
                new ArrayList<>(hitEntities)
        ));
    }

    private @Nullable Tuple<BlockPos, BlockState> blockRaycast(LidarScanRequest request, Vec3 direction) {
        float marchDistance = RAY_START_OFFSET;

        while (marchDistance < request.range) {
            Vec3 marchPos = request.lidarPos.getCenter().add(direction.scale(marchDistance));
            marchDistance += RAY_INCREMENT; // For next iteration.

            BlockPos blockPos = new BlockPos(
                    (int) Math.ceil(marchPos.x),
                    (int) Math.ceil(marchPos.y),
                    (int) Math.ceil(marchPos.z)
            );

            if (request.blocks.containsKey(blockPos) && !request.blocks.get(blockPos).isAir()) {
                return new Tuple<>(blockPos, request.blocks.get(blockPos));
            }
        }
        return null;
    }

    private @Nullable EntityHitResult entityRaycast(LidarScanRequest request, Vec3 direction) {
        Vec3 rayStart = request.lidarPos.getCenter().add(direction.scale(RAY_START_OFFSET));
        Vec3 rayEnd = request.lidarPos.getCenter().add(direction.scale(request.range));

        for (EntityRaycastData data : request.entities) {
            AABB aabb = data.aabb().inflate(data.pickRadius());
            Entity e = data.entity();
            if (aabb.distanceToSqr(request.lidarPos.getCenter()) > (Math.pow(request.range,2))) {
                continue; // Discard entities out of range; grabs in box & we want sphere
            }

            Optional<Vec3> pos = aabb.clip(rayStart, rayEnd);
            if (pos.isPresent()) {
                return new EntityHitResult(e, pos.get());
            }
        }
        return null;
    }
}
