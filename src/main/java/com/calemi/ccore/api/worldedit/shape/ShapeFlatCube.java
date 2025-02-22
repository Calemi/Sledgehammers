package com.calemi.ccore.api.worldedit.shape;

import com.calemi.ccore.api.location.Location;
import net.minecraft.core.Direction;

import java.util.ArrayList;

public class ShapeFlatCube extends ShapeBase {

    /**
     * Creates a flat cube Shape.
     * @param origin The origin Location of the flat cube.
     * @param facing Determines the direction of the flat cube
     * @param radius The radius of the flat cube.
     */
    public ShapeFlatCube(Location origin, Direction facing, int radius) {

        ArrayList<Location> shapeLocations = new ArrayList<>();

        int xRad = radius;
        int yRad = radius;
        int zRad = radius;

        if (facing == Direction.UP || facing == Direction.DOWN) {
            yRad = 0;
        }

        else if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            zRad = 0;
        }

        else if (facing == Direction.EAST || facing == Direction.WEST) {
            xRad = 0;
        }

        addShapeLocations(new ShapeCube(origin, xRad, yRad, zRad).getShapeLocations());
    }
}