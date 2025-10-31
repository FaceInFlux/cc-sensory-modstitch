package io.github.faceinflux.ccsensory.content.blockentities;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.7 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class LidarSensorBlockEntity extends BlockEntity {
    /** The tick at which the cooldown will end */
    private int cooldownTick;

    public LidarSensorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    //? if >=1.21.7 {
    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        commonLoad(valueInput);
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        commonSave(valueOutput);
    }
    //?} else if 1.21.1 {
    /*@Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        commonLoad(compoundTag);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        commonSave(compoundTag);
    }
    *///?} else {
    /*@Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        commonLoad(compoundTag);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        commonSave(compoundTag);
    }
    *///?}

    private <T> T getOrDefault(
            /*? if <1.21.7 {*/ /*CompoundTag *//*?} else {*/ ValueInput /*?}*/ input,
            Function<
                String,
                /*? if <1.21.7 {*/ /*T *//*?} else {*/ Optional<T> /*?}*/
            > getFunction, // :sob:
            String id,
            T defaultTo
    ) {
        //? if <1.21.7 {
        /*if (input.contains(id)) {
            return getFunction.apply(id);
        } else {
            return defaultTo;
        }
        *///?} else {
        return getFunction.apply(id).orElse(defaultTo);
        //?}
    }

    private void commonLoad(
            /*? if <1.21.7 {*/ /*CompoundTag *//*?} else {*/ ValueInput /*?}*/ input
    ) {
        this.cooldownTick = getOrDefault(input, input::getInt, "cooldownTick", 0);
    }

    private void commonSave(
            /*? if <1.21.7 {*/ /*CompoundTag *//*?} else {*/ ValueOutput /*?}*/ output
    ) {
        output.putInt("cooldownTick", cooldownTick);
    }
}
