package geometry;

import material.Material;
import raytracer.Ray;

/**
 * Abstrakte Klasse fuer geometrische Figuren
 */
public abstract class Geometry {
    
    /**
     * Epsilon-Wert zur Vermeidung von Nullvektoren
     */
    public static final double EPS = 0.00001;
    /**
     * Das Material der Geometrie
     */
    public final Material material;
    
    /**
     * Konstruktor fuer eine neue Geometrie
     *
     * @param material das Material der Geometrie
     */
    public Geometry(Material material) {
        this.material = material;
    }
    
    /**
     * Berechnet den Hit fuer einen Stahl mit dieser Geometrie
     *
     * @param r der Strahl
     * @return der Hit, falls der Strahl die Geometrie trifft; sonst null
     */
    public abstract Hit hit(Ray r);
    
    /**
     * Gibt die Geometrie mit einem anderen Material zurueck
     * Diese Methode wird fuer das Cel-Shading benoetigt
     *
     * @param m Das neue Material
     * @return Die Geometrie mit dem neuen Material
     */
    public abstract Geometry changeMaterial(Material m);
}