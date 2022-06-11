package com.hnks.raytraycer.material;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.util.SamplerUtil;

public class Mirror implements Material {
    private final Vector color;
    private final double roughness;

    public Mirror(Vector color, double roughness) {
        this.color = color;
        this.roughness = roughness;
    }

    @Override
    public Vector shade(Scene scene, RayHit hit) {
        if (scene.isRayDepthExceeded(hit.ray())) {
            return new Vector(0, 0, 0);
        }

        Vector incoming = hit.ray().direction();
        Vector reflectedDirection = Vector.reflect(incoming, hit.normal());
        Vector randomDirection = SamplerUtil.INSTANCE.nextUnitVectorHemi(hit.normal());

        Vector direction = Vector.lerp(roughness, reflectedDirection, randomDirection);
        Ray reflected = new Ray(hit.position(), direction, hit.ray().depth() + 1);

        return Vector.multiply(scene.shade(reflected, true), color);
    }
}
