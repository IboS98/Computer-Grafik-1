package light;

import geometry.Color;
import geometry.Hit;
import math.Point3;
import math.Vector3;
import raytracer.Ray;
import raytracer.World;

/**
 * Das ist die Klasse SpotLight
 */
public class SpotLight extends Light {

    // Die Position eines Punktes
    public final Point3 position;

    // Die Richtung eines Vektors
    public final Vector3 direction;

    // Der halbe Winkel
    public final double halfAngle;

    /**
     * Der Konstruktor der Klasse SpotLight
     *
     * @param position  Die Position eines Punktes
     * @param direction Die Richtung eines Vektors
     * @param halfAngle Der halbe Winkel
     */
    public SpotLight(final Color color, final Point3 position, final Vector3 direction, final double halfAngle, boolean castsShadows) {
        super(color, castsShadows);
        this.position = position;
        this.direction = direction;
        this.halfAngle = halfAngle;
    }
    
    @Override
    public boolean illuminates(Point3 point, World world) {
        if(!castsShadows) return true;
        
        /* false, wenn der Winkel zwischen der Richtung des Lichts
           und des Vektors zum Punkt groesser als der Blickwinkel ist */
        if(Math.acos(direction.normalized().dot(directionFrom(point).mul(-1))) > halfAngle) return false;
        
        Vector3 toPosition = position.sub(point);
        Ray r = new Ray(point, toPosition);
        Hit h = world.hit(r);
        
        // true, wenn keine Geometrie zwischen dem Punkt und der Lichtquelle liegt
        return (h == null || h.ray.at(h.t).sub(point).magnitude > position.sub(point).magnitude);
    }
    
    @Override
    public Vector3 directionFrom(Point3 point) {
        return position.sub(point).normalized();
    }
}
