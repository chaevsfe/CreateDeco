package com.github.talrey.createdeco.registrate.builders;

import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.entry.ItemEntry;
import com.github.talrey.createdeco.registrate.fn.NonNullFunction;
import com.github.talrey.createdeco.registrate.fn.NonNullSupplier;
import com.github.talrey.createdeco.registrate.fn.NonNullUnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemBuilder<T extends Item, P> extends AbstractBuilder<T, P, ItemBuilder<T, P>> {
    private final NonNullFunction<Item.Properties, T> factory;
    private final NonNullSupplier<Block> owningBlock;
    private final List<NonNullUnaryOperator<Item.Properties>> propertyOperators = new ArrayList<>();
    private final List<TagKey<Item>> tags = new ArrayList<>();

    public ItemBuilder(CreateRegistrate owner, P parent, String name, NonNullFunction<Item.Properties, T> factory) {
        this(owner, parent, name, factory, null);
    }

    public ItemBuilder(CreateRegistrate owner, P parent, String name, NonNullFunction<Item.Properties, T> factory, NonNullSupplier<Block> owningBlock) {
        super(owner, name, parent);
        this.factory = factory;
        this.owningBlock = owningBlock;
    }

    public ItemBuilder<T, P> properties(NonNullUnaryOperator<Item.Properties> operator) {
        propertyOperators.add(operator);
        return this;
    }

    @SafeVarargs
    public final ItemBuilder<T, P> tag(TagKey<Item>... values) {
        Collections.addAll(tags, values);
        return this;
    }

    public List<TagKey<Item>> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public ItemEntry<T> register() {
        Identifier id = getId();
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item.Properties properties = new Item.Properties();
        if (owningBlock != null)
            properties = properties.useBlockDescriptionPrefix();
        for (NonNullUnaryOperator<Item.Properties> operator : propertyOperators)
            properties = operator.apply(properties);
        T item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        if (owningBlock != null)
            Item.BY_BLOCK.put(owningBlock.get(), item);
        ItemEntry<T> entry = new ItemEntry<>(id, item);
        getOwner().trackRegistered(Registries.ITEM, id);
        getOwner().trackForCreativeTab(entry);
        runRegisterCallbacks(item);
        queueAfterRegisterCallbacks(item);
        return entry;
    }
}
