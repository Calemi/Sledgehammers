package com.calemi.ccore.api.scanner;

import com.calemi.ccore.api.location.Location;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to scan through blocks of the same kind. Collects all scanned blocks in a list.
 */
public abstract class BlockScanner {

    public final ArrayList<Location> collectedLocations = new ArrayList<>();

    public final Location origin;
    public final int maxScanSize;

    /**
     * Creates a BlockScanner
     * @param origin The origin Location of the scan.
     * @param maxScanSize The maximum amount of Blocks to scan.
     */
    public BlockScanner(Location origin, int maxScanSize) {
        this.origin = origin;
        this.maxScanSize = maxScanSize;
    }

    /**
     * @param scannedLocation The currently scanned Location.
     * @param scannedState The currently scanned BlockState.
     * @return whether the scanner should collect this location or not.
     */
    public abstract boolean shouldCollect(Location scannedLocation, BlockState scannedState);

    public abstract boolean continueOnFailedCollect();

    public abstract List<Location> nextLocationsToScan(Location scannedLocation, BlockState scannedState);

    /**
     * Starts a scan that will search adjacent Blocks.
     */
    public void start() {
        reset();
        scan(origin);
    }

    /**
     * Clears the buffer.
     */
    public void reset() {
        collectedLocations.clear();
    }

    /**
     * Recursive method used to search through similar Blocks.
     * @param location The Location to search.
     */
    public void scan(Location location) {

        if (collectedLocations.size() >= maxScanSize) {
            return;
        }

        if (!collectedLocations.contains(location)) {

            if (shouldCollect(location, location.getBlockState())) {
                collectedLocations.add(location);
            }

            else if (!continueOnFailedCollect()) {
                return;
            }

            for (Location nextLocationToScan : nextLocationsToScan(location, location.getBlockState())) {
                scan(nextLocationToScan);
            }
        }
    }

    /**
     * @param location The Location to test.
     * @return True, if the given location is in the buffer.
     */
    public boolean contains(Location location) {

        for (Location nextLocation : collectedLocations) {

            if (nextLocation.equals(location)) {
                return true;
            }
        }

        return false;
    }
}