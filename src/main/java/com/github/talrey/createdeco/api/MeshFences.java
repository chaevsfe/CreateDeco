package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.blocks.MeshFenceBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class MeshFences {

  public static BlockBuilder<MeshFenceBlock,?> build (CreateRegistrate reg, String metal) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_mesh_fence", MeshFenceBlock::new)
      .properties(props-> props.strength(5, 6).requiresCorrectToolForDrops()
        .sound(SoundType.CHAIN)
      )
      
      .tag(BlockTags.FENCES)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      
      .build()
      ;
  }
}
