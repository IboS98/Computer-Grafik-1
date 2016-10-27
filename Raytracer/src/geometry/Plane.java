package geometry;

import material.Material;
import math.Normal3;
import math.Point3;
import raytracer.Ray;

/**
 * Implementiert eine Ebene
 */
public class Plane extends Geometry{
    
    // ein Punkt auf der Ebene
    public final Point3 a;
    
    // der Normalvektor der Ebene
    public final Normal3 n;

    /**
     * Der Konstruktor der Klasse Plane
     *
     * @param material Das Material
     */
    public Plane(Material material) {
        
        super(material);
        this.a = new Point3(0, 0, 0);
        this.n = new Normal3(0, 1, 0);
    }
    
    @Override
    public Hit hit(Ray r) {
        final double t;
        
        try{
            t = (n.dot(a.asVector()) - n.dot(r.o.asVector())) / n.dot(r.d);
        }
        // Division durch 0 (= Strahl parallel zur Ebene)
        catch(ArithmeticException ex) {
            return null;
        }
        
        // Prueft, ob der Punkt nicht hinter dem Strahl liegt
        if(t <= EPS) return null;
        
        // Normale des Schnittpunktes = Normale der Ebene
        Normal3 n = this.n;
        
        // Normale wird gespiegelt,wenn der Strahl von unten kommt
        if(n.dot(r.d.mul(-1)) < 0) {
            n = n.mul(-1);
        }
        
        return new Hit(t, r, this, n);
    }
    
    @Override
    public Geometry changeMaterial(Material m) {
        return new Plane(m);
    }
}