package io.github.faceinflux.ccsensory.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryEntry<R, T extends R> implements Supplier<T> {
    public final String id;
    /** The supplier used to create this registryEntry. This should pretty much only be accessed
     * during registration in loader-specific code.*/
    public final Supplier<T> creationSupplier;

    /** The supplier created while registering. this.get() will return the get val of this,
     * so it only needs to be directly accessed during registration. */
    public Supplier<T> returnSupplier;

    public RegistryEntry(String id, Supplier<T> supplier) {
        this.id = id;
        this.creationSupplier = supplier;
    }

    @Override
    public T get() {
        return returnSupplier.get();
    }
}
