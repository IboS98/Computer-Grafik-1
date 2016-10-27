package material;

import geometry.Color;
import geometry.Hit;
import light.Light;
import math.Point3;
import math.Vector3;
import raytracer.World;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;

// Zusatzaufgabe

/**
 * Phong-Material mit abgestuftem Schatten (Cel-Shading)
 */
public class ShadedMaterial extends Material {
    
    public final Color diffuse;
    public final Color specular;
    public final int exponent;

    /**
     * Erstellt eine neue ShadedMaterial-Instanz
     * @param diffuse die diffuse Farbe des Materials
     * @param specular
     * @param exponent Groesse des Glanzpunktes. Der groesser der Exponent ist, umso kleiner erscheint dieser
     */
    public ShadedMaterial(Color diffuse, Color specular, int exponent) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.exponent = exponent;
    }
    
    @Override
    public Color colorFor(Hit hit, World world) {
        Point3 p = hit.ray.at(hit.t);
        
        // Ambientes Licht wird gesetzt
        Color c = diffuse.mul(world.ambient);
        
        for(Light li : world.lichtquellen) {
            
            // Vektor zur Lichtquelle
            Vector3 l = li.directionFrom(p).normalized();
            
            // Reflektierter Strahl
            Vector3 rl = l.reflectedOn(hit.n).normalized();
            
            // Vektor zum Beobachter (Kamera)
            Vector3 e = hit.ray.o.sub(p).normalized();
            
            // Prueft, ob der Punkt angestrahlt wird
            if(li.illuminates(p, world)) {
                
                // Phong-Formel
                c = c.add(diffuse.mul(li.color).mul(Math.max(0,rundeWinkel(hit.n.dot(l)))));
                c = c.add(specular.mul(li.color).mul(Math.pow(Math.max(0,rundeWinkel(e.dot(rl))), exponent)));
            }
        }
        return c;
    }
    
    /**
     * Rundet einen Winkel
     *
     * @param cosX Der Cosinus des Winkels
     * @return Der Cosinus des gerundeten Winkels
     */
    private double rundeWinkel(double cosX) {
        
        // Der Winkel, auf den gerundet wird
        double faktor = PI/12;
        
        double winkel = acos(cosX);
        int rWert = (int) (winkel / faktor);
        return cos(rWert * faktor);
    }
}