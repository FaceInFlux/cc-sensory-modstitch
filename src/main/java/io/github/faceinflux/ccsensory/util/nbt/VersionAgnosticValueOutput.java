package io.github.faceinflux.ccsensory.util.nbt;

import java.util.Optional;
import java.util.function.BiFunction;

public interface VersionAgnosticValueOutput {
    Void putByte(String id, Byte value);
    Void putShort(String id, Short value);
    Void putInt(String id, Integer value);
    Void putLong(String id, Long value);
    Void putFloat(String id, Float value);
    Void putDouble(String id, Double value);
    Void putString(String id, String value);
    Void putIntArray(String id, int[] value);
    Void putBool(String id, Boolean value);

    default <T> BiFunction<String, T, Void> getFunction(Class<T> clazz) {
        if (clazz == Byte.class) {
            return (id, value) -> putByte(id, (Byte) value);
        } else if (clazz == Short.class) {
            return (id, value) -> putShort(id, (Short) value);
        } else if (clazz == Integer.class) {
            return (id, value) -> putInt(id, (Integer) value);
        } else if (clazz == Long.class) {
            return (id, value) -> putLong(id, (Long) value);
        } else if (clazz == Float.class) {
            return (id, value) -> putFloat(id, (Float) value);
        } else if (clazz == Double.class) {
            return (id, value) -> putDouble(id, (Double) value);
        } else if (clazz == String.class) {
            return (id, value) -> putString(id, (String) value);
        } else if (clazz == int[].class) {
            return (id, value) -> putIntArray(id, (int[]) value);
        } else if (clazz == Boolean.class) {
            return (id, value) -> putBool(id, (Boolean) value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz);
        }
    }
}
