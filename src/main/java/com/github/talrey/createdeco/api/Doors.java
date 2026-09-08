package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.Locale;

public class Doors {
  public static final BlockSetType OPEN_METAL_DOOR = new BlockSetType(
    "metal", true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.METAL,
    SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
    SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
    SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
    SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
  );


  public static BlockBuilder<DoorBlock,?> build (
    CreateRegistrate reg, String metal, boolean locked
  ) {
    String regName = (locked ? "locked_" : "")
      + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")
      + "_door";

    return reg.block(regName, p -> new DoorBlock(locked ? BlockSetType.GOLD : OPEN_METAL_DOOR, p))
      .initialProperties(()-> Blocks.IRON_DOOR)
      .properties(props -> props.noOcclusion().strength(5, 5).requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      
      
      
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.DOORS)
      .item()
      
      .properties(props -> (metal.contains("Netherite") ? props.fireResistant() : props))
      .build();
  }


  public static BlockBuilder<TrapDoorBlock,?> buildTrapdoor (
    CreateRegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_")
      + "_trapdoor";
    String path = "block/palettes/doors/" + regName ;
    Identifier texture = CreateDecoMod.id(path);

    return reg.block(regName, p->new TrapDoorBlock(OPEN_METAL_DOOR, p))
      .properties(props -> props.noOcclusion().strength(5, 5)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      
      .lang(metal + " Trapdoor")
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(BlockTags.TRAPDOORS)
      
      .item()
      
      .build();
  }

}
