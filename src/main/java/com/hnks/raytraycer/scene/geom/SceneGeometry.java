package com.hnks.raytraycer.scene.geom;

import com.hnks.raytraycer.material.Material;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.ray.RayHit;

public interface SceneGeometry {
    RayHit hitTest(Ray ray);
    Material getMaterial();
}
