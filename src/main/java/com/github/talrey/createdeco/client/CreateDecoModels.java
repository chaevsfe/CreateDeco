package com.github.talrey.createdeco.client;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.ItemRegistry;
import com.github.talrey.createdeco.client.connected.CatwalkCTBehaviour;
import com.github.talrey.createdeco.client.connected.ShippingContainerCTBehavior;
import com.github.talrey.createdeco.client.connected.SpriteShifts;
import com.zurrtum.create.client.AllModels;
import com.zurrtum.create.client.foundation.block.connected.ConnectedTextureBehaviour;
import com.zurrtum.create.client.foundation.block.connected.GlassPaneCTBehaviour;
import com.zurrtum.create.client.foundation.block.connected.HorizontalCTBehaviour;
import com.zurrtum.create.client.foundation.block.connected.RotatedPillarCTBehaviour;
import com.zurrtum.create.client.infrastructure.model.CTModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public class CreateDecoModels {
  public static void register () {
    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      connectedTextures(BlockRegistry.SHEET_METAL_PILLARS.get(metal).get(),
        new RotatedPillarCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal), null));
      connectedTextures(BlockRegistry.CATWALKS.get(metal).get(),
        new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)));
    }

    windows("Andesite", BlockRegistry.ANDESITE_WINDOW.get(), BlockRegistry.ANDESITE_WINDOW_PANE.get());
    windows("Copper", BlockRegistry.COPPER_WINDOW.get(), BlockRegistry.COPPER_WINDOW_PANE.get());
    windows("Iron", BlockRegistry.IRON_WINDOW.get(), BlockRegistry.IRON_WINDOW_PANE.get());
    windows("Industrial Iron", BlockRegistry.INDUSTRIAL_IRON_WINDOW.get(), BlockRegistry.INDUSTRIAL_IRON_WINDOW_PANE.get());
    windows("Brass", BlockRegistry.BRASS_WINDOW.get(), BlockRegistry.BRASS_WINDOW_PANE.get());
    windows("Zinc", BlockRegistry.ZINC_WINDOW.get(), BlockRegistry.ZINC_WINDOW_PANE.get());

    for (DyeColor color : DyeColor.values())
      connectedTextures(BlockRegistry.SHIPPING_CONTAINERS.get(color).get(), new ShippingContainerCTBehavior());
  }

  private static void windows (String metal, Block window, Block pane) {
    connectedTextures(window, new HorizontalCTBehaviour(SpriteShifts.METAL_WINDOWS.get(metal)));
    connectedTextures(pane, new GlassPaneCTBehaviour(SpriteShifts.METAL_WINDOWS.get(metal)));
  }

  private static void connectedTextures (Block block, ConnectedTextureBehaviour behaviour) {
    AllModels.register(block, CTModel.of(behaviour));
  }
}
