package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.blocks.CageLampBlock;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

import java.util.Locale;

public class CageLamps {

  public static final Identifier YELLOW_ON  = CreateDecoMod.id( "block/palettes/cage_lamp/light_default");
  public static final Identifier YELLOW_OFF = CreateDecoMod.id( "block/palettes/cage_lamp/light_default_off");
  public static final Identifier RED_ON     = CreateDecoMod.id( "block/palettes/cage_lamp/light_redstone");
  public static final Identifier RED_OFF    = CreateDecoMod.id( "block/palettes/cage_lamp/light_redstone_off");
  public static final Identifier GREEN_ON   = CreateDecoMod.id( "block/palettes/cage_lamp/light_green");
  public static final Identifier GREEN_OFF  = CreateDecoMod.id( "block/palettes/cage_lamp/light_green_off");
  public static final Identifier BLUE_ON    = CreateDecoMod.id( "block/palettes/cage_lamp/light_soul");
  public static final Identifier BLUE_OFF   = CreateDecoMod.id( "block/palettes/cage_lamp/light_soul_off");




  public static BlockBuilder<CageLampBlock, ?> build (
    CreateRegistrate reg, String name, DyeColor color, Identifier cage, Identifier lampOn, Identifier lampOff
  ) {
    return reg.block(color.getName().toLowerCase(Locale.ROOT) + "_" + name.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_lamp",
        (p)-> new CageLampBlock(p, new Vector3f(0.3f, 0.3f, 0f)))
      .properties(props-> props.noOcclusion().strength(0.5f).sound(SoundType.LANTERN).lightLevel((state)-> state.getValue(BlockStateProperties.LIT)?15:0))
      
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .lang(color.name().charAt(0) + color.name().substring(1).toLowerCase() + " " + name + " Cage Lamp")
      .simpleItem();
  }
}
