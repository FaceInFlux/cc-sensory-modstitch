//? if neoforge {
/*package io.github.faceinflux.ccsensory.loaders.neoforge.registries;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import io.github.faceinflux.ccsensory.util.cc.PeripheralHoldingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class PeripheralRegistryHandler {
    public static void registerPeripherals(IEventBus modEventBus) {
        modEventBus.addListener(PeripheralRegistryHandler::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerPeripheral(event, ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get());
    }

    @SuppressWarnings("unchecked")
    private static <P extends IPeripheral, BE extends PeripheralHoldingBlockEntity<P>> void registerPeripheral(
            RegisterCapabilitiesEvent event, BlockEntityType<?> blockEntityType
    ) {
        event.registerBlockEntity(
                PeripheralCapability.get(),
                (BlockEntityType<BE>) blockEntityType,
                (b, d) -> b.getPeripheral()
        );
    }
}
*///?}