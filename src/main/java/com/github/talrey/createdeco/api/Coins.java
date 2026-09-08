package com.github.talrey.createdeco.api;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.github.talrey.createdeco.registrate.CreateRegistrate;
import com.github.talrey.createdeco.registrate.builders.BlockBuilder;
import com.github.talrey.createdeco.registrate.builders.ItemBuilder;
import com.github.talrey.createdeco.registrate.fn.NonNullSupplier;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Coins {
  public static ItemBuilder<Item,?> buildCoinItem (
    CreateRegistrate reg, NonNullSupplier<Item> coinstack, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coin";
    return reg.item(regName, Item::new)
      .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
      
      .lang(metal + " Coin");
  }

  public static ItemBuilder<CoinStackItem,?> buildCoinStackItem (
    CreateRegistrate reg, NonNullSupplier<Item> coin, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coinstack";
    return reg.item(regName, (p)-> new CoinStackItem(p, metal))
      .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
      
      .lang(metal + " Coin Stack");
  }

  public static BlockBuilder<CoinStackBlock,?> buildCoinStackBlock (
    CreateRegistrate reg, NonNullSupplier<Item> material, String metal,
    Identifier side, Identifier bottom, Identifier top
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coinstack";
    return reg.block(regName, (p)->new CoinStackBlock(p, metal))
      .properties(props -> props.noOcclusion().strength(0.5f).sound(SoundType.CHAIN))
      
      
      .lang(metal + "Coin Stack Block")
      ;
  }
}
