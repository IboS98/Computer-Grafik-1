package material;

import geometry.Color;
import geometry.Hit;
import light.Light;
import math.Point3;
import math.Vector3;
import raytracer.Ray;
import raytracer.World;

import static java.lang.Math.max;
import static java.lang.Math.pow;

/**
 * Reflektierendes Phong-Material
 */
public class ReflectiveMaterial extends Material {

    public final Color diffuse;
    public final Color specular;
    public final int exponent;
    public final Color reflection;

    // Die Anzahl der Reflektionen
    public static final int ANZ_REFLEKTIONEN = 5;

    /**
     * Erstellt eine neue ReflectiveMaterial-Instanz
     *
     * @param diffuse die diffuse Farbe des Materials
     * @param specular Die Farbe des Glanzpunktes
     * @param exponent Groesse des Glanzpunktes. Der groesser der Exponent ist, umso kleiner erscheint dieser
     * @param reflection Die Farbe der Reflektion
     */
    public ReflectiveMaterial(Color diffuse, Color specular, int exponent, Color reflection) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.exponent = exponent;
        this.reflection = reflection;
    }

    @Override
    public Color colorFor(Hit hit, World world) {
        return colorFor(hit, world, ANZ_REFLEKTIONEN);
    }
    
    protected Color colorFor(Hit hit, World world, final int anzahlReflektionen) {
        Point3 p = hit.ray.at(hit.t);
        
        // Ambientes Licht wird gesetzt
        Color c = diffuse.mul(world.ambient);
            
        // Vektor zum Beobachter (Kamera)
        Vector3 e = hit.ray.o.sub(p).normalized();
        
        for(Light li : world.lichtquellen) {
            
            // Vektor zur Lichtquelle
            Vector3 l = li.directionFrom(p).normalized();
            
            // Reflektierter Strahl
            Vector3 rl = l.reflectedOn(hit.n).normalized();
            
            // Prueft, ob der Punkt angestrahlt wird
            if(li.illuminates(p, world)) {
            
                // Phong-Formel
                c = c.add(diffuse.mul(li.color).mul(max(0,hit.n.dot(l))));
                c = c.add(specular.mul(li.color).mul(pow(max(0,e.dot(rl)), exponent)));
            }
        }
        
        // Reflektierter Strahl
        Ray reflected = new Ray(p, e.reflectedOn(hit.n).normalized());
        
        // Hit des reflektierten Strahls
        Hit h = world.hit(reflected);
        
        // Prueft, ob die maximale Anzahl der Reflektionen erreicht ist und berechnet ggf. weiter
        if(h != null && anzahlReflektionen > 0) {
            Material m = h.geo.material;
            if(m instanceof ReflectiveMaterial) {
                c = c.add(((ReflectiveMaterial) m).colorFor(h, world, anzahlReflektionen-1).mul(reflection));
            }
            else if(m instanceof TransparentMaterial) {
                c = c.add(((TransparentMaterial) m).colorFor(h, world, anzahlReflektionen-1, false).mul(reflection));
            }
            else {
                c = c.add(m.colorFor(h, world).mul(reflection));
            }
        }
        return c;
    }
}