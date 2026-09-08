package com.github.talrey.createdeco.client.connected;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.ItemRegistry;
import com.zurrtum.create.catnip.data.Couple;
import com.zurrtum.create.client.foundation.block.connected.AllCTTypes;
import com.zurrtum.create.client.foundation.block.connected.CTSpriteShiftEntry;
import com.zurrtum.create.client.foundation.block.connected.CTSpriteShifter;
import com.zurrtum.create.client.foundation.block.connected.CTType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Locale;

public class SpriteShifts {
  public static final HashMap<String, CTSpriteShiftEntry> METAL_WINDOWS     = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> SHEET_METAL_SIDES = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> CATWALK_TOPS      = new HashMap<>();

  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_TOP = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_FRONT = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_SIDE = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_BOTTOM = new HashMap<>();

  private static boolean populated;

  private static Couple<CTSpriteShiftEntry> vault (DyeColor color, String face) {
    final String prefixed = "block/palettes/shipping_containers/" + color + "/vault_" + face;
    return Couple.createWithContext(medium -> CTSpriteShifter.getCT(
      AllCTTypes.RECTANGLE, CreateDecoMod.id(prefixed + "_small"),
      CreateDecoMod.id(medium ? prefixed + "_medium" : prefixed + "_large")
    ));
  }

  public static void populateMaps () {
    if (populated) return;
    populated = true;
    CreateDecoMod.LOGGER.info("Populating connected texture maps...");
    for (DyeColor color : DyeColor.values()) {
      VAULT_TOP   .put(color, vault(color, "top"));
      VAULT_BOTTOM.put(color, vault(color, "bottom"));
      VAULT_FRONT .put(color, vault(color, "front"));
      VAULT_SIDE  .put(color, vault(color, "side"));
    }

    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String path = "block/palettes/sheet_metal/" + regName(metal) + "_sheet_metal";
      SHEET_METAL_SIDES.put(metal, make(AllCTTypes.VERTICAL, path));

      path = "block/palettes/catwalks/" + regName(metal) + "_catwalk";
      CATWALK_TOPS.put(metal, make(AllCTTypes.OMNIDIRECTIONAL, path));

      path = "block/palettes/windows/" + regName(metal) + "_window";
      METAL_WINDOWS.put(metal, make(AllCTTypes.VERTICAL, path));
    }
  }

  private static String regName (String metal) {
    return metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
  }

  private static CTSpriteShiftEntry make (CTType type, String path) {
    Identifier blockTexture     = CreateDecoMod.id(path);
    Identifier connectedTexture = CreateDecoMod.id(path + "_connected");
    return CTSpriteShifter.getCT(type, blockTexture, connectedTexture);
  }
}
