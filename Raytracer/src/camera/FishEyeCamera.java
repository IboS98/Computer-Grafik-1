package camera;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import math.Vector3;
import math.Point3;
import raytracer.Ray;

/**
 * Diese Klasse repraesentiert die Fischaugenkamera
 */
public class FishEyeCamera extends Camera {

    public final double angle;

    /**
     * Konstruktor der Klasse FishEyeCamera
     *
     * @param e eyeposition Position
     * @param g gazeposition Blickrichtung
     * @param t up Vektor
     * @param angle Oeffnungswinkel
     */
    public FishEyeCamera(final Point3 e, final Vector3 g, final Vector3 t, final double angle){
        super(e, g, t);
        this.angle = angle;
    }
    
    @Override
    public Ray rayFor(final int width, final int height, final int x, final int y){
        double x2 = (double) (2*x)/width-1;
        double y2 = (double) (2*y)/height-1;
        double r = sqrt(x2*x2 + y2*y2);
        if(r > 1) return null;
        
        double phi;
        if(x2 < 0) phi = PI - asin(y2/r);
        else phi = asin(y2/r);
        if(r == 0) phi = 0;
        
        double theta = r * angle/2;
        Vector3 d = u.mul(sin(theta) * cos(phi)).add(v.mul(sin(theta) * sin(phi))).add(g.mul(cos(theta)));
        return new Ray(e, d);
    }
}