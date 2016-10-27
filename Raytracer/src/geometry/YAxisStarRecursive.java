package geometry;

import java.util.ArrayList;
import java.util.List;
import material.Material;
import math.Normal3;
import math.Point3;
import math.Transformation;
import raytracer.Ray;
import raytracer.World;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

/**
 * Implementiert einen Stern, der auf der Y-Achse liegt
 * Eine Spitze zeigt immer in Richtung X-Achse
 * Der Abstand eines Zacken zur Mitte ist immer 1
 * Die Dicke des Sterns ist immer 1
 */
public class YAxisStarRecursive extends Geometry {
    
    /**
     * Anzahl der Zacken
     */
    public final int jags;
    /**
     * Verhaeltnis zwischen Innen- und Aussenkreis
     */
    public final double ratio;
    
    // Der Stern aus Dreiecken
    private final World stars;

    /**
     * Der Konstruktor der Klasse YAxisStarRecursive
     *
     * @param jags Die Zacken
     * @param ratio Verhaeltnis zwischen Innen und Aussenkreis
     * @param material Das Material
     * @param recursions Die Rekursion
     */
    public YAxisStarRecursive(int jags, double ratio, Material material, int recursions) {
        this(jags, ratio, material, recursions, -1);
    }

    /**
     * Erweiterung des Konstruktors YAxisStarRecursive
     *
     * @param jags Die Zacken
     * @param ratio Verhaeltnis zwischen Innen und Aussenkreis
     * @param material Das Material
     * @param recursions Die Rekursion
     * @param ii Die Anzahl
     */
    private YAxisStarRecursive(int jags, double ratio, Material material, int recursions, int ii) {
        super(material);
        this.jags = jags;
        this.ratio = ratio;
        this.stars = new World(null, null);
        
        YAxisStar main = new YAxisStar(jags, ratio, material);
        double scaleFactor = 0.4;
        
        stars.addGeometry(main);
        
        if( recursions < 1) return;
        for(int i = 0; i < jags; i++) {
            Point3 p = main.outerPoints[i];
            if(i != ii) {
                if(recursions < 2)
                    stars.addGeometry(new Node(new Transformation().translation(p.x, p.y, p.z).scale(scaleFactor, scaleFactor, scaleFactor).rotateY(PI), main));
                else
                    stars.addGeometry(new Node(new Transformation().translation(p.x, p.y, p.z).scale(scaleFactor, scaleFactor, scaleFactor).rotateY(PI), new YAxisStarRecursive(jags, ratio, material, recursions-1, i)));
            }
        }
    }

    @Override
    public Hit hit(Ray r) {
        return stars.hit(r);
    }

    @Override
    public Geometry changeMaterial(Material m) {
        return new YAxisStar(jags, ratio, m);
    }
}