package geometry;

import material.Material;
import math.Mat3x3;
import math.Normal3;
import math.Point3;
import math.Vector3;
import raytracer.Ray;

/**
 * Implementiert ein Dreieck
 */
public class Triangle extends Geometry{
    
    // Punkt A
    public final Point3 a;
    
    // Punkt B
    public final Point3 b;
    
    // Punkt C
    public final Point3 c;

    /**
     * Der Konstruktor der Klasse Triangle
     *
     * @param a Der Punkt A
     * @param b Der Punkt B
     * @param c Der Punkt C
     * @param material Das Material
     */
    public Triangle(Point3 a, Point3 b, Point3 c, Material material) {
        
        super(material);
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public Hit hit(Ray r) {
        
        //Schnittpunktformel Dreieck
        Mat3x3 mat = new Mat3x3(
            a.x - b.x, a.x - c.x, r.d.x,
            a.y - b.y, a.y - c.y, r.d.y,
            a.z - b.z, a.z - c.z, r.d.z
        );
        
        Vector3 v = new Vector3(a.x - r.o.x, a.y - r.o.y, a.z - r.o.z);
        
        Mat3x3 mat1 = mat.changeCol1(v);
        Mat3x3 mat2 = mat.changeCol2(v);
        Mat3x3 mat3 = mat.changeCol3(v);
        
        double beta = mat1.determinant / mat.determinant;
        double gamma = mat2.determinant / mat.determinant;
        double t = mat3.determinant / mat.determinant;
        
        // Prueft, ob der Punkt nicht hinter dem Strahl liegt
        if(t <= EPS) return null;
        
        // Prueft, ob der Schnittpunkt auf dem Dreieck liegt
        if(beta >= 0 && beta <= 1 && gamma >= 0 && gamma <= 1 && beta + gamma <= 1) {
        
            // Normale des Schnittpunktes
            Normal3 n;
            
            Vector3 ab = b.sub(a);
            Vector3 ac = c.sub(a);
            
            // Normale auf dem Dreieck
            n = ab.x(ac).normalized().asNormal();
        
            // Normale wird gespiegelt,wenn der Strahl von unten kommt
            if(n.dot(r.d.mul(-1)) < 0) {
                n = n.mul(-1);
            }
            return new Hit(t, r, this, n);
        }
        return null;
    }
    
    @Override
    public Geometry changeMaterial(Material m) {
        return new Triangle(a, b, c, m);
    }
}