package objImport;

import geometry.AxisAlignedBox;
import geometry.Geometry;
import geometry.Triangle;
import java.util.ArrayList;
import java.util.List;
import material.Material;
import math.Normal3;
import math.Point3;
import raytracer.Ray;

// Zusatzaufgabe

/**
 * Beschreibt eine Gruppierung von Dreiecken einer OBJ-Datei
 */
public class Group {
    // Liste von Punkten der Gruppe
    public final List<Point3> eckpunkte;
    // Liste von Dreiecken der Gruppe
    public final List<Triangle> dreiecke;
    // Das Material der Gruppe
    public final Material material;
    
    /*
     Geometrien zur Begrenzung der in der OBJ-Datei gespeicherten Figur
    
     Beim Rendern einer solchen Figur werden die Strahlen zuerst gegen
     diese Geometrien getestet, um Rechenzeit zu sparen.
    */
    public Geometry aussenBox;

    /**
     * Erzeugt eine neue Group-Instanz
     *
     * @param m Das Material
     */
    public Group(Material m) {
        dreiecke = new ArrayList<>();
        eckpunkte = new ArrayList<>();
        material = m;
    }
    
    /**
     * Fuegt der Gruppe ein neues Dreieck hinzu
     *
     * @param d Das neue Dreieck
     */
    public void AddDreieck(Triangle d) {
        dreiecke.add(d);
    }
    
    /**
     * Prueft, ob ein Strahl potentiell die Gruppe trifft
     *
     * @param r Der Strahl
     * @return true, wenn der Strahl potentiell die Gruppe trifft
     */
    public boolean hitsGroup(Ray r) {
        if(eckpunkte.size() < 1) return false;
        if(aussenBox == null) {
        
            // Werte fuer die Eingrenzung der Geometrie
            double minX = eckpunkte.get(0).x;
            double maxX = eckpunkte.get(0).x;
            double minY = eckpunkte.get(0).y;
            double maxY = eckpunkte.get(0).y;
            double minZ = eckpunkte.get(0).z;
            double maxZ = eckpunkte.get(0).z;
            
            // Erzeugung der Aussenbox
            for (Point3 p : eckpunkte) {
                if(p.x < minX) minX = p.x;
                if(p.x > maxX) maxX = p.x;
                if(p.y < minY) minY = p.y;
                if(p.y > maxY) maxY = p.y;
                if(p.z < minZ) minZ = p.z;
                if(p.z > maxZ) maxZ = p.z;
            }
            aussenBox = new AxisAlignedBox(new Point3(minX, minY, minZ), new Point3(maxX, maxY, maxZ), material);
        }
        return aussenBox.hit(r) != null;
    }
}
