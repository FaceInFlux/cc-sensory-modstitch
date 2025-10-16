package io.github.faceinflux.ccsensory.util;

import io.github.faceinflux.ccsensory.CCSensory;

import java.util.HashMap;
import java.util.function.Supplier;

public class Register<R> extends HashMap<String, RegistryEntry<R, ?>> {
    public <T extends R> RegistryEntry<R, T> register(String id, Supplier<T> supplier) {
        CCSensory.LOGGER.info("Adding entry with id {} to register", id);
        RegistryEntry<R, T> entry = new RegistryEntry<R, T>(id, supplier);
        this.put(id, entry);
        return entry;
    }
}
