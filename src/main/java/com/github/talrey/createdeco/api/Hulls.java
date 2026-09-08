package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.blocks.HullBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Hulls {
  public static BlockBuilder<HullBlock,?> build (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_hull";

    return reg.block(regName, HullBlock::new)
        .properties(props-> props.strength(5, 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
            .noOcclusion()
            .isViewBlocking((a,b,c)->false)
        )
        
        .item()
        .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
        .build()
        .tag(BlockTags.STAIRS)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        
        .lang(metal + " Train Hull");
  }


}
