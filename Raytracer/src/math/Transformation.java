package math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import raytracer.Ray;

/**
 * Klasee fuer die Transformationen
 */
public class Transformation {
    
    public static final Mat4x4 IDENTITY = new Mat4x4(1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1);
    // Transformationsmatrix
    public final Mat4x4 m;
    // Inverse Transformationsmatrix
    public final Mat4x4 i;
    
    /**
     * Erzeugt eine neue Transformation
     */
    public Transformation() {
        m = IDENTITY;
        i = IDENTITY;
    }

    /**
     * Der Konstruktor der Klasse Transformation
     *
     * @param m Matrix 1
     * @param i Matrix 2
     */
    private Transformation(Mat4x4 m, Mat4x4 i) {
        this.m = m;
        this.i = i;
    }
    
    /**
     * Translation
     * 
     * @param x Verschiebung in x-Richtung
     * @param y Verschiebung in y-Richtung
     * @param z Verschiebung in z-Richtung
     * 
     * @return neues Transformation-Objekt
     */
    public Transformation translation(double x, double y, double z) {
        Mat4x4 m = new Mat4x4(1,0,0,x, 0,1,0,y, 0,0,1,z, 0,0,0,1);
        Mat4x4 i = new Mat4x4(1,0,0,-x, 0,1,0,-y, 0,0,1,-z, 0,0,0,1);
        
        return new Transformation(this.m.mul(m), i.mul(this.i));
    }
    
    /**
     * Skalierung
     * 
     * @param x Skalierung in x-Richtung
     * @param y Skalierung in y-Richtung
     * @param z Skalierung in z-Richtung
     * 
     * @return neues Transformation-Objekt
     */
    public Transformation scale(double x, double y, double z) {
        Mat4x4 m = new Mat4x4(x,0,0,0, 0,y,0,0, 0,0,z,0, 0,0,0,1);
        Mat4x4 i = new Mat4x4(1/x,0,0,0, 0,1/y,0,0, 0,0,1/z,0, 0,0,0,1);
        
        return new Transformation(this.m.mul(m), i.mul(this.i));
    }
    
    /**
     * Rotation um die x-Achse
     * 
     * @param w Der Rotationswinkel
     * 
     * @return neues Transformation-Objekt
     */
    public Transformation rotateX(double w) {
        Mat4x4 m = new Mat4x4(1,0,0,0, 0,cos(w),-sin(w),0, 0,sin(w),cos(w),0, 0,0,0,1);
        Mat4x4 i = new Mat4x4(1,0,0,0, 0,cos(w),sin(w),0, 0,-sin(w),cos(w),0, 0,0,0,1);
        
        return new Transformation(this.m.mul(m), i.mul(this.i));
    }
    
    /**
     * Rotation um die y-Achse
     * 
     * @param w Der Rotationswinkel
     * 
     * @return neues Transformation-Objekt
     */
    public Transformation rotateY(double w) {
        Mat4x4 m = new Mat4x4(cos(w),0,sin(w),0, 0,1,0,0, -sin(w),0,cos(w),0, 0,0,0,1);
        Mat4x4 i = new Mat4x4(cos(w),0,-sin(w),0, 0,1,0,0, sin(w),0,cos(w),0, 0,0,0,1);
        
        return new Transformation(this.m.mul(m), i.mul(this.i));
    }
    
    /**
     * Rotation um die z-Achse
     * 
     * @param w Der Rotationswinkel
     * 
     * @return neues Transformation-Objekt
     */
    public Transformation rotateZ(double w) {
        Mat4x4 m = new Mat4x4(cos(w), -sin(w),0,0, sin(w), cos(w),0,0, 0,0,1,0, 0,0,0,1);
        Mat4x4 i = new Mat4x4(cos(w), sin(w),0,0, -sin(w), cos(w),0,0, 0,0,1,0, 0,0,0,1);
        
        return new Transformation(this.m.mul(m), i.mul(this.i));
    }
    
    /**
     * Multiplikation der Transformationsmatrix mit einem Strahl
     * 
     * @param r Der Strahl
     * @return Der transformierte Strahl
     */
    public Ray mul(Ray r) {
        return new Ray(i.mul(r.o), i.mul(r.d));
    }
    
    /**
     * Multiplikation der Transformationsmatrix mit einer Normalen
     * 
     * @param n Die Normale
     * @return Die transformierte Normale
     */
    public Normal3 mul(Normal3 n) {
        return i.transpose().mul(n);
    }
}
