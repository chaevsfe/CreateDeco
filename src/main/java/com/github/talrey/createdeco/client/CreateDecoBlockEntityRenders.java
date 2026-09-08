package com.github.talrey.createdeco.client;

import com.github.talrey.createdeco.BlockRegistry;
import com.zurrtum.create.client.AllBlockEntityRenders;
import com.zurrtum.create.client.content.decoration.placard.PlacardRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CreateDecoBlockEntityRenders {
  public static void register () {
    AllBlockEntityRenders.render(BlockRegistry.PLACARD_ENTITIES.get(), PlacardRenderer::new);
  }
}
