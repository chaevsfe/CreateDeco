package com.github.talrey.createdeco.api;

import com.zurrtum.create.content.decoration.MetalLadderBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Ladders {
  public static BlockBuilder<MetalLadderBlock,?> build (
      CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");


    return reg.block(regName + "_ladder", MetalLadderBlock::new)
        .initialProperties(() -> Blocks.LADDER)
        
        
        .properties(p -> p.sound(SoundType.COPPER))
        .tag(BlockTags.CLIMBABLE)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .lang(metal + " Ladder")
        .item()
        
        //.model((c, p) -> p.blockSprite(c::get, p.modLoc("block/ladder_" + regName)))
        .build();
  }


}
