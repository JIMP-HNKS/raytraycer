package com.hnks.raytraycer.scene.camera;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.Ray;
import com.hnks.raytraycer.util.CameraUtil;
import com.hnks.raytraycer.util.SamplerUtil;
import lombok.Builder;

@Builder
public class PerspectiveCamera implements Camera {
    private final Vector origin;
    private final Vector eye;
    private final Vector up;

    private final double imageDistance;

    private final int width;
    private final int height;

    public PerspectiveCamera(Vector origin, Vector eye, Vector up, double imageDistance, int width, int height) {
        this.origin = origin;
        this.eye = eye;
        this.up = up;
        this.imageDistance = imageDistance;
        this.width = width;
        this.height = height;
    }

    @Override
    public Ray project(int imageX, int imageY) {
        Vector xAxis = Vector.cross(up, eye);

        Vector displacement = CameraUtil.imageCoordinatesToDisplacement(imageX, imageY, width, height);

        double pixelSize = 1.0 / Math.max(imageX, imageY) * 0.6;
        displacement = Vector.add(displacement, SamplerUtil.INSTANCE.nextUnitDisplacement().scale(pixelSize));

        Vector imagePlanePoint = Vector.add(
                origin,
                Vector.add(
                        eye.scale(imageDistance),
                        Vector.add(
                                xAxis.scale(displacement.x()),
                                up.scale(displacement.y())
                        )
                )
        );

        return new Ray(origin, Vector.sub(imagePlanePoint, origin).normalized());
    }
}
