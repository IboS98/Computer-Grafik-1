package light;

import geometry.Color;
import math.Point3;
import math.Vector3;
import raytracer.World;

/**
 * Das ist die abstrakte Klasse Light
 */
public abstract class Light {

    // Die Farbe
    public final Color color;
    
    // Gibt an, ob das Licht einen Schatten wirft
    public final boolean castsShadows;

    /**
     * Das ist der Konstruktor der Klasse Light
     *
     * @param color Die Farbe
     */
    public Light(final Color color, boolean castsShadows) {
        this.color = color;
        this.castsShadows = castsShadows;
    }

    /**
     * Prueft, ob ein Punkt von der Lichtquelle angestrahlt wird
     *
     * @param point Der Punkt
     * @return true, wenn der Punkt angestrahlt wird
     */
    public abstract boolean illuminates(final Point3 point, World world);

    /**
     * Berechnet den Vektor zur Lichtquelle
     *
     * @param point Der Punkt
     * @return Der Vektor zur Lichtquelle
     */
    public abstract Vector3 directionFrom(final Point3 point);
}
