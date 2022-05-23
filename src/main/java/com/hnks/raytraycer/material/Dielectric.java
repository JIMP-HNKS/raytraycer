package com.hnks.raytraycer.material;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.util.FresnelUtil;

public class Dielectric implements Material {
    private final Vector color;

    public Dielectric(Vector color) {
        this.color = color;
    }

    @Override
    public Vector shade(Scene scene, RayHit hit) {
        double fresnelCoeff = FresnelUtil.calculateFresnelCoefficient(hit, 1.45);

        Vector diffuse = new Lambert(color).shade(scene, hit);
        Vector mirror = new Mirror(new Vector(1, 1, 1), 0).shade(scene, hit);

        return Vector.lerp(fresnelCoeff, diffuse, mirror);
    }
}
