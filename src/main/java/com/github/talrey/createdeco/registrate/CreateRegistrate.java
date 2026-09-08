package com.github.talrey.createdeco.registrate;

import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import com.github.talrey.createdeco.registrate.builders.BlockEntityBuilder;
import com.github.talrey.createdeco.registrate.builders.ItemBuilder;
import com.github.talrey.createdeco.registrate.entry.ItemProviderEntry;
import com.github.talrey.createdeco.registrate.fn.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateRegistrate {
    private final String modid;
    private final Map<String, List<ItemProviderEntry<?, ?>>> tabContents = new LinkedHashMap<>();
    private final List<Runnable> afterRegister = new ArrayList<>();
    private final Map<ResourceKey<? extends Registry<?>>, List<Identifier>> registered = new LinkedHashMap<>();
    private String defaultCreativeTab;

    protected CreateRegistrate(String modid) {
        this.modid = modid;
    }

    public static CreateRegistrate create(String modid) {
        return new CreateRegistrate(modid);
    }

    public String getModid() {
        return modid;
    }

    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modid, path);
    }

    public CreateRegistrate defaultCreativeTab(String tab) {
        this.defaultCreativeTab = tab;
        return this;
    }

    public CreateRegistrate addLang(String prefix, Identifier id, String translation) {
        return this;
    }

    public <T extends Item> ItemBuilder<T, CreateRegistrate> item(String name, NonNullFunction<Item.Properties, T> factory) {
        return new ItemBuilder<>(this, this, name, factory);
    }

    public <T extends Block> BlockBuilder<T, CreateRegistrate> block(String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<>(this, this, name, factory);
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, CreateRegistrate> blockEntity(String name, BlockEntityBuilder.Factory<T> factory) {
        return new BlockEntityBuilder<>(this, this, name, factory);
    }

    public void trackForCreativeTab(ItemProviderEntry<?, ?> entry) {
        if (defaultCreativeTab == null)
            return;
        tabContents.computeIfAbsent(defaultCreativeTab, key -> new ArrayList<>()).add(entry);
    }

    public List<ItemProviderEntry<?, ?>> creativeTabContents(String tab) {
        return tabContents.getOrDefault(tab, List.of());
    }

    public void trackRegistered(ResourceKey<? extends Registry<?>> registry, Identifier id) {
        registered.computeIfAbsent(registry, key -> new ArrayList<>()).add(id);
    }

    public List<Identifier> registered(ResourceKey<? extends Registry<?>> registry) {
        return registered.getOrDefault(registry, List.of());
    }

    public void queueAfterRegister(Runnable callback) {
        afterRegister.add(callback);
    }

    public void runAfterRegister() {
        for (Runnable callback : afterRegister)
            callback.run();
        afterRegister.clear();
    }

    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }
}
