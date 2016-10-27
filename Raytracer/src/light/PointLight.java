package light;

import geometry.Color;
import geometry.Hit;
import math.Point3;
import math.Vector3;
import raytracer.Ray;
import raytracer.World;

/**
 * Das ist die Klasse PointLight
 */
public class PointLight extends Light{

    /**
     * Die Position eines Punktes
     */
    public final Point3 position;

    /**
     * Das ist der Konstruktor der Klasse PointLight
     *
     * @param color Die Farbe
     * @param position Die Position eines Punktes
     */
    public PointLight(final Color color, final Point3 position, boolean castsShadows){
        super(color, castsShadows);
        this.position = position;
    }
    
    @Override
    public boolean illuminates(Point3 point, World world) {
        if(!castsShadows) return true;
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
