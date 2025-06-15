package com.calemi.sledgehammers.block.scanner;

import com.calemi.ccore.api.block.scanner.VeinBlockScanner;
import com.calemi.ccore.api.location.BlockLocation;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.Tags;

public class OreVeinBlockScanner extends VeinBlockScanner {

    public OreVeinBlockScanner(BlockLocation originLocation, int maxCollectionSize) {
        super(originLocation, maxCollectionSize);
    }

    @Override
    public boolean shouldCollect(BlockPos scannedBlockPos) {
        return getLevel().getBlockState(scannedBlockPos).is(Tags.Blocks.ORES);
    }
}
