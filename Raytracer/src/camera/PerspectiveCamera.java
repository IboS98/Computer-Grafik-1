package camera;

import math.Vector3;
import math.Point3;
import raytracer.Ray;

/**
 * Diese Klasse repraesentiert die Perspektivische Kamera
 */
public class PerspectiveCamera extends Camera {

    // Der Oeffnungswinkel
    public final double angle;

    /**
     * Konstruktor der Klasse PerspectiveCamera
     *
     * @param e eyeposition Position
     * @param g gazeposition Blickrichtung
     * @param t up Vektor
     * @param angle Oeffnungswinkel
     */
    public PerspectiveCamera(final Point3 e, final Vector3 g, final Vector3 t, final double angle){
        super(e, g, t);
        this.angle = angle/2;
    }
    
    @Override
    public Ray rayFor(final int width, final int height, final int x, final int y){
        Vector3 v1 = u.mul(x-(width-1)/2);
        Vector3 v2 = v.mul(y-(height-1)/2);
        Vector3 r = w.mul(-1).mul((height/2)/Math.tan(angle)).add(v1).add(v2);
        return new Ray(e,r.normalized());
    }
}
