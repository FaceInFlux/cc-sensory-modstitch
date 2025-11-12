package io.github.faceinflux.ccsensory.content.blockentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.peripherals.LidarSensorPeripheral;
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
    /** The tick at which the cooldown will end */
    private Long cooldownTick = 0L;
    /** Get the tick at which the cooldown will end */
    public Long getCooldownTick() {return cooldownTick;} // I miss C# properties TwT
    /** Set the tick at which the cooldown will end and mark the chunk dirty */
    public void setCooldownTick(Long value) {
        cooldownTick = value;
        this.setChanged();
    }

    private final LidarSensorPeripheral peripheral = new LidarSensorPeripheral(this);

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

    public LidarSensorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get(), blockPos, blockState);

        entries.put("cooldownTick", new NBTDataEntry<>(
                () -> cooldownTick,
                (value) -> {
                    this.cooldownTick = (Long) value;
                    return null;
                },
                (long) 0L
        ));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LidarSensorBlockEntity e) {
        e.onCooldown = e.getCooldownTick() > level.getGameTime();
    }

    public IPeripheral peripheral() {
        return peripheral;
    }
}
