package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.registrate.entry.BlockEntry;
import com.github.talrey.createdeco.registrate.fn.NonNullFunction;
import com.zurrtum.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.zurrtum.create.content.decoration.palettes.GlassPaneBlock;
import com.zurrtum.create.content.decoration.palettes.WindowBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.function.Supplier;

public class Windows {
  private static BlockBehaviour.Properties glassProperties(BlockBehaviour.Properties p) {
    return p.isValidSpawn(Windows::never)
        .isRedstoneConductor(Windows::never)
        .isSuffocating(Windows::never)
        .isViewBlocking(Windows::never);
  }

  private static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
    return false;
  }

  private static Boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> type) {
    return false;
  }

  public static BlockEntry<WindowBlock> metalWindowBlock(String metal) {
    return metalWindowBlock(metal, false);
  }

  public static BlockEntry<WindowBlock> metalWindowBlock(String metal, boolean translucent) {
    String name = metal + "_window";
    return windowBlock(name, translucent, Blocks.GLASS::defaultMapColor);
  }

  public static BlockEntry<WindowBlock> windowBlock(String name, boolean translucent, Supplier<MapColor> color) {
    return CreateDecoMod.REGISTRATE.block(name.toLowerCase(Locale.ROOT).replace(" ", "_"), p -> new WindowBlock(p, translucent))
      .initialProperties(() -> Blocks.GLASS)
      .properties(Windows::glassProperties)
      .properties(p -> p.mapColor(color.get()))
      .tag(BlockTags.IMPERMEABLE)
      .tag(CreateDecoTags.GLASS_BLOCKS)
      .simpleItem()
      .register();
  }

  public static BlockEntry<ConnectedGlassPaneBlock> metalWindowPane(String metal, Supplier<? extends Block> parent) {
    String name = metal.toLowerCase(Locale.ROOT).replace(" ", "_") + "_window";
    return glassPane(name, parent, ConnectedGlassPaneBlock::new);
  }

  private static <G extends GlassPaneBlock> BlockEntry<G> glassPane(String name, Supplier<? extends Block> parent,
                                                                   NonNullFunction<BlockBehaviour.Properties, G> factory) {
    name += "_pane";

    return CreateDecoMod.REGISTRATE.block(name, factory)
      .initialProperties(() -> Blocks.GLASS_PANE)
      .properties(p -> p.mapColor(parent.get()
        .defaultMapColor()))
      .tag(CreateDecoTags.GLASS_PANES)
      .item()
      .build()
      .register();
  }
}
