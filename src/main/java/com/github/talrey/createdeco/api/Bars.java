package com.github.talrey.createdeco.api;

import com.zurrtum.create.AllBlockTags;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Bars {
  public static BlockBuilder<IronBarsBlock, ?> build (
          CreateRegistrate reg, String metal, String suffix, boolean doPost
  ) {
    String base = metal.replace(' ', '_').toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_bars";
    String suf = suffix.equals("") ? "" : "_" + suffix.replace(' ', '_').toLowerCase(Locale.ROOT);
    String post = "block/palettes/metal_bars/" + base + (doPost ? "_post" : "");

    Identifier barTexture, postTexture;
    final Identifier bartex, postex;
    //try {
    if (metal.equals("Iron")) {
      barTexture = Identifier.fromNamespaceAndPath("minecraft", "block/iron_bars");
      postTexture = barTexture;
    }
    else {
      barTexture = Identifier.fromNamespaceAndPath(reg.getModid(), "block/palettes/metal_bars/" + base);
      postTexture = Identifier.fromNamespaceAndPath(reg.getModid(), post);
    }

    // for lambda stuff, must be final
    bartex = barTexture;
    postex = postTexture;

    var block = reg.block(base + suf, IronBarsBlock::new)
      .properties(props -> props.noOcclusion().strength(5, 6)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK))
      
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(CreateDecoTags.BARS)
      .item()
        
        .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
        .build();

    if (!suffix.equals("overlay")) {
      block = block.tag(AllBlockTags.FAN_TRANSPARENT);
    }

    return block;
  }



}
