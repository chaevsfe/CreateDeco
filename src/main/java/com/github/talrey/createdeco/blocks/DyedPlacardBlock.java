package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.zurrtum.create.content.decoration.placard.PlacardBlock;
import com.zurrtum.create.content.decoration.placard.PlacardBlockEntity;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DyedPlacardBlock extends PlacardBlock {
  public DyedPlacardBlock (Properties props) {
    super(props);
  }

  @Override
  public BlockEntityType<? extends PlacardBlockEntity> getBlockEntityType() {
    return BlockRegistry.PLACARD_ENTITIES.get();
  }

  @Override
  public ItemRequirement getRequiredItems (BlockState state, BlockEntity be) {
    ItemStack placard = state.getBlock().asItem().getDefaultInstance();
    if (be instanceof PlacardBlockEntity placardBE) {
      ItemStack held = placardBE.getHeldItem();
      if (!held.isEmpty()) {
        return new ItemRequirement(List.of(
          new ItemRequirement.StackRequirement(placard, ItemRequirement.ItemUseType.CONSUME),
          new ItemRequirement.StrictNbtStackRequirement(held, ItemRequirement.ItemUseType.CONSUME)
        ));
      }
    }
    return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, placard);
  }

  public static class Entity extends PlacardBlockEntity {
    public Entity (BlockPos pos, BlockState state) {
      super(pos, state);
    }
  }
}
