package com.github.talrey.createdeco.registrate;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SharedProperties {
    public static Block copperMetal() {
        return Blocks.COPPER_BLOCK.waxed().unaffected();
    }

    public static Block softMetal() {
        return Blocks.GOLD_BLOCK;
    }
}
