package io.github.faceinflux.ccsensory.content.blockentities;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.faceinflux.ccsensory.util.blockentities.MultiVersionBlockEntity;
import io.github.faceinflux.ccsensory.util.nbt.NBTDataEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.7 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class LidarSensorBlockEntity extends MultiVersionBlockEntity {
    /** The tick at which the cooldown will end */
    private int cooldownTick;
    /** Get the tick at which the cooldown will end */
    public int getCooldownTick() {return cooldownTick;} // I miss C# properties TwT
    /** Set the tick at which the cooldown will end and mark the chunk dirty */
    public void setCooldownTick(int value) {
        cooldownTick = value;
        this.setChanged();
    }

    public LidarSensorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get(), blockPos, blockState);

        entries.put("cooldownTick", new NBTDataEntry<>(
                () -> cooldownTick,
                (value) -> {
                    cooldownTick = value;
                    return null;
                },
                0
        ));
    }
}
