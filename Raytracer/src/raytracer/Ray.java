package raytracer;

import math.Point3;
import math.Vector3;

/**
 * Diese Klasse repraesentiert den Strahl
 */
public class Ray {

    // origin Ursprung
    public final Point3 o;

    // direction Richtung
    public final Vector3 d;

    /**
     * Der Konstruktor der Klasse Ray
     *
     * @param o origin Ursprung
     * @param d direction Richtung
     */
    public Ray(final Point3 o, final Vector3 d){
        this.o = o;
        this.d = d;
    }

    /**
     * Die Methode at nimmt als Parameter ein t entgegen
     * und gibt den entsprechenden Punkt
     *
     * @param t up Vector
     * @return Gibt den entsprechenden Punkt wieder
     */
    public Point3 at(final double t) {
        return new Point3(o.x + t * d.x, o.y + t * d.y, o.z + t * d.z);
    }

    /**
     * Die Methode tOf nimmt als Parameter ein p entgegen
     * und gibt das entsprechende t zurück
     *
     * @param p point Punkt
     * @return Gibt das entsprechende t zurück
     */
    public double tOf(final Point3 p){
        return (p.x - o.x) / d.x;
    }
    
    @Override
    public String toString() {
        return o.toString() + "; " + d.toString();
    }
}