package com.hnks.raytraycer.scene.camera;

import com.hnks.raytraycer.ray.Ray;

public interface Camera {
    Ray project(int imageX, int imageY);
}
