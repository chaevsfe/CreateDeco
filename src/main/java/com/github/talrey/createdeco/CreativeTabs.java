package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registrate.entry.ItemProviderEntry;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class CreativeTabs {
  public static final String PROPS_KEY = "props_tab";
  public static final String BRICKS_KEY = "bricks_tab";

  public static final ResourceKey<CreativeModeTab> PROPS = key(PROPS_KEY);
  public static final ResourceKey<CreativeModeTab> BRICKS = key(BRICKS_KEY);

  private static ResourceKey<CreativeModeTab> key (String name) {
    return ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateDecoMod.id(name));
  }

  public static void register () {
    register(PROPS, PROPS_KEY, () -> BlockRegistry.GREEN_CAGE_LAMPS.get("Brass").asStack());
    register(BRICKS, BRICKS_KEY, () -> BlockRegistry.BRICKS.get(DyeColor.LIGHT_BLUE).get("blue_bricks").asStack());
  }

  private static void register (ResourceKey<CreativeModeTab> key, String name, Supplier<ItemStack> icon) {
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, FabricCreativeModeTab.builder()
      .title(Component.translatableWithFallback(
        "itemGroup." + CreateDecoMod.MOD_ID + "." + name, "Create Deco"
      ))
      .icon(icon)
      .displayItems((parameters, output) -> {
        List<ItemProviderEntry<?, ?>> contents = CreateDecoMod.REGISTRATE.creativeTabContents(name);
        for (ItemProviderEntry<?, ?> entry : contents)
          output.accept(entry.asStack());
      })
      .build());
  }
}
