package io.github.faceinflux.ccsensory.misc.lidar;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public record EntityRaycastData(Entity entity, AABB aabb, double pickRadius) {}
