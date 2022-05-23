package com.hnks.raytraycer.scene.geom;

import com.hnks.raytraycer.material.Material;
import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;

public class Sphere implements SceneGeometry {
    private final Vector origin;
    private final double radius;

    private final Material material;

    public Sphere(Vector origin, double radius, Material material) {
        this.origin = origin;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public RayHit hitTest(Ray ray) {
        double a = ray.direction().lengthsq();
        double b = 2 * Vector.dot(ray.direction(), Vector.sub(ray.origin(), origin));
        double c = Vector.sub(ray.origin(), origin).lengthsq() - radius * radius;

        double delta = b * b - 4 * a * c;

        double distance;

        if (delta < 0) {
            return RayHit.FLYAWAY;
        } else if (delta == 0) {
            distance = -b / (2 * a);
        } else {
            distance = Math.min(
                    (-b + Math.sqrt(delta)) / (2 * a),
                    (-b - Math.sqrt(delta)) / (2 * a)
            );
        }

        Vector position = ray.atDistance(distance);
        Vector normal = Vector.sub(position, origin).normalized();

        return new RayHit(true, distance, position, normal, this, ray);
    }
}
