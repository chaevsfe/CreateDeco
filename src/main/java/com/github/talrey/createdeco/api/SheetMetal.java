package com.github.talrey.createdeco.api;

import com.zurrtum.create.content.decoration.palettes.ConnectedPillarBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class SheetMetal {
  public static BlockBuilder<ConnectedPillarBlock,?> buildBlock (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";

    return reg.block(regName, ConnectedPillarBlock::new)
        .properties(props-> props.strength(5, 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
        .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        
        .lang(metal + " Sheet Metal")

        ;
  }


/*
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
          .blockstate((ctx, prov) -> BlockStateGenerator.brickStair(ctx, prov, color))
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0, 1).toUpperCase()
                  + color.substring(1)
                  + " " + "Brick Stairs"
          )
          
          .recipe((ctx, prov) -> {
            prov.stairs(
                DataIngredient.items(
                    (ItemLike) BlockRegistry.BRICKS.get(BlockRegistry.fromName(color)).get(
                        (prefix.isEmpty() ? "" : prefix + "_") + color + "_bricks"
                    )),
                RecipeCategory.BUILDING_BLOCKS,
                ctx,
                CreateDecoMod.MOD_ID,
                true
            );
            recipeStonecuttingStair(finalName, color, prefix, ctx, prov);
          })
          .simpleItem()
      );
    }
    return ret;
  }

 */
}
