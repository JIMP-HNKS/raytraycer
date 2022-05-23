package com.hnks.raytraycer.scene.geom;

import com.hnks.raytraycer.material.Material;
import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;

public class Plane implements SceneGeometry {
    private final Vector origin;
    private final Vector normal;

    private final Material material;

    public Plane(Vector origin, Vector normal, Material material) {
        this.origin = origin;
        this.normal = normal;
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public RayHit hitTest(Ray ray) {
        double denom = Vector.dot(normal, ray.direction());

        if (Math.abs(denom) > 1e-3) {
            double distance = Vector.dot(Vector.sub(origin, ray.origin()), normal) / denom;

            if (distance > 1e-3) {
                Vector position = ray.atDistance(distance);

                return new RayHit(true, distance, position, normal, this, ray);
            }
        }

        return RayHit.FLYAWAY;
    }
}
