package com.hnks.raytraycer.material;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.light.SceneLight;

public class Lambert implements Material {
    private final Vector color;

    public Lambert(Vector color) {
        this.color = color;
    }

    @Override
    public Vector shade(Scene scene, RayHit hit) {
        Vector lightIntensity = new Vector(0, 0, 0);

        for (SceneLight light : scene.getLights()) {
            lightIntensity = Vector.add(lightIntensity, light.light(scene, hit));
        }

        return Vector.multiply(lightIntensity, color);
    }
}
