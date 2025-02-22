package com.calemi.ccore.api.worldedit.shape;

import com.calemi.ccore.api.location.Location;

import java.util.ArrayList;

public class ShapeCube extends ShapeBase {

    /**
     * Creates a cube Shape.
     * @param corner1 The first corner Location of the cube.
     * @param corner2 The second corner Location of the cube.
     */
    public ShapeCube(Location corner1, Location corner2) {

        ArrayList<Location> shapeLocations = new ArrayList<>();

        for (int x = Math.min(corner1.getX(), corner2.getX()); x <= Math.max(corner1.getX(), corner2.getX()); x++) {

            for (int y = Math.min(corner1.getY(), corner2.getY()); y <= Math.max(corner1.getY(), corner2.getY()); y++) {

                for (int z = Math.min(corner1.getZ(), corner2.getZ()); z <= Math.max(corner1.getZ(), corner2.getZ()); z++) {
                    shapeLocations.add(new Location(corner1.getLevel(), x, y, z));
                }
            }
        }

        addShapeLocations(shapeLocations);
    }

    /**
     * Creates a cube Shape.
     * @param origin The origin Location of the circle.
     * @param xRadius The x radius of the cube.
     * @param yRadius The y radius of the cube.
     * @param zRadius The z radius of the cube.
     */
    public ShapeCube(Location origin, int xRadius, int yRadius, int zRadius) {

        ArrayList<Location> shapeLocations = new ArrayList<>();

        for (int x = -xRadius; x <= xRadius; x++) {

            for (int y = -yRadius; y <= yRadius; y++) {

                for (int z = -zRadius; z <= zRadius; z++) {
                    Location nextLocation = origin.copy();
                    nextLocation.offset(x, y, z);
                    shapeLocations.add(nextLocation);
                }
            }
        }

        addShapeLocations(shapeLocations);
    }
}