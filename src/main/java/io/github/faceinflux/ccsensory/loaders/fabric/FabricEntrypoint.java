//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric;

import io.github.faceinflux.ccsensory.CCSensory;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class FabricEntrypoint implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Hello from FabricEntrypoint!");
        CCSensory.initialize();
    }
}
//?}
