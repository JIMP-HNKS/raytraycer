package com.hnks.raytraycer;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.camera.Camera;
import com.hnks.raytraycer.util.ColorUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class Raytracer {

    private final int width, height;
    private volatile BufferedImage image;

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

    public void createImage() {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void raytrace(int x, int y) {
        Vector sampleGather = new Vector(0, 0, 0);

        for (int i = 0; i < sampleCount; i++) {
            Ray ray = camera.project(x, y);

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
                if (x >= this.width || y >= this.height) {
                    continue;
                }

                raytrace(x, y);
            }
        }
    }

    public void raytrace() {
        raytrace(0, 0, width, height);
    }

    public Runnable raytraceThreaded(int tileSize, int tileStart, int tileSkip) {
        return () -> {
            int tileCountX = (int) Math.ceil((double) width / tileSize);
            int tileCountY = (int) Math.ceil((double) height / tileSize);
            int tileCount = tileCountX * tileCountY;

            for (int i = tileStart; i < tileCount; i += tileSkip) {
                int tileX = i % tileCountX;
                int tileY = i / tileCountX;

                int localX = tileX * tileSize;
                int localY = tileY * tileSize;
                int localWidth = Math.min(width - localX, 64);
                int localHeight = Math.min(height - localY, 64);

                raytrace(localX, localY, localWidth, localHeight);
            }
        };
    }
}
