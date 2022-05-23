package com.hnks.raytraycer.material;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;

public class Emissive implements Material {
    private final Vector color;

    public Emissive(Vector color) {
        this.color = color;
    }

    @Override
    public Vector shade(Scene scene, RayHit hit) {
        return color;
    }
}
