package com.hnks.raytraycer;

import com.hnks.raytraycer.material.Dielectric;
import com.hnks.raytraycer.material.Material;
import com.hnks.raytraycer.material.Mirror;
import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.scene.camera.Camera;
import com.hnks.raytraycer.scene.camera.PerspectiveCamera;
import com.hnks.raytraycer.scene.geom.Plane;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.geom.Sphere;
import com.hnks.raytraycer.scene.light.PointLight;
import com.hnks.raytraycer.util.ColorUtil;
import com.hnks.raytraycer.util.SamplerUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Material red = new Dielectric(new Vector(1, 0, 0));
        Material green = new Dielectric(new Vector(0, 1, 0));
        Material blue = new Dielectric(new Vector(0, 0, 1));
        Material white = new Dielectric(new Vector(1, 1, 1));
        Material groundMat = new Dielectric(new Vector(0.2, 0.2, 0.2));

        Material mirror = new Mirror(new Vector(0.9, 0.9, 0.9), 0);
        Material glossy = new Mirror(new Vector(1, 0.7, 0.4), 0.2);

        Scene scene = new Scene(new Vector(1, 1, 1), 8);
        scene.add(
                new Sphere(new Vector(0, 0, 0), 1, mirror),
                new Sphere(new Vector(-2, -1, 0), 1, red),
                new Sphere(new Vector(-2, 3, 0), 1, green),
                new Sphere(new Vector(3, 1, 0), 1, blue),
                new Plane(new Vector(0, 0, -1), new Vector(0, 0, 1), groundMat)
        );
        scene.addLight(
                new PointLight(
                        new Vector(0, 0, 5),
                        new Vector(1, 1, 1).scale(3),
                        1
                ),
                new PointLight(
                        new Vector(5, 5, 5),
                        new Vector(0.5, 0.3, 1),
                        3
                )
        );

        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);

        int sampleCount = 64;

        Camera camera = new PerspectiveCamera(
                new Vector(0, -3, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, 1),
                1,
                image.getWidth(), image.getHeight()
        );

        Raytracer raytracer = new Raytracer(scene, camera, sampleCount);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, raytracer.raytrace(x, y).getRGB());
            }
        }

        try {
            ImageIO.write(image, "png", new File("out.png"));
        } catch (IOException ignored) {}
    }
}
