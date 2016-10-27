package camera;

import math.Point3;
import math.Vector3;
import raytracer.Ray;

/**
 * Diese Klasse repraesentiert die Orthografische Kamera
 */
public class OrthographicCamera extends Camera {

    // Der Skalierungsfaktor
    public final double s;

    /**
     * Konstruktor der Klasse OrthographicCamera
     *
     * @param e eyeposition Position
     * @param g gazeposition Blickrichtung
     * @param t up Vektor
     * @param s Skalierungsfaktor
     */
    public OrthographicCamera(final Point3 e,final Vector3 g,final Vector3 t,final double s) {
        super(e, g, t);
        this.s = s;
    }
    
    @Override
    public Ray rayFor(final int width, final int height, final int x, final int y){
        Vector3 d = this.w.mul(-1);// d = -w
        double a = (double) width/ (double) height;
        Vector3 wVector = u.mul(s*(x-(width-1)/2)/(width-1));
        Vector3 hVector = v.mul(s*(y-(height-1)/2)/(height-1));
        return new Ray(e.add(wVector.mul(a)).add(hVector),d);
    }

}
