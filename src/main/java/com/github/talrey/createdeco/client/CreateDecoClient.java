package com.github.talrey.createdeco.client;

import com.github.talrey.createdeco.client.connected.SpriteShifts;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CreateDecoClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    SpriteShifts.populateMaps();
    CreateDecoModels.register();
    CreateDecoBlockEntityRenders.register();
  }
}
