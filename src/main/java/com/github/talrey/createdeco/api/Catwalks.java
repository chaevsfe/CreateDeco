package com.github.talrey.createdeco.api;

import com.zurrtum.create.AllBlockTags;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkRailingBlock;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
import com.github.talrey.createdeco.items.CatwalkBlockItem;
import com.github.talrey.createdeco.items.CatwalkStairBlockItem;
import com.github.talrey.createdeco.items.RailingBlockItem;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Catwalks {

  public static BlockBuilder<CatwalkBlock,?> build (
    CreateRegistrate reg, String metal
  ) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk", CatwalkBlock::new)
      .properties(props->
        props.strength(5, 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )

      
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllBlockTags.FAN_TRANSPARENT)
      .item(CatwalkBlockItem::new)
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      
      .build()
      
      ;
  }

  public static BlockBuilder<CatwalkStairBlock,?> buildStair (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT)
      .replaceAll(" ", "_") + "_catwalk_stairs",
      p -> new CatwalkStairBlock(p, metal)
    )
      .properties(props-> props
        .strength(5, 6)
        .requiresCorrectToolForDrops().noOcclusion()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.STAIRS)
      .tag(AllBlockTags.FAN_TRANSPARENT)
      
      .item(CatwalkStairBlockItem::new).build();
  }

  public static BlockBuilder<CatwalkRailingBlock,?> buildRailing (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk_railing", CatwalkRailingBlock::new)
      .properties(props->
        props.strength(5, 6)
          .requiresCorrectToolForDrops().noOcclusion().sound(SoundType.NETHERITE_BLOCK)
      )
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllBlockTags.FAN_TRANSPARENT)
      
      
      
      .item(RailingBlockItem::new)
      .build();
  }





}
