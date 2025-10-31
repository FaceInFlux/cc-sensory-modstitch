//? if neoforge {
/*package io.github.faceinflux.ccsensory.loaders.neoforge.registries;

import io.github.faceinflux.ccsensory.CCSensory;
import io.github.faceinflux.ccsensory.content.blockentities.ModBlockEntityTypes;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.blockentities.BlockEntityTypeRegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

public class BlockEntityRegistryHandler {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES
            = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CCSensory.ID);

    public static <T extends BlockEntity> void registerBlockEntities(IEventBus eventBus) {
        CCSensory.LOGGER.info("Registering BlockEntities");
        BLOCK_ENTITY_TYPES.register(eventBus);
        for (RegistryEntry<BlockEntityType<?>, ?> entry : ModBlockEntityTypes.register.values()) {
            CCSensory.LOGGER.info("Registering BlockEntityType {}", entry.id);
            // This is really cursed but I'm struggling with generics ;-;
            BlockEntityTypeRegistryEntry castedEntry = (BlockEntityTypeRegistryEntry) entry;
            castedEntry.returnSupplier = BLOCK_ENTITY_TYPES.register(
                    entry.id,
                    //? if >=1.21.7 {
                    /^() -> new BlockEntityType<T>(
                            (pos, state) ->
                                    (T) castedEntry.factory.create(pos, state),
                            false,
                            Arrays.stream(castedEntry.blocks)
                                    .map(RegistryEntry::get)
                                    .toArray(Block[]::new)
                    )
                    ^///?} else {
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> (T) castedEntry.factory.create(pos, state),
                            Arrays.stream(castedEntry.blocks)
                                    .map(RegistryEntry::get)
                                    .toArray(Block[]::new)
                    ).build(null)
                    //?}
            );

            ModBlockEntityTypes.register.replace(entry.id, castedEntry);
        }
    }
}
*///?}