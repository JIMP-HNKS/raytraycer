package com.hnks.raytraycer.util;

import com.hnks.raytraycer.math.Vector;

public class CameraUtil {
    public static Vector imageCoordinatesToDisplacement(int x, int y, int width, int height) {
        double maxDim = Math.max(width, height);

        return new Vector(
                2.0 * ((double) x / maxDim) - 1.0,
                -2.0 * ((double) y / maxDim) + 1.0,
                0.0
        );
    }
}
