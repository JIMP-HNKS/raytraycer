package com.hnks.raytraycer;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.camera.Camera;
import com.hnks.raytraycer.util.ColorUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.awt.*;
import java.awt.image.BufferedImage;

@Builder @AllArgsConstructor
public class Raytracer {
    private final int width, height;
    private BufferedImage image;

    private final Scene scene;
    private final Camera camera;

    private final int sampleCount;

    public Raytracer(Scene scene, Camera camera, int sampleCount, int width, int height) {
        this.scene = scene;
        this.camera = camera;
        this.sampleCount = sampleCount;

        this.width = width;
        this.height = height;
        createImage();
    }

    private void createImage() {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void raytrace(int x, int y) {
        if (image == null) {
            createImage();
        }

        Vector sampleGather = new Vector(0, 0, 0);

        for (int i = 0; i < sampleCount; i++) {
            Ray ray = camera.project(x, y);

            scene.resetRayDepth();
            sampleGather = Vector.add(sampleGather, scene.shade(ray));
        }

        Color color = ColorUtil.linearVectorToColor(
                sampleGather.scale(1.0 / sampleCount)
        );

        image.setRGB(x, y, color.getRGB());
    }

    public void raytrace(int left, int top, int width, int height) {
        for (int x = left; x < (left + width); x++) {
            for (int y = top; y < (top + height); y++) {
                if (x >= width || y >= height) {
                    continue;
                }

                raytrace(x, y);
            }
        }
    }

    public void raytrace() {
        raytrace(0, 0, width, height);
    }
}
