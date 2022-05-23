package com.hnks.raytraycer.scene.light;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.geom.SceneGeometry;

public interface SceneLight {
    Vector light(Scene scene, RayHit hit);
    SceneGeometry getGeometry();
}
