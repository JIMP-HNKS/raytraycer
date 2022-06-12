package com.hnks.raytraycer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) {
        String inputPathName = null;
        String outputPathName = null;
        int threadCount = 1;

        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 >= args.length) {
                System.out.println("Value for argument not provided.");
                System.exit(1);
            }

            switch (args[i]) {
                case "-i" -> {
                    inputPathName = args[i + 1];
                }
                case "-o" -> {
                    outputPathName = args[i + 1];
                }
                case "-n" -> {
                    threadCount = Integer.parseInt(args[i + 1]);
                }
            }
        }

        if (inputPathName == null || outputPathName == null) {
            System.out.println("Both input scene file and output path must be provided.");
            System.exit(1);
        }

        try {
            Raytracer raytracer = new RaytracerLoader(new File(inputPathName)).read();
            Thread[] threads = new Thread[threadCount];

            long start = System.nanoTime();

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(raytracer.raytraceThreaded(64, i, threadCount));
                threads[i].setName("Thread: " + i);
                threads[i].start();

                System.out.println("Running " + threads[i].getName());
            }

            for (int i = 0; i < threadCount; i++) {
                threads[i].join();
            }

            System.out.println("Execution time: " + (double)(System.nanoTime() - start)/ 1000000000L);

            ImageIO.write(raytracer.getImage(), "png", new File(outputPathName));

            System.out.println("Execution+save time: " + (double)(System.nanoTime() - start)/ 1000000000L);

        } catch (IOException | InterruptedException ignored) {
        }
    }
}
