package io.github.faceinflux.ccsensory.util.blockentities;

import io.github.faceinflux.ccsensory.util.nbt.*;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.7 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
*///?}

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class MultiVersionBlockEntity extends BlockEntity {
    public HashMap<String, NBTDataEntry<?>> entries = new HashMap<>();

    protected MultiVersionBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    //? if >=1.21.7 {
    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        VersionAgnosticValueInput commonInput = new VanillaValueInput(valueInput);
        commonLoad(commonInput);
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        VersionAgnosticValueOutput commonOutput = new VanillaValueOutput(valueOutput);
        commonSave(commonOutput);
    }
    //?} else if 1.21.1 {
    /*@Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        VersionAgnosticValueInput commonInput = new CompoundTagValueIO(compoundTag);
        commonLoad(commonInput);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        VersionAgnosticValueOutput commonOutput = new CompoundTagValueIO(compoundTag);
        commonSave(commonOutput);
    }
    *///?} else {
    /*@Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        VersionAgnosticValueInput commonInput = new CompoundTagValueIO(compoundTag);
        commonLoad(commonInput);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        VersionAgnosticValueOutput commonOutput = new CompoundTagValueIO(compoundTag);
        commonSave(commonOutput);
    }
    *///?}

    protected <F extends FunctionalInterface> void commonLoad(
            VersionAgnosticValueInput input
    ) {
        for (Map.Entry<String, NBTDataEntry<?>> hashPair : entries.entrySet()) {
            loadEntry(input, hashPair.getKey(), hashPair.getValue());
        }
    }

    /** Load a singular entry. This method exists for the purpose of making the generic more
     * specific.*/
    private <T> void loadEntry(
            VersionAgnosticValueInput input,
            String id,
            NBTDataEntry<T> entry
    ) {
        // This is hacky, but idk if there's a better way to do it.
        Class<T> clazz = (Class<T>) entry.defaultValue().getClass();

        BiFunction<String, T, T> inputGetFunction = input.getFunctionWithDefault(clazz);

        entry.setter().apply(inputGetFunction.apply(id, entry.defaultValue()));
    }

    protected void commonSave(
            VersionAgnosticValueOutput output
    ) {
        for (Map.Entry<String, NBTDataEntry<?>> hashPair : entries.entrySet()) {
            saveEntry(output, hashPair.getKey(), hashPair.getValue());
        }
    }

    private <T> void saveEntry(
            VersionAgnosticValueOutput output,
            String id,
            NBTDataEntry<T> entry
    ) {
        // This is hacky, but idk if there's a better way to do it.
        Class<T> clazz = (Class<T>) entry.defaultValue().getClass();

        BiFunction<String, T, Void> outputSaveFunction = output.getFunction(clazz);

        outputSaveFunction.apply(id, entry.getter().get());
    }
}
