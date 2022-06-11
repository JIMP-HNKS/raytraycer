package com.hnks.raytraycer.util;

import com.hnks.raytraycer.math.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SamplerUtil {
    public Vector nextUnitDisplacement() {
        Random random = ThreadLocalRandom.current();

        return new Vector(
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1,
                0
        );
    }

    public Vector nextUnitVector() {
        Random random = ThreadLocalRandom.current();

        double angle = random.nextDouble() * 2 * Math.PI;
        double z = random.nextDouble() * 2 - 1;

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
