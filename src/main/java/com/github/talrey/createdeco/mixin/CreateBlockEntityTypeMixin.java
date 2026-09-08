package com.github.talrey.createdeco.mixin;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.blocks.DyedPlacardBlock;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.registrate.entry.BlockEntityEntry;
import com.zurrtum.create.content.decoration.placard.PlacardBlockEntity;
import com.zurrtum.create.content.logistics.vault.ItemVaultBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ItemVaultBlockEntity.class, PlacardBlockEntity.class})
public abstract class CreateBlockEntityTypeMixin {
    @ModifyArg(
        method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/zurrtum/create/foundation/blockEntity/SmartBlockEntity;<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
        ),
        index = 0
    )
    private static BlockEntityType<?> createdeco$substituteType(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof ShippingContainerBlock) {
            DyeColor color = ShippingContainerBlock.getColor(state);
            BlockEntityEntry<ShippingContainerBlock.Entity> entry = BlockRegistry.CONTAINER_ENTITIES.get(color);
            if (entry != null)
                return entry.get();
        }
        if (state.getBlock() instanceof DyedPlacardBlock && BlockRegistry.PLACARD_ENTITIES != null) {
            return BlockRegistry.PLACARD_ENTITIES.get();
        }
        return type;
    }
}
