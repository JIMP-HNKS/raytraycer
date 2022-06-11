package com.hnks.raytraycer.scene.light;

import com.hnks.raytraycer.material.Emissive;
import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.geom.SceneGeometry;
import com.hnks.raytraycer.scene.geom.Sphere;
import com.hnks.raytraycer.util.SamplerUtil;

public class PointLight implements SceneLight {
    private final Vector position;
    private final Vector color;
    private final double radius;

    public PointLight(Vector position, Vector color, double radius) {
        this.position = position;
        this.color = color;
        this.radius = radius;
    }

    @Override
    public Vector light(Scene scene, RayHit hit) {
        Vector lightPosition = Vector.add(
                position, SamplerUtil.INSTANCE.nextUnitVector().scale(radius)
        );

        double distance = Vector.sub(lightPosition, hit.position()).length();
        Vector direction = Vector.sub(lightPosition, hit.position()).normalized();

        Ray shadowRay = new Ray(hit.position(), direction, 0);
        RayHit shadowHit = scene.hitTest(shadowRay);

        if (!shadowHit.hit() || shadowHit.distance() > distance) {
            double intensity = Math.max(0, Vector.dot(direction, hit.normal()));
            return color.scale(intensity);
        }

        return new Vector(0, 0, 0);
    }

    @Override
    public SceneGeometry getGeometry() {
        return new Sphere(position, radius, new Emissive(color));
    }
}
