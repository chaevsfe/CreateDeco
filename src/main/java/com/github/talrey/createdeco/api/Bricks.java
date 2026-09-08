package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bricks {
  public static List<String> TYPES = Arrays.asList(
      "", "short", "tiled", "long", "corner", "cracked", "mossy"
  );
  private static List<String> CAPITALS = Arrays.asList(
      "", "Short ", "Tiled ", "Long ", "Corner ", "Cracked ", "Mossy "
  );

  public static ArrayList<BlockBuilder<Block,?>> buildBlock (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<Block,?>> ret = new ArrayList<>();
    for (String prefix : TYPES) {
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks";

      if (color.contains("red") && prefix.isEmpty()) continue;
      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, Block::new)

          .initialProperties(() -> Blocks.BRICKS)
          .properties(props -> props
              .strength(2, 6)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
          )
          
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0, 1).toUpperCase()
                  + color.substring(1)
                  + " " + "Bricks"
          )
          
          
          .simpleItem()
      );
    }
    return ret;
  }

  public static ArrayList<BlockBuilder<StairBlock,?>> buildStair (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<StairBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty() && prefix.isEmpty()) continue;
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_stairs";

      if (color.contains("red") && prefix.isEmpty()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, p -> new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), p))
        .initialProperties(() -> Blocks.BRICKS)
        .properties(props -> props
          .strength(2, 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.STONE)
        )
        
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(BlockTags.STAIRS)
        .lang(
          CAPITALS.get(TYPES.indexOf(prefix))
            + color.substring(0, 1).toUpperCase()
            + color.substring(1)
            + " " + "Brick Stairs"
        )
        
        
        .item().build()
      );
    }
    return ret;
  }

  public static ArrayList<BlockBuilder<SlabBlock,?>> buildSlab (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<SlabBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty() && prefix.isEmpty()) continue;
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_slab";

      if (color.contains("red") && prefix.isEmpty()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, SlabBlock::new)
        .initialProperties(() -> Blocks.BRICKS)
        .properties(props -> props
          .strength(2, 6)
          .requiresCorrectToolForDrops()
          .sound(SoundType.STONE)
        )
        
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .tag(BlockTags.SLABS)
        .lang(
          CAPITALS.get(TYPES.indexOf(prefix))
            + color.substring(0, 1).toUpperCase()
            + color.substring(1)
            + " " + "Brick Slab"
        )
        
        
        .simpleItem()
      );
    }
    return ret;
  }
  public static ArrayList<BlockBuilder<WallBlock,?>> buildWall (CreateRegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<WallBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty() && prefix.isEmpty()) continue;
      name = (prefix.isEmpty() ? "" : prefix + "_") + color + "_brick_wall";

      if (color.contains("red") && prefix.isEmpty()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, WallBlock::new)
        .initialProperties(() -> Blocks.BRICKS)
        .properties(props -> props
          .strength(2, 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
          )
          
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .tag(BlockTags.WALLS)
          .lang(
      CAPITALS.get(TYPES.indexOf(prefix))
            + color.substring(0, 1).toUpperCase()
            + color.substring(1)
            + " " + "Brick Wall"
          )
          
          
          .item().tag(ItemTags.WALLS).build()
      );
    }
    return ret;
  }










}
