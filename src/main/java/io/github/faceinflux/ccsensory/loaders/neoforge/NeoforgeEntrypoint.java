//? if neoforge {
/*package io.github.faceinflux.ccsensory.loaders.neoforge;

import io.github.faceinflux.ccsensory.CCSensory;
import com.mojang.logging.LogUtils;
import io.github.faceinflux.ccsensory.loaders.neoforge.registries.ItemRegistryHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod("ccsensory")
public class NeoforgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    public NeoforgeEntrypoint(IEventBus eventBus) {
        LOGGER.info("Hello from NeoforgeEntrypoint!");
        CCSensory.initialize();

        ItemRegistryHandler.registerItems(eventBus);
    }
}
*///?}
