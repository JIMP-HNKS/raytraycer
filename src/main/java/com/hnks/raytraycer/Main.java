package com.hnks.raytraycer;

import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;

public class Main {

    static final int THREADS_NUM = 10;

    public static void main(String[] args) {

        try {

            Raytracer raytracer = new RaytracerLoader(new File("scene.dat")).read();
            raytracer.generateStack();
            Thread[] threads = new Thread[THREADS_NUM];

            long start = System.nanoTime();

            for (int i = 0; i < THREADS_NUM; i++) {
                threads[i] = new Thread(raytracer);
                threads[i].setName("Thread: " + i);
                threads[i].start();

                System.out.println("Running " + threads[i].getName());
            }

            for (int i = 0; i < THREADS_NUM; i++) {
                threads[i].join();
            }

            ImageIO.write(raytracer.getImage(), "png", new File("out.png"));

            System.out.println("Execution time: " + (double)(System.nanoTime() - start)/ 10000000000L);

        } catch (IOException | InterruptedException ignored) {
        }
    }
}
