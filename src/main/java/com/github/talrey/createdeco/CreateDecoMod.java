package com.github.talrey.createdeco;

import com.github.talrey.createdeco.registrate.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDecoMod implements ModInitializer {
  public static final String MOD_ID = "createdeco";
  public static final String NAME = "Create Deco";
  public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

  public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

  public static void init() {
    ItemRegistry.init();
    BlockRegistry.init();
  }

  @Override
  public void onInitialize() {
    CreateDecoPlugin.verifyEarlyRegistrationComplete();
    REGISTRATE.runAfterRegister();
    CreativeTabs.register();
    LOGGER.info("Registered {} blocks, {} items and {} block entity types",
      REGISTRATE.registered(Registries.BLOCK).size(),
      REGISTRATE.registered(Registries.ITEM).size(),
      REGISTRATE.registered(Registries.BLOCK_ENTITY_TYPE).size());
    if (Boolean.getBoolean("createdeco.dumpRegistry")) {
      for (Identifier id : REGISTRATE.registered(Registries.BLOCK))
        LOGGER.info("createdeco-block {}", id);
      for (Identifier id : REGISTRATE.registered(Registries.ITEM))
        LOGGER.info("createdeco-item {}", id);
    }
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
