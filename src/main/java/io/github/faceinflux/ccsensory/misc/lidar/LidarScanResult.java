package io.github.faceinflux.ccsensory.misc.lidar;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public record LidarScanResult(ArrayList<Tuple<BlockPos, BlockState>> blocks, ArrayList<Entity> entities) { }
