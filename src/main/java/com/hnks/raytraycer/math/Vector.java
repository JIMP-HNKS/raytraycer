package com.hnks.raytraycer.math;

public record Vector(double x, double y, double z) {
    public double length() {
        return Math.sqrt(Vector.dot(this, this));
    }

    public double lengthsq() {
        return Vector.dot(this, this);
    }

    public Vector scale(double s) {
        return new Vector(x * s, y * s, z * s);
    }

    public Vector normalized() {
        return scale(1.0 / length());
    }

    public static double distance(Vector v1, Vector v2) {
        return Vector.sub(v1, v2).length();
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.x() + v2.x(), v1.y() + v2.y(), v1.z() + v2.z());
    }

    public static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x() - v2.x(), v1.y() - v2.y(), v1.z() - v2.z());
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.x() * v2.x() + v1.y() * v2.y() + v1.z() * v2.z();
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }

    public static Vector multiply(Vector v1, Vector v2) {
        return new Vector(v1.x() * v2.x(), v1.y() * v2.y(), v1.z() * v2.z());
    }

    public static Vector reflect(Vector incoming, Vector normal) {
        return Vector.sub(incoming, normal.scale(2 * Vector.dot(incoming, normal)));
    }

    public static Vector lerp(double x, Vector a, Vector b) {
        return Vector.add(a, Vector.sub(b, a).scale(x));
    }

    @Override
    public String toString() {
        return String.format("Vector[ %g, %g, %g ]", x, y, z);
    }
}
