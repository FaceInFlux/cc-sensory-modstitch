package io.github.faceinflux.ccsensory.util.nbt;

import java.util.function.Function;
import java.util.function.Supplier;

public record NBTDataEntry<T>(Supplier<T> getter, Function<T, Void> setter, T defaultValue) {}
