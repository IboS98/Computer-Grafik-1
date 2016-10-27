package camera;

import math.Point3;
import math.Vector3;
import raytracer.Ray;

/**
 * Das ist die abstrakte Klasse Camera
 */
public abstract class Camera {

    // eyeposition
    public final Point3 e;
    // gazeposition
    public final Vector3 g;
    // upvector
    public final Vector3 t;

    // txw/|txw|
    public final Vector3 u;
    // wxu
    public final Vector3 v;
    // -g/|g|
    public final Vector3 w;

    /**
     * Konstruktor der Klasse Camera
     *
     * @param e eyeposition Position
     * @param g gazeposition Blickrichtung
     * @param t up Vektor
     */
    public Camera(final Point3 e, final Vector3 g, final Vector3 t) {

        this.e = e;
        this.g = g;
        this.t = t;
        this.w = g.normalized().mul(-1);
        this.u = t.x(w).normalized();
        this.v = w.x(u);
    }

    /**
     * Methode rayFor der Klasse Camera
     *
     * @param w width Breite
     * @param h height Hoehe
     * @param x Koordinate in X Richtung
     * @param y Koordinate in Y Richtung
     * @return Rueckgabewerte von w, h, x, y
     */
    public abstract Ray rayFor(final int w, final int h, final int x, final int y);
}
