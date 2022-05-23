package com.hnks.raytraycer.util;

import com.hnks.raytraycer.math.Vector;
import com.hnks.raytraycer.ray.RayHit;

public class FresnelUtil {
    public static double calculateFresnelDielectricCos(double cosi, double eta) {
        double c = Math.abs(cosi);
        double g = eta * eta - 1 + c * c;

        if (g > 0) {
            g = Math.sqrt(g);

            double a = (g - c) / (g + c);
            double b = (c * (g + c) - 1) / (c * (g - c) + 1);

            return 0.5 * a * a * (1 + b * b);
        }

        return 1.0;
    }

    public static double calculateFresnelCoefficient(RayHit hit, double ior) {
        double cosi = Vector.dot(hit.ray().direction().scale(-1), hit.normal());

        return calculateFresnelDielectricCos(cosi, ior);
    }
}
