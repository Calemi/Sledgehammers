package com.calemi.ccore.api.scanner;

import com.calemi.ccore.api.location.Location;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class VeinBlockScanner extends BlockScanner {

    /**
     * Creates a BlockScanner
     * @param origin      The origin Location of the scan.
     * @param maxScanSize The maximum amount of Blocks to scan.
     */
    public VeinBlockScanner(Location origin, int maxScanSize) {
        super(origin, maxScanSize);
    }

    @Override
    public boolean shouldCollect(Location scannedLocation, BlockState scannedState) {
        return scannedLocation.getBlock().equals(origin.getBlock());
    }

    @Override
    public boolean continueOnFailedCollect() {
        return false;
    }

    @Override
    public List<Location> nextLocationsToScan(Location scannedLocation, BlockState scannedState) {

        List<Location> nextLocations = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {

                    Location nextLocation = new Location(scannedLocation.getLevel(), scannedLocation.getX() + x, scannedLocation.getY() + y, scannedLocation.getZ() + z);
                    scan(nextLocation);
                }
            }
        }

        return List.of();
    }
}
