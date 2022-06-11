package com.hnks.raytraycer.ray;

import com.hnks.raytraycer.math.Vector;

public record Ray(Vector origin, Vector direction, int depth) {
    public Vector atDistance(double distance) {
        return Vector.add(origin, direction.scale(distance));
    }
}
