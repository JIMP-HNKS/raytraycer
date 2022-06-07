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

@Builder
@AllArgsConstructor
public class Raytracer implements Runnable {

    private final int width, height;
    private volatile BufferedImage image;

    private final Scene scene;
    private final Camera camera;

    private final int sampleCount;

    private volatile Deque<int[]> dividedStack;

    public Raytracer(Scene scene, Camera camera, int sampleCount, int width, int height) {
        this.scene = scene;
        this.camera = camera;
        this.sampleCount = sampleCount;

        this.width = width;
        this.height = height;
        generateStack();
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

        synchronized (image) {
            image.setRGB(x, y, color.getRGB());
        }
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
        if (dividedStack.isEmpty()) {
            generateStack();
        }
        raytrace(0, 0, width, height);
    }

    @Override
    public void run() {
        while (!dividedStack.isEmpty()) {
            int[] startingPoint = dividedStack.pop();
            int localWidth = Math.min(width - startingPoint[0], 64);
            int localHeight = Math.min(height - startingPoint[1], 64);

            raytrace(startingPoint[0], startingPoint[1], localWidth, localHeight);
        }
    }

    public void generateStack() {
        dividedStack = new ArrayDeque<>();
        for (int x = 0; x < width; x = x + 64) {
            for (int y = 0; y < height; y = y + 64) {
                dividedStack.push(new int[]{x, y});
            }
        }
    }

}
