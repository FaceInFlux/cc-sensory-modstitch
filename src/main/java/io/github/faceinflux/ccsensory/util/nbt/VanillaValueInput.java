//? if >=1.21.7 {
package io.github.faceinflux.ccsensory.util.nbt;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Optional;

/**
 * @param input ValueInput seems really inconsistent as to which types for which you can get an optional vs which types you can only set a default.
 */
public record VanillaValueInput(ValueInput input) implements VersionAgnosticValueInput {
    private boolean contains(String id, Codec<?> codec) {
        //? if fabric {
        return input.read(id, codec).isPresent(); // Could use contains but that only works on 1.21.8
         //?} else if neoforge {
        /*return input.keySet().contains(id);
        *///?}
    }

    @Override
    public Optional<Byte> getByte(String id) {
        if (contains(id, Codec.BYTE)) {
            return Optional.of(input.getByteOr(id, (byte) 0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Short> getShort(String id) {
        if (contains(id, Codec.SHORT)) {
            // getShortOr returns an int.
            return Optional.of((short) input.getShortOr(id, (short) 0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getInt(String id) {
        return input.getInt(id);
    }

    @Override
    public Optional<Long> getLong(String id) {
        return input.getLong(id);
    }

    @Override
    public Optional<Float> getFloat(String id) {
        if (contains(id, Codec.FLOAT)) {
            return Optional.of(input.getFloatOr(id, 0.0f));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Double> getDouble(String id) {
        if (contains(id, Codec.DOUBLE)) {
            return Optional.of(input.getDoubleOr(id, 0.0d));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getString(String id) {
        return input.getString(id);
    }

    @Override
    public Optional<int[]> getIntArray(String id) {
        return input.getIntArray(id);
    }

    @Override
    public Optional<Boolean> getBool(String id) {
        if (contains(id, Codec.BOOL)) {
            return Optional.of(input.getBooleanOr(id, false));
        }
        return Optional.empty();
    }
}
//?}