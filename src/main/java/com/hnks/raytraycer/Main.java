package com.hnks.raytraycer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {

            Raytracer raytracer = new RaytracerLoader(new File("scene.dat")).read();
            raytracer.raytrace();

            ImageIO.write(raytracer.getImage(), "png", new File("out.png"));

        } catch (IOException ignored) {}
    }
}
