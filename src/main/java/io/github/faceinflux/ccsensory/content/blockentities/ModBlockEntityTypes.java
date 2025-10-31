package io.github.faceinflux.ccsensory.content.blockentities;

import io.github.faceinflux.ccsensory.content.blocks.ModBlocks;
import io.github.faceinflux.ccsensory.util.RegistryEntry;
import io.github.faceinflux.ccsensory.util.blockentities.BlockEntityFactory;
import io.github.faceinflux.ccsensory.util.blockentities.BlockEntityTypeRegistryEntry;
import io.github.faceinflux.ccsensory.util.Register;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final Register<BlockEntityType<?>> register = new Register<>();

    public static ResourceKey<BlockEntityType<?>> blockEntityKey(String name) {
        return Register.makeResourceKey(Registries.BLOCK_ENTITY_TYPE, name);
    }

    public static <B extends Block> BlockEntityTypeRegistryEntry registerBlockEntity(
            ResourceKey<BlockEntityType<?>> key,
            BlockEntityFactory<?> entity,
            RegistryEntry<Block, B>... blocks
    ) {
        return (BlockEntityTypeRegistryEntry) register.register(
                key.location().getPath(),
                (id) -> new BlockEntityTypeRegistryEntry(
                    id,
                    entity,
                    blocks
                )
        );
    }

    public static final BlockEntityTypeRegistryEntry LIDAR_SENSOR_BLOCK_ENTITY
            = registerBlockEntity(
                blockEntityKey("lidar_sensor"),
                LidarSensorBlockEntity::new,
                ModBlocks.LIDAR_SENSOR
    );
}
