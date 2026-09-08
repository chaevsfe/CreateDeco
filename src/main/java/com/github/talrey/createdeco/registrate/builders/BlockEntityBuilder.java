package com.github.talrey.createdeco.registrate.builders;

import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.entry.BlockEntityEntry;
import com.github.talrey.createdeco.registrate.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity, P> extends AbstractBuilder<BlockEntityType<T>, P, BlockEntityBuilder<T, P>> {
    @FunctionalInterface
    public interface Factory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    private final Factory<T> factory;
    private final List<Supplier<? extends Block>> validBlocks = new ArrayList<>();

    public BlockEntityBuilder(CreateRegistrate owner, P parent, String name, Factory<T> factory) {
        super(owner, name, parent);
        this.factory = factory;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T, P> validBlocks(BlockEntry<? extends Block>... blocks) {
        for (BlockEntry<? extends Block> block : blocks)
            validBlocks.add(block);
        return this;
    }

    public BlockEntityEntry<T> register() {
        if (validBlocks.isEmpty())
            throw new IllegalStateException("Block entity " + getId() + " has no valid blocks");
        Identifier id = getId();
        ResourceKey<BlockEntityType<?>> key = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, id);
        Block[] blocks = new Block[validBlocks.size()];
        for (int i = 0; i < blocks.length; i++)
            blocks[i] = validBlocks.get(i).get();
        BlockEntityType<T> type = new BlockEntityType<>(factory::create, java.util.Set.of(blocks));
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, type);
        BlockEntityEntry<T> entry = new BlockEntityEntry<>(id, type);
        getOwner().trackRegistered(Registries.BLOCK_ENTITY_TYPE, id);
        runRegisterCallbacks(type);
        queueAfterRegisterCallbacks(type);
        return entry;
    }
}
