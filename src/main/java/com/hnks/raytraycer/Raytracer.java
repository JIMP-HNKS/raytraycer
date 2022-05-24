package com.hnks.raytraycer;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.camera.Camera;
import com.hnks.raytraycer.util.ColorUtil;

import java.awt.*;

public class Raytracer {
    private final Scene scene;
    private final Camera camera;

    private final int sampleCount;

    public Raytracer(Scene scene, Camera camera, int sampleCount) {
        this.scene = scene;
        this.camera = camera;
        this.sampleCount = sampleCount;
    }

    public Color raytrace(int x, int y) {
        Vector sampleGather = new Vector(0, 0, 0);

        for (int i = 0; i < sampleCount; i++) {
            Ray ray = camera.project(x, y);

            scene.resetRayDepth();
            sampleGather = Vector.add(sampleGather, scene.shade(ray));
        }

        return ColorUtil.linearVectorToColor(
                sampleGather.scale(1.0 / sampleCount)
        );
    }
}
