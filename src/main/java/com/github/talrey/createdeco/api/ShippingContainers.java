package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.items.ShippingContainerBlockItem;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.SharedProperties;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import com.zurrtum.create.AllMountedStorageTypes;
import com.zurrtum.create.api.contraption.storage.item.MountedItemStorageType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;

public class ShippingContainers {
  public static BlockBuilder<ShippingContainerBlock,?> build (
    CreateRegistrate reg, DyeColor color
  ) {
    String regName = color.getName() + "_shipping_container";
    String visName = capitalizeFully(color.getName());

    return reg.block(regName, p -> new ShippingContainerBlock(p, color))
      .initialProperties(SharedProperties::softMetal)
      .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
      .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).explosionResistance(1200))
      .item(ShippingContainerBlockItem::new)
        .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(visName + " Shipping Container")
      .onRegister(block -> MountedItemStorageType.REGISTRY.register(block, AllMountedStorageTypes.VAULT));
  }

  private static String capitalizeFully (String name) {
    StringBuilder out = new StringBuilder();
    for (String word : name.split("_")) {
      if (word.isEmpty()) continue;
      if (!out.isEmpty()) out.append(' ');
      out.append(Character.toUpperCase(word.charAt(0)));
      out.append(word.substring(1).toLowerCase(Locale.ROOT));
    }
    return out.toString();
  }
}
