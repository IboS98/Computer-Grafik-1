package material;

import geometry.Color;
import geometry.Hit;
import raytracer.World;

/**
 * Material fuer eine Geometrie
 */
public abstract class Material {
    
    /**
     * Berechnet die Farbe eines Hit-Punktes in einer Welt
     *
     * @param hit der Hit
     * @param world die Welt
     * @return die darzustellende Farbe
     */
    public abstract Color colorFor(Hit hit, World world);
}