package material;

import geometry.Color;
import geometry.Hit;
import raytracer.World;

/**
 * einfarbiges Material
 */
public class SingleColorMaterial extends Material {
    
    public final Color color;
    
    /**
     * Erstellt eine neue SingleColorMaterial-Instanz
     *
     * @param color die Farbe des Materials
     */
    public SingleColorMaterial(Color color) {
        this.color = color;
    }
    
    @Override
    public Color colorFor(Hit hit, World world) {
        return color;
    }
}