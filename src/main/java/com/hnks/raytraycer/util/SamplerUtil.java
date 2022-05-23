package com.hnks.raytraycer.util;

import com.hnks.raytraycer.math.Vector;

import java.util.Random;

public class SamplerUtil {
    private final Random random;

    private SamplerUtil() {
        this.random = new Random();
    }

    public Vector nextUnitDisplacement() {
        return new Vector(
                this.random.nextDouble() * 2 - 1,
                this.random.nextDouble() * 2 - 1,
                0
        );
    }

    public Vector nextUnitVector() {
        double angle = this.random.nextDouble() * 2 * Math.PI;
        double z = this.random.nextDouble() * 2 - 1;

        double xy = Math.sqrt(1 - z * z);

        return new Vector(
                Math.cos(angle) * xy,
                Math.sin(angle) * xy,
                z
        );
    }

    public Vector nextUnitVectorHemi(Vector normal) {
        Vector unitVector = nextUnitVector();

        if (Vector.dot(unitVector, normal) < 0) {
            return unitVector.scale(-1);
        }

        return unitVector;
    }

    public static SamplerUtil INSTANCE = new SamplerUtil();
}
