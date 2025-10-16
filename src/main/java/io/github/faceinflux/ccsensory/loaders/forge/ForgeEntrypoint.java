//? if forge {
/*package io.github.faceinflux.ccsensory.loaders.forge;

import io.github.faceinflux.ccsensory.CCSensory;
import com.mojang.logging.LogUtils;
import io.github.faceinflux.ccsensory.loaders.forge.registries.ItemRegistryHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("ccsensory")
public class ForgeEntrypoint {
    private static final Logger LOGGER = LogUtils.getLogger();
    public IEventBus eventBus;

    public ForgeEntrypoint() {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info("Hello from ForgeEntrypoint!");
        CCSensory.initialize();

        ItemRegistryHandler.registerItems(eventBus);
    }
}
*///?}
