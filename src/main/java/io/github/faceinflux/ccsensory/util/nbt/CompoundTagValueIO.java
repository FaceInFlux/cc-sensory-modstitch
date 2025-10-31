//? if <1.21.7 {
/*package io.github.faceinflux.ccsensory.util.nbt;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class CompoundTagValueIO implements VersionAgnosticValueInput, VersionAgnosticValueOutput{
    public final CompoundTag tag;

    public CompoundTagValueIO(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public Optional<Byte> getByte(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getByte(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Short> getShort(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getShort(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getInt(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getInt(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Long> getLong(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getLong(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Float> getFloat(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getFloat(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Double> getDouble(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getDouble(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getString(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getString(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<int[]> getIntArray(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getIntArray(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> getBool(String id) {
        if (tag.contains(id)) {
            return Optional.of(tag.getBoolean(id));
        }
        return Optional.empty();
    }

    @Override
    public Void putByte(String id, Byte value) {
        tag.putByte(id, value);
        return null;
    }

    @Override
    public Void putShort(String id, Short value) {
        tag.putShort(id, value);
        return null;
    }

    @Override
    public Void putInt(String id, Integer value) {
        tag.putInt(id, value);
        return null;
    }

    @Override
    public Void putLong(String id, Long value) {
        tag.putLong(id, value);
        return null;
    }

    @Override
    public Void putFloat(String id, Float value) {
        tag.putFloat(id, value);
        return null;
    }

    @Override
    public Void putDouble(String id, Double value) {
        tag.putDouble(id, value);
        return null;
    }

    @Override
    public Void putString(String id, String value) {
        tag.putString(id, value);
        return null;
    }

    @Override
    public Void putIntArray(String id, int[] value) {
        tag.putIntArray(id, value);
        return null;
    }

    @Override
    public Void putBool(String id, Boolean value) {
        tag.putBoolean(id, value);
        return null;
    }
}
*///?}