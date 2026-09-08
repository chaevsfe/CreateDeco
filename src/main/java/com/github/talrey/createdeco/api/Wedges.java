package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.blocks.SupportWedgeBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Wedges {
  public static BlockBuilder<SupportWedgeBlock,?> build (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_support_wedge";

    return reg.block(regName, SupportWedgeBlock::new)
      .properties(props -> props.strength(5, 6)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
        .noOcclusion()
        .isViewBlocking((a, b, c) -> false)
        .isSuffocating((a, b, c) -> false)
      )
      
      .item()
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      
      .lang(metal + " Support Wedge");
  }

}
