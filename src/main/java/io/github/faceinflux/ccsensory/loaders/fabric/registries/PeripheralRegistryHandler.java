//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.shared.peripheral.redstone.RedstoneRelayBlockEntity;
import io.github.faceinflux.ccsensory.content.blockentities.LidarSensorBlockEntity;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import io.github.faceinflux.ccsensory.content.peripherals.LidarSensorPeripheral;

public final class PeripheralRegistryHandler {
    public static void registerPeripherals() {
        PeripheralLookup.get().registerForBlockEntity(
                (f, s) -> ((LidarSensorBlockEntity) f).peripheral(),
                ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get()
        );
    }
}
//? }