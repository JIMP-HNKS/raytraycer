package com.hnks.raytraycer.ray;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.scene.geom.SceneGeometry;

public record RayHit(boolean hit, double distance, Vector position, Vector normal, SceneGeometry hitObject, Ray ray) {
    public static RayHit FLYAWAY = new RayHit(false, Double.POSITIVE_INFINITY, null, null, null, null);
}
