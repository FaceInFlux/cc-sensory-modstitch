//? if forge {
/*package io.github.faceinflux.ccsensory.loaders.forge.registries;

import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.util.cc.PeripheralHoldingBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class PeripheralRegistryHandler {
    public static void registerPeripherals() {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, PeripheralRegistryHandler::attachPeripheral);
    }

    private static void attachPeripheral(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof PeripheralHoldingBlockEntity<?> blockEntity) {
            PeripheralProvider.attach(event, blockEntity, PeripheralHoldingBlockEntity::getPeripheral);
        }
    }

    // Forge be forging (Boilerplate for adding a new capability provider)
    public static final Capability<IPeripheral> CAPABILITY_PERIPHERAL = CapabilityManager.get(new CapabilityToken<>() {
    });
    private static final ResourceLocation PERIPHERAL = new ResourceLocation(CCSensory.ID, "peripheral");

    // A {@link ICapabilityProvider} that lazily creates an {@link IPeripheral} when required.
    private static final class PeripheralProvider<O extends BlockEntity> implements ICapabilityProvider {
        private final O blockEntity;
        private final Function<O, IPeripheral> factory;
        private @Nullable LazyOptional<IPeripheral> peripheral;

        private PeripheralProvider(O blockEntity, Function<O, IPeripheral> factory) {
            this.blockEntity = blockEntity;
            this.factory = factory;
        }

        private static <O extends BlockEntity> void attach(AttachCapabilitiesEvent<BlockEntity> event, O blockEntity, Function<O, IPeripheral> factory) {
            var provider = new PeripheralProvider<>(blockEntity, factory);
            event.addCapability(PERIPHERAL, provider);
            event.addListener(provider::invalidate);
        }

        private void invalidate() {
            if (peripheral != null) peripheral.invalidate();
            peripheral = null;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction direction) {
            if (capability != CAPABILITY_PERIPHERAL) return LazyOptional.empty();
            if (blockEntity.isRemoved()) return LazyOptional.empty();

            var peripheral = this.peripheral;
            return (peripheral == null ? (this.peripheral = LazyOptional.of(() -> factory.apply(blockEntity))) : peripheral).cast();
        }
    }
}
*///?}