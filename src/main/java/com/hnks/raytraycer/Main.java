package com.hnks.raytraycer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;

public class Main {

    static final int THREADS_NUM = 4;

    public static void main(String[] args) {
        String inputPathName = "scene.dat";
        String outputPathName = "out.png";

        if (args.length == 4) {
            System.out.println(args[0]);
            System.out.println(args[1]);
            System.out.println(args[2]);
            System.out.println(args[3]);


            switch (args[0]) {
                case "-i": inputPathName = args[1];
                case "-o": outputPathName = args[1];
            }

            switch (args[2]) {
                case "-i": inputPathName = args[3];
                case "-o": outputPathName = args[3];
            }
        }

        try {
            Raytracer raytracer = new RaytracerLoader(new File(inputPathName)).read();
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

            ImageIO.write(raytracer.getImage(), "png", new File(outputPathName));

            System.out.println("Execution+save time: " + (double)(System.nanoTime() - start)/ 1000000000L);

        } catch (IOException | InterruptedException ignored) {
        }
    }
}
