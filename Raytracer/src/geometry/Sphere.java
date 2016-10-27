package geometry;

import material.Material;
import math.Normal3;
import math.Point3;
import math.Vector3;
import raytracer.Ray;

/**
 * Implementiert eine Kugel
 */
public class Sphere extends Geometry{
    
    // Mittelpunkt der Kugel
    public final Point3 c;
    
    // Radius der Kugel
    public final double r;

    /**
     * Der Konstruktor der Klasse Sphere
     *
     * @param material Das Material
     */
    public Sphere(Material material) {
        
        super(material);
        this.c = new Point3(0, 0, 0);
        this.r = 1;
    }
    
    @Override
    public Hit hit(Ray r) {
        
        // Schnittpunktformel Kugel
        double aa = r.d.dot(r.d);
        double bb = r.d.dot(r.o.sub(c).mul(2));
        double cc = r.o.sub(c).dot(r.o.sub(c)) - (this.r * this.r);
        double w = bb*bb - 4*aa*cc;
        
        // Wurzel mit negativen Zahlen
        if(w < 0) return null;
        
        double t1 = (-bb + Math.sqrt(w)) / (2*aa);
        double t2 = (-bb - Math.sqrt(w)) / (2*aa);
        
        // t = kleinstes positives t
        double t;
        if(t1 > EPS && (t2 <= EPS || t1 <= t2)) {
            t = t1;
        }
        else if(t2 > EPS && (t1 <= EPS || t2 <= t1)) {
            t = t2;
        }
        else {
            return null;
        }
        
        // Normale des Schnittpunktes = Vektor vom Mittelpunkt zum Schnittpunkt
        Normal3 n = r.at(t).sub(c).normalized().asNormal();
        
        // Normale wird gespiegelt,wenn der Strahl von unten kommt
        if(n.dot(r.d.mul(-1)) < 0) {
            n = n.mul(-1);
        }
        return new Hit(t, r, this, n);
    }
    
    @Override
    public Geometry changeMaterial(Material m) {
        return new Sphere(m);
    }
}