package material;

import geometry.Color;
import geometry.Hit;
import light.DirectionalLight;
import light.Light;
import light.PointLight;
import light.SpotLight;
import math.Point3;
import math.Vector3;
import raytracer.World;

/**
 * Material mit Lambert-Beleuchtung
 */
public class LambertMaterial extends Material {
    
    public final Color color;
    
    /**
     * Erstellt eine neue LambertMaterial-Instanz
     *
     * @param color die Farbe des Materials
     */
    public LambertMaterial(Color color) {
        this.color = color;
    }
    
    @Override
    public Color colorFor(Hit hit, World world) {
        
        // Der Schnittpunkt
        Point3 p = hit.ray.at(hit.t);
        
        // Ambientes Licht wird gesetzt
        Color c = color.mul(world.ambient);
        
        for(Light li : world.lichtquellen) {
            
            // Vektor zur Lichtquelle
            Vector3 l = li.directionFrom(p);
            
            // Prueft, ob der Punkt angestrahlt wird
            if(li.illuminates(p, world)) {
                
                // Lambert-Formel
                c = c.add(color.mul(li.color).mul(Math.max(0,hit.n.dot(l))));
            }
        }
        return c;
    }
}