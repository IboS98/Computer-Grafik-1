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
public class YAxisStar extends Geometry {
    
    /**
     * Anzahl der Zacken
     */
    public final int jags;
    /**
     * Verhaeltnis zwischen Innen- und Aussenkreis
     */
    public final double ratio;
    
    // Hinterer und Vorderer Mittelpunkt
    private static final Point3 LOWER = new Point3(0, -0.5, 0);
    private static final Point3 UPPER = new Point3(0, 0.5, 0);
    
    private final Geometry aussenBox;
    
    protected final Point3[] outerPoints;
    
    // Der Stern aus Dreiecken
    private final World star;
    
    public YAxisStar(int jags, double ratio, Material material) {
        super(material);
        this.jags = jags;
        this.ratio = ratio;
        this.aussenBox = new AxisAlignedBox(new Point3(-1, LOWER.y, -1), new Point3(1, UPPER.y, 1), material);
        outerPoints = new Point3[jags];
        Point3[] innerPoints = new Point3[jags];
        
        // Berechnung der Punkte
        for(int i = 0; i < jags; i++) {
            outerPoints[i] = new Point3(cos(i*2*PI/jags), 0, sin(i*2*PI/jags));
            innerPoints[i] = new Point3(ratio*cos(i*2*PI/jags + PI/jags), 0, ratio*sin(i*2*PI/jags + PI/jags));
        }
        
        // Erzeugung der Dreiecke 
        star = new World(null, null);
        for(int i = 0; i < jags; i++) {
            star.addGeometry(new Triangle(outerPoints[i], LOWER, innerPoints[i], material));
            star.addGeometry(new Triangle(outerPoints[(i+1)%jags], LOWER, innerPoints[i], material));
            star.addGeometry(new Triangle(outerPoints[i], UPPER, innerPoints[i], material));
            star.addGeometry(new Triangle(outerPoints[(i+1)%jags], UPPER, innerPoints[i], material));
        }
    }

    @Override
    public Hit hit(Ray r) {
        if(aussenBox.hit(r) == null) return null;
        return star.hit(r);
    }

    @Override
    public Geometry changeMaterial(Material m) {
        return new YAxisStar(jags, ratio, m);
    }
}