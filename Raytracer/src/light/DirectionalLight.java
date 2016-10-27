package light;

import geometry.Color;
import geometry.Hit;
import math.Point3;
import math.Vector3;
import raytracer.Ray;
import raytracer.World;

/**
 * Das ist die Klasse Directional Light
 */
public class DirectionalLight extends Light {


     // Die Richtung eines Vektors
    public final Vector3 direction;

    /**
     * Das ist der Konstruktor der Klasse DrectionalLight
     *
     * @param color     Die Farbe
     * @param direction Die Richtung eines Vektors
     */
    public DirectionalLight(final Color color, final Vector3 direction, boolean castsShadows) {
        super(color, castsShadows);
        this.direction = direction;
    }
    
    @Override
    public boolean illuminates(Point3 point, World world) {
        if(!castsShadows) return true;
        Ray r = new Ray(point, direction.mul(-1));
        Hit h = world.hit(r);
        return (h == null);
    }
    
    @Override
    public Vector3 directionFrom(Point3 point) {
        return direction.mul(-1).normalized();
    }
}
