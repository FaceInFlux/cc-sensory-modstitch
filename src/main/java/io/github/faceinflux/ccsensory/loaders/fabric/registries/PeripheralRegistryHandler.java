//? if fabric {
package io.github.faceinflux.ccsensory.loaders.fabric.registries;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import io.github.faceinflux.ccsensory.util.cc.PeripheralHoldingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class PeripheralRegistryHandler {
    public static void registerPeripherals() {
        registerPeripheral(ModBlockEntityTypes.LIDAR_SENSOR_BLOCK_ENTITY.get());
    }

    @SuppressWarnings("unchecked")
    public static  <P extends IPeripheral, T extends PeripheralHoldingBlockEntity<P>> void registerPeripheral(
            BlockEntityType<?> type
    ) {
        PeripheralLookup.get().registerForBlockEntity(
                (f, s) -> f.getPeripheral(),
                (BlockEntityType<T>) type
        );
    }
}
//?}