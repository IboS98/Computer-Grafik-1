package material;

import geometry.Color;
import geometry.Hit;
import geometry.Sphere;
import light.Light;
import math.Point3;
import math.Vector3;
import raytracer.Ray;
import raytracer.World;

import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import math.Normal3;

/**
 * Refraktierendes Material
 */
public class TransparentMaterial extends Material {
    
    // Der Brechungsindex
    public final double indexOfRefraction;

    // Die Anzahl der Reflektionen
    public static final int ANZ_REFLEKTIONEN = 5;

    /**
     * Erstellt eine neue TransparentMaterial-Instanz
     *
     * @param indexOfRefraction Der Brechungsindex
     */
    public TransparentMaterial(double indexOfRefraction) {
        this.indexOfRefraction = indexOfRefraction;
    }

    @Override
    public Color colorFor(Hit hit, World world) {
        return colorFor(hit, world, ANZ_REFLEKTIONEN, false);
    }
    
    protected Color colorFor(Hit hit, World world, final int anzahlReflektionen, boolean insideTransparentGeometry) {
        
        Point3 p = hit.ray.at(hit.t);
        
        // Vektor zum Beobachter (-d)
        Vector3 e = hit.ray.o.sub(p).normalized();
        
        // Brechungsindizes
        double n1 = 1;
        double n2 = indexOfRefraction;
        
        // Indizes tauschen, wenn der Strahl die Geometrie verlaesst
        if(insideTransparentGeometry) {
            n1 = n2; n2 = 1;
        }
        
        // Berechnet Anteil der Reflektion (Schlicksche Approximation)
        double r0 = pow((n1 - n2) / (n1 + n2), 2);
        double reflection = r0 + (1 - r0) * pow((1 -  e.dot(hit.n)), 5);
        
        // Winkel zwischen der Normalen und e
        double phi1 = e.dot(hit.n);
        double phi2 = sqrt(1 - pow(n1/n2, 2) * (1 - pow(phi1, 2)));
        
        // Reflektierter Strahl (rd)
        Ray reflected = new Ray(p, e.reflectedOn(hit.n));
        
        // Refraktierter Strahl
        Ray refracted = new Ray(p, e.mul(-1).mul(n1/n2).sub(hit.n.mul(phi2 - n1/n2 * phi1)));
        
        // Totale Reflektion
        if(phi2 <= 0) {
            reflection = 1;
        }
        
        // Initialfarbe: schwarz
        Color c = new Color(0, 0, 0);
        Hit h;
        
        // Hit des reflektierten Strahls
        h = world.hit(reflected);
        
        // Prueft, ob die maximale Anzahl der Reflektionen erreicht ist und berechnet ggf. weiter
        if(h != null && anzahlReflektionen > 0) {
            Material m = h.geo.material;
            if(m instanceof ReflectiveMaterial) {
                c = c.add(((ReflectiveMaterial) m).colorFor(h, world, anzahlReflektionen-1).mul(reflection));
            }
            else if(m instanceof TransparentMaterial) {
                // insideTransparentGeometry: Strahl bleibt inner- oder ausserhalb der Geometrie
                c = c.add(((TransparentMaterial) m).colorFor(h, world, anzahlReflektionen-1, insideTransparentGeometry).mul(reflection));
            }
            else {
                c = c.add(m.colorFor(h, world).mul(reflection));
            }
        }
        
        // Hit des refraktierten Strahls
        h = world.hit(refracted);
        
        // Prueft, ob die maximale Anzahl der Refraktionen erreicht ist und berechnet ggf. weiter
        if(h != null && anzahlReflektionen > 0) {
            Material m = h.geo.material;
            if(m instanceof ReflectiveMaterial) {
                c = c.add(((ReflectiveMaterial) m).colorFor(h, world, anzahlReflektionen-1).mul(1 - reflection));
            }
            else if(m instanceof TransparentMaterial) {
                // !insideTransparentGeometry: Strahl betritt oder verlaesst die Geometrie
                c = c.add(((TransparentMaterial) m).colorFor(h, world, anzahlReflektionen-1, !insideTransparentGeometry).mul(1 - reflection));
            }
            else {
                c = c.add(m.colorFor(h, world).mul(1 - reflection));
            }
        }
        return c;
    }
}