package geometry;

import math.Normal3;
import raytracer.Ray;

/**
 * Die Klasse Hit
 */
public class Hit {

    // Der Schnittpunkt
    public final double t;

    // Der Strahl
    public final Ray ray;

    // Die geometrische Figur
    public final Geometry geo;

    /**
     * Die Normale des Schnittpunktes
     */
    public final Normal3 n;

    /**
     * Der Konstruktor der Klasse Hit
     *
     * @param t Der Schnittpunkt
     * @param ray Der Strahl
     * @param geo Die Geometrie
     * @param n Die Normale
     */
    public Hit(double t, Ray ray, Geometry geo, Normal3 n) {

        this.t = t;
        this.ray = ray;
        this.geo = geo;
        this.n = n;
    }
}
