package io.github.faceinflux.ccsensory.util;

import java.util.function.Supplier;

public abstract class RegistryEntry<R, T extends R> implements Supplier<T> {
    public final String id;

    /** The supplier created while registering. this.get() will return the get val of this,
     * so it only needs to be directly accessed during registration. */
    public Supplier<T> returnSupplier;

    protected RegistryEntry(String id) {
        this.id = id;
    }

    @Override
    public T get() {
        return returnSupplier.get();
    }
}
