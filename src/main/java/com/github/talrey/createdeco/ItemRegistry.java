package com.github.talrey.createdeco;

import com.github.talrey.createdeco.api.Coins;
import com.github.talrey.createdeco.api.CreateDecoTags;
import com.github.talrey.createdeco.items.CoinStackItem;
import com.zurrtum.create.AllItems;
import com.github.talrey.createdeco.registrate.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.function.Function;

public class ItemRegistry {
  public static ItemEntry<Item> ANDESITE_SHEET;
  public static ItemEntry<Item> ZINC_SHEET;
  public static ItemEntry<Item> NETHERITE_SHEET;
  public static ItemEntry<Item> NETHERITE_NUGGET;
  public static ItemEntry<Item> INDUSTRIAL_IRON_NUGGET;
  public static ItemEntry<Item> INDUSTRIAL_IRON_INGOT;
  public static ItemEntry<Item> INDUSTRIAL_IRON_SHEET;

  public static HashMap<String, Function<String, Item>> METAL_TYPES = new HashMap<>();
  public static HashMap<String, Function<String, Item>> COIN_METALS = new HashMap<>();

  public static HashMap<String, ItemEntry<Item>> COINS = new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACKS = new HashMap<>();

  public static void init () {
    CreateDecoMod.LOGGER.info("Registering items for " + CreateDecoMod.NAME);
    CreateDecoMod.REGISTRATE.defaultCreativeTab(CreativeTabs.PROPS_KEY);
    CreateDecoMod.REGISTRATE.addLang("itemGroup", CreateDecoMod.id(CreativeTabs.PROPS_KEY), "Create Deco Props");
    CreateDecoMod.REGISTRATE.addLang("itemGroup", CreateDecoMod.id(CreativeTabs.BRICKS_KEY), "Create Deco Bricks");

    METAL_TYPES.put("Andesite", (str) -> AllItems.ANDESITE_ALLOY);
    METAL_TYPES.put("Zinc", (str) -> AllItems.ZINC_INGOT);
    METAL_TYPES.put("Copper", (str) -> Items.COPPER_INGOT);
    METAL_TYPES.put("Brass", (str) -> AllItems.BRASS_INGOT);
    METAL_TYPES.put("Iron", (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Industrial Iron", (str) -> INDUSTRIAL_IRON_INGOT.get());

    COIN_METALS.put("Zinc", (str) -> AllItems.ZINC_INGOT);
    COIN_METALS.put("Copper", (str) -> Items.COPPER_INGOT);
    COIN_METALS.put("Brass", (str) -> AllItems.BRASS_INGOT);
    COIN_METALS.put("Iron", (str) -> Items.IRON_INGOT);
    COIN_METALS.put("Industrial Iron", (str) -> INDUSTRIAL_IRON_INGOT.get());
    COIN_METALS.put("Gold", (str) -> Items.GOLD_INGOT);
    COIN_METALS.put("Netherite", (str) -> Items.NETHERITE_INGOT);

    CreateDecoTags.init();
    registerSheets();
    registerNuggets();
    registerIngots();

    COIN_METALS.forEach(ItemRegistry::registerCoins);
  }

  private static void registerSheets () {
    ANDESITE_SHEET = CreateDecoMod.REGISTRATE.item("andesite_sheet", Item::new)
        .tag(CreateDecoTags.plate("Andesite"))
        .tag(CreateDecoTags.plate())
        .lang("Andesite Alloy Sheet")
        .register();

    ZINC_SHEET = CreateDecoMod.REGISTRATE.item("zinc_sheet", Item::new)
      .tag(CreateDecoTags.plate("Zinc"))
      .tag(CreateDecoTags.plate())
      .lang("Zinc Sheet")
      .register();

//    NETHERITE_SHEET = CreateDecoMod.REGISTRATE.item("netherite_sheet", Item::new)
//      .properties(Item.Properties::fireResistant)
//      .tag(CreateDecoTags.plate("Netherite"))
//      .lang("Netherite Sheet")
//      .register();

    INDUSTRIAL_IRON_SHEET = CreateDecoMod.REGISTRATE.item("industrial_iron_sheet", Item::new)
      .tag(CreateDecoTags.plate("Industrial Iron"))
      .tag(CreateDecoTags.plate())
      .lang("Industrial Iron Sheet")
      .register();
  }

  private static void registerNuggets () {
    NETHERITE_NUGGET = CreateDecoMod.REGISTRATE.item("netherite_nugget", Item::new)
      .properties(Item.Properties::fireResistant)
      .tag(CreateDecoTags.nugget("Netherite"))
      .tag(CreateDecoTags.nugget())
      .lang("Netherite Nugget")
      
      .register();

    INDUSTRIAL_IRON_NUGGET = CreateDecoMod.REGISTRATE.item("industrial_iron_nugget", Item::new)
      .tag(CreateDecoTags.nugget("Industrial Iron"))
      .tag(CreateDecoTags.nugget())
      .lang("Industrial Iron Nugget")
      
      .register();
  }

  private static void registerIngots () {
    INDUSTRIAL_IRON_INGOT = CreateDecoMod.REGISTRATE.item("industrial_iron_ingot", Item::new)
      .tag(CreateDecoTags.ingot("Industrial Iron"))
      .tag(CreateDecoTags.ingot())
      .lang("Industrial Iron Ingot")
      
      .register();
  }

  private static void registerCoins (String metal, Function<String, Item> getter) {
    if (metal.equals("Andesite")) return;

    COINS.put(metal, Coins.buildCoinItem(CreateDecoMod.REGISTRATE,
      () -> COINSTACKS.get(metal).get(), metal)
      .register()
    );
    COINSTACKS.put(metal, Coins.buildCoinStackItem(CreateDecoMod.REGISTRATE,
      () -> COINS.get(metal).get(), metal)
      .register()
    );
  }
}
