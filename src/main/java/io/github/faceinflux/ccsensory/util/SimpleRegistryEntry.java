package io.github.faceinflux.ccsensory.util;

import java.util.function.Supplier;

public class SimpleRegistryEntry<R, T extends R> extends RegistryEntry<R, T> {
    /** The supplier used to create this registryEntry. This should pretty much only be accessed
     * during registration in loader-specific code.*/
    public final Supplier<T> creationSupplier;

    public SimpleRegistryEntry(String id, Supplier<T> supplier) {
        super(id);
        this.creationSupplier = supplier;
    }
}
