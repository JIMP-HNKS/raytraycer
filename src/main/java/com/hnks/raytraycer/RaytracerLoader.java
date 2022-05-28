package com.hnks.raytraycer;

import com.hnks.raytraycer.material.Dielectric;
import com.hnks.raytraycer.material.Lambert;
import com.hnks.raytraycer.material.Material;
import com.hnks.raytraycer.material.Mirror;
import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.scene.Scene;
import com.hnks.raytraycer.scene.camera.Camera;
import com.hnks.raytraycer.scene.camera.PerspectiveCamera;
import com.hnks.raytraycer.scene.geom.Plane;
import com.hnks.raytraycer.scene.geom.SceneGeometry;
import com.hnks.raytraycer.scene.geom.Sphere;
import com.hnks.raytraycer.scene.light.PointLight;
import com.hnks.raytraycer.scene.light.SceneLight;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaytracerLoader {
    private final BufferedReader reader;

    public RaytracerLoader(File file) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(file));
    }

    public Raytracer read() throws IOException {
        int lineNumber = 0;

        Scene.SceneBuilder sceneBuilder = Scene.builder();
        PerspectiveCamera.PerspectiveCameraBuilder cameraBuilder = PerspectiveCamera.builder();

        Raytracer.RaytracerBuilder raytracerBuilder = Raytracer.builder();

        Map<String, Material> materials = new HashMap<>();
        List<SceneGeometry> objects = new ArrayList<>();
        List<SceneLight> lights = new ArrayList<>();

        while(reader.ready()) {
            lineNumber++;

            String line = reader.readLine().trim();
            if (line.startsWith("#")) {
                continue;
            }

            String[] components = line.split("\s+");

            // TODO: exceptions!!!
            switch (components[0]) {
                case "image_size" -> {
                    int width = Integer.parseInt(components[1]);
                    int height = Integer.parseInt(components[2]);

                    raytracerBuilder = raytracerBuilder.width(width).height(height);
                    cameraBuilder = cameraBuilder.width(width).height(height);
                }
                case "sample_count" -> {
                    raytracerBuilder = raytracerBuilder.sampleCount(Integer.parseInt(components[1]));
                }
                case "max_depth" -> {
                    sceneBuilder = sceneBuilder.maxDepth(Integer.parseInt(components[1]));
                }
                case "world_bg" -> {
                    sceneBuilder = sceneBuilder.background(new Vector(
                            Double.parseDouble(components[1]),
                            Double.parseDouble(components[2]),
                            Double.parseDouble(components[3])
                    ));
                }

                case "cam_origin" -> {
                    cameraBuilder = cameraBuilder.origin(new Vector(
                            Double.parseDouble(components[1]),
                            Double.parseDouble(components[2]),
                            Double.parseDouble(components[3])
                    ));
                }
                case "cam_up" -> {
                    cameraBuilder = cameraBuilder.up(new Vector(
                            Double.parseDouble(components[1]),
                            Double.parseDouble(components[2]),
                            Double.parseDouble(components[3])
                    ).normalized());
                }
                case "cam_eye" -> {
                    cameraBuilder = cameraBuilder.eye(new Vector(
                            Double.parseDouble(components[1]),
                            Double.parseDouble(components[2]),
                            Double.parseDouble(components[3])
                    ).normalized());
                }
                case "cam_plane_distance" -> {
                    cameraBuilder = cameraBuilder.imageDistance(Double.parseDouble(components[1]));
                }

                case "dielectric_mat" -> {
                    materials.put(
                            components[1],
                            new Dielectric(
                                    new Vector(
                                            Double.parseDouble(components[2]),
                                            Double.parseDouble(components[3]),
                                            Double.parseDouble(components[4])
                                    )
                            )
                    );
                }
                case "mirror_mat" -> {
                    materials.put(
                            components[1],
                            new Mirror(
                                    new Vector(
                                            Double.parseDouble(components[2]),
                                            Double.parseDouble(components[3]),
                                            Double.parseDouble(components[4])
                                    ),
                                    Double.parseDouble(components[5])
                            )
                    );
                }
                case "diffuse_mat" -> {
                    materials.put(
                            components[1],
                            new Lambert(
                                    new Vector(
                                            Double.parseDouble(components[2]),
                                            Double.parseDouble(components[3]),
                                            Double.parseDouble(components[4])
                                    )
                            )
                    );
                }

                case "sphere" -> {
                    Material mat = materials.get(components[5]);
                    if (mat == null) {
                        throw new IOException();
                    }

                    objects.add(new Sphere(
                            new Vector(
                                    Double.parseDouble(components[1]),
                                    Double.parseDouble(components[2]),
                                    Double.parseDouble(components[3])
                            ),
                            Double.parseDouble(components[4]),
                            mat
                    ));
                }
                case "plane" -> {
                    Material mat = materials.get(components[7]);
                    if (mat == null) {
                        throw new IOException();
                    }

                    objects.add(new Plane(
                            new Vector(
                                    Double.parseDouble(components[1]),
                                    Double.parseDouble(components[2]),
                                    Double.parseDouble(components[3])
                            ),
                            new Vector(
                                    Double.parseDouble(components[4]),
                                    Double.parseDouble(components[5]),
                                    Double.parseDouble(components[6])
                            ),
                            mat
                    ));
                }

                case "point_light" -> {
                    lights.add(new PointLight(
                            new Vector(
                                    Double.parseDouble(components[1]),
                                    Double.parseDouble(components[2]),
                                    Double.parseDouble(components[3])
                            ),
                            new Vector(
                                    Double.parseDouble(components[4]),
                                    Double.parseDouble(components[5]),
                                    Double.parseDouble(components[6])
                            ),
                            Double.parseDouble(components[7])
                    ));
                }
            }
        }

        Scene scene = sceneBuilder.lights(lights).objects(objects).build();

        Camera camera = cameraBuilder.build();

        return raytracerBuilder.scene(scene).camera(camera).build();
    }
}
