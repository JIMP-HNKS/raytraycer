package com.hnks.raytraycer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;

public class Main {

    static final int THREADS_NUM = 4;

    public static void main(String[] args) {

        try {

            Raytracer raytracer = new RaytracerLoader(new File("scene.dat")).read();
            Thread[] threads = new Thread[THREADS_NUM];

            long start = System.nanoTime();

            for (int i = 0; i < THREADS_NUM; i++) {
                threads[i] = new Thread(raytracer.raytraceThreaded(64, i, THREADS_NUM));
                threads[i].setName("Thread: " + i);
                threads[i].start();

                System.out.println("Running " + threads[i].getName());
            }

            for (int i = 0; i < THREADS_NUM; i++) {
                threads[i].join();
            }

            System.out.println("Execution time: " + (double)(System.nanoTime() - start)/ 1000000000L);

            ImageIO.write(raytracer.getImage(), "png", new File("out.png"));

            System.out.println("Execution+save time: " + (double)(System.nanoTime() - start)/ 1000000000L);

        } catch (IOException | InterruptedException ignored) {
        }
    }
}
