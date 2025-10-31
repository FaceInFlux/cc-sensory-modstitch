package io.github.faceinflux.ccsensory.util.nbt;

import java.util.Optional;
import java.util.function.BiFunction;

public interface VersionAgnosticValueInput {
    Optional<Byte> getByte(String id);
    Optional<Short> getShort(String id);
    Optional<Integer> getInt(String id);
    Optional<Long> getLong(String id);
    Optional<Float> getFloat(String id);
    Optional<Double> getDouble(String id);
    Optional<String> getString(String id);
    Optional<int[]> getIntArray(String id);
    Optional<Boolean> getBool(String id);

    default Byte getByteOr(String id, Byte defaultValue) {
        return getByte(id).orElse(defaultValue);
    };

    default Short getShortOr(String id, Short defaultValue) {
        return getShort(id).orElse(defaultValue);
    }

    default Integer getIntOr(String id, Integer defaultValue) {
        return getInt(id).orElse(defaultValue);
    }

    default Long getLongOr(String id, Long defaultValue) {
        return getLong(id).orElse(defaultValue);
    }

    default Float getFloatOr(String id, Float defaultValue) {
        return getFloat(id).orElse(defaultValue);
    }

    default Double getDoubleOr(String id, Double defaultValue) {
        return getDouble(id).orElse(defaultValue);
    }

    default String getStringOr(String id, String defaultValue) {
        return getString(id).orElse(defaultValue);
    }

    default int[] getIntArrayOr(String id, int[] defaultValue) {
        return getIntArray(id).orElse(defaultValue);
    }

    default Boolean getBoolOr(String id, Boolean defaultValue) {
        return getBool(id).orElse(defaultValue);
    }

    @SuppressWarnings("unchecked")
    default <T> BiFunction<String, T, T> getFunctionWithDefault(Class<T> clazz) {
        if (clazz == Byte.class) {
            return (id, defaultValue) -> (T) getByteOr(id, (Byte) defaultValue);
        } else if (clazz == Short.class) {
            return (id, defaultValue) -> (T) getShortOr(id, (Short) defaultValue);
        } else if (clazz == Integer.class) {
            return (id, defaultValue) -> (T) getIntOr(id, (Integer) defaultValue);
        } else if (clazz == Long.class) {
            return (id, defaultValue) -> (T) getLongOr(id, (Long) defaultValue);
        } else if (clazz == Float.class) {
            return (id, defaultValue) -> (T) getFloatOr(id, (Float) defaultValue);
        } else if (clazz == Double.class) {
            return (id, defaultValue) -> (T) getDoubleOr(id, (Double) defaultValue);
        } else if (clazz == String.class) {
            return (id, defaultValue) -> (T) getStringOr(id, (String) defaultValue);
        } else if (clazz == int[].class) {
            return (id, defaultValue) -> (T) getIntArrayOr(id, (int[]) defaultValue);
        } else if (clazz == Boolean.class) {
            return (id, defaultValue) -> (T) getBoolOr(id, (Boolean) defaultValue);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz);
        }
    }
}
