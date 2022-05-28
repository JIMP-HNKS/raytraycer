package com.hnks.raytraycer.scene;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.geom.SceneGeometry;
import com.hnks.raytraycer.scene.light.SceneLight;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder @AllArgsConstructor
public class Scene {
    private final Vector background;

    private final List<SceneGeometry> objects;
    private final List<SceneLight> lights;

    private final double epsilon = 1e-3;

    private final int maxDepth;
    private int currentDepth = 0;

    public Scene(Vector background, List<SceneGeometry> objects, List<SceneLight> lights, int maxDepth) {
        this.background = background;
        this.objects = objects;
        this.lights = lights;
        this.maxDepth = maxDepth;
    }

    public void add(SceneGeometry ...objects) {
        Collections.addAll(this.objects, objects);
    }

    public void addLight(SceneLight ...lights) {
        Collections.addAll(this.lights, lights);
    }

    public List<SceneGeometry> getObjects() {
        return objects;
    }

    public List<SceneLight> getLights() {
        return lights;
    }

    public boolean isRayDepthExceeded() {
        currentDepth += 1;

        return currentDepth >= maxDepth;
    }

    public void resetRayDepth() {
        currentDepth = 0;
    }

    public RayHit hitTest(Ray ray, boolean hitLights) {
        double minDistance = Double.POSITIVE_INFINITY;
        RayHit minHit = RayHit.FLYAWAY;

        for (SceneGeometry object : objects) {
            RayHit hit = object.hitTest(ray);

            if (hit.hit() && hit.distance() > epsilon && hit.distance() < minDistance) {
                minDistance = hit.distance();
                minHit = hit;
            }
        }

        if (hitLights) {
            for (SceneLight light : lights) {
                RayHit hit = light.getGeometry().hitTest(ray);

                if (hit.hit() && hit.distance() > epsilon && hit.distance() < minDistance) {
                    minDistance = hit.distance();
                    minHit = hit;
                }
            }
        }

        return minHit;
    }

    public RayHit hitTest(Ray ray) {
        return hitTest(ray, false);
    }

    public Vector shade(Ray ray, boolean shadeLights) {
        RayHit hit = hitTest(ray, shadeLights);

        if (hit.hit()) {
            return hit.hitObject().getMaterial().shade(this, hit);
        }

        return background;
    }

    public Vector shade(Ray ray) {
        return shade(ray, false);
    }
}
