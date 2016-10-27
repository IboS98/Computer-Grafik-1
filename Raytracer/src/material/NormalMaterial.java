package material;

import geometry.Color;
import geometry.Hit;
import raytracer.World;

// Zusatzaufgabe

/**
 * Normalenbild eines Materials (Cel-Shading)
 */
public class NormalMaterial extends Material {
    
    /**
     * Erstellt eine neue NormalMaterial-Instanz
     */
    public NormalMaterial() {
    }

    @Override
    public Color colorFor(Hit hit, World world) {
        double r = hit.n.x;
        double g = hit.n.y;
        double b = hit.n.z;
        return new Color(r,g,b);
    }
}
