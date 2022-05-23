package com.hnks.raytraycer.material;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;

public interface Material {
    Vector shade(Scene scene, RayHit hit);
}
