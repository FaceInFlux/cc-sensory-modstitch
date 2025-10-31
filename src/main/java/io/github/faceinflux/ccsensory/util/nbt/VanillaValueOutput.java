//? if >=1.21.7 {
package io.github.faceinflux.ccsensory.util.nbt;

import net.minecraft.world.level.storage.ValueOutput;

public record VanillaValueOutput(ValueOutput output) implements VersionAgnosticValueOutput {

    @Override
    public Void putByte(String id, Byte value) {
        output.putByte(id, value);
        return null;
    }

    @Override
    public Void putShort(String id, Short value) {
        output.putShort(id, value);
        return null;
    }

    @Override
    public Void putInt(String id, Integer value) {
        output.putInt(id, value);
        return null;
    }

    @Override
    public Void putLong(String id, Long value) {
        output.putLong(id, value);
        return null;
    }

    @Override
    public Void putFloat(String id, Float value) {
        output.putFloat(id, value);
        return null;
    }

    @Override
    public Void putDouble(String id, Double value) {
        output.putDouble(id, value);
        return null;
    }

    @Override
    public Void putString(String id, String value) {
        output.putString(id, value);
        return null;
    }

    @Override
    public Void putIntArray(String id, int[] value) {
        output.putIntArray(id, value);
        return null;
    }

    @Override
    public Void putBool(String id, Boolean value) {
        output.putBoolean(id, value);
        return null;
    }
}
//?}