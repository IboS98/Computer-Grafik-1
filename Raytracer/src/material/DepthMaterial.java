package material;

import geometry.Color;
import geometry.Hit;
import raytracer.World;

// Zusatzaufgabe

/**
 * Tiefenbild eines Materials (Cel-Shading)
 */
public class DepthMaterial extends Material {
    
    final double faktor;
    /**
     * Erstellt eine neue DepthMaterial-Instanz
     * @param faktor Je groesser dieser Wert ist, umso schneller steigt die Helligkeit des Lichts
     */
    public DepthMaterial(double faktor) {
        this.faktor = faktor;
    }

    @Override
    public Color colorFor(Hit hit, World world) {
        double x = hit.t * faktor;
        return new Color(x,x,x);
    }
}
