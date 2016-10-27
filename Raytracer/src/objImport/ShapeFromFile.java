package objImport;

import geometry.Geometry;
import geometry.Hit;
import geometry.Triangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import material.Material;
import math.Normal3;
import math.Point3;
import raytracer.Ray;
import raytracer.World;

// Zusatzaufgabe

/**
 * Liest eine OBJ-Datei ein und erstellt eine Welt mit Dreiecken
 */
public class ShapeFromFile extends Geometry {
    
    private final List<Group> groups;
    private final World world;
    private final String file;
    
    // Liste der Eckpunkte
    public final List<Point3> eckpunkte;
    
    // Liste der Normelen

    public final List<Normal3> normalen;

    // Gruppen Zaehler
    private int groupCounter;

    // Anzahl der Dreiecke
    private int anzahlDreiecke;

    /**
     * Der Konstruktor der Klasse ShapeFromFile
     *
     * @param objFile Die Datei
     * @param m Das Material
     */
    public ShapeFromFile(String objFile, Material m) {
        super(m);
        groups = new ArrayList<>();
        groups.add(new Group(m));
        world = new World(null, null);
        file = objFile;

        eckpunkte = new ArrayList<>();
        normalen = new ArrayList<>();

        groupCounter = 0;
        anzahlDreiecke = 0;

        List<String> lines;
        try {
            lines = StringSaver.load(new File(file));
        }
        catch(FileNotFoundException ex) {
            System.out.println("Die Datei " + objFile + " wurde nicht gefunden");
            return;
        }

        System.out.println("Importiere Geometrien von " + file + ":");
        for (String str : lines) {
            str = str.trim();
            str = str.replaceAll(" +", " ");
            
            // wenn eine neue Gruppe beginnt
            if (str.matches("^g [^ ]+$")) {
                groupCounter++;
                groups.add(new Group(m));
            }
            // wenn ein Eckpunkt definiert ist
            if (str.matches("^v( [^ ]+){3}$")) {
                try {
                    String[] numbers = str.split(" ");
                    Point3 p = new Point3(new Double(numbers[1]), new Double(numbers[2]), new Double(numbers[3]));
                    eckpunkte.add(p);
                }
                catch(Exception ex) {
                    System.out.println(ex);
                    continue;
                }
            }
            // wenn eine Normale definiert ist
            if (str.matches("^vn( [^ ]+){3}$")) {
                try {
                    String[] numbers = str.split(" ");
                    normalen.add(new Normal3(new Double(numbers[1]), new Double(numbers[2]), new Double(numbers[3])));
                }
                catch(Exception ex) {
                    System.out.println(ex);
                    continue;
                }
            }
            // wenn ein Dreieck definiert ist
            if (str.matches("^f( [0-9/]+){3,}$")) {
                try {
                    // aktuelle Gruppe
                    Group g = groups.get(groupCounter);

                    // Daten der Punkte
                    String[] pointData = str.split(" ");

                    // Punkte der aktuellen Flaeche
                    List<Point3> points = new ArrayList<>();

                    // Iteriere ueber alle Punkte
                    for (int i = 1; i < pointData.length; i++) {
                        // Komponenten: v[, vn][, vt]
                        String[] components = pointData[i].split("/");
                        // Holt den Eckpunkt aus der Liste der aktuellen Gruppe
                        Point3 p = eckpunkte.get(new Integer(components[0]) - 1);
                        points.add(p);
                        g.eckpunkte.add(p);
                    }
                    // Trianguliere und fuege die Dreiecke der Gruppe hinzu
                    for (int i = 3; i <= points.size(); i++) {
                        g.AddDreieck(new Triangle(points.get(0), points.get(i - 2), points.get(i - 1), m));
                        anzahlDreiecke++;
                    }
                }
                catch(Exception ex) {
                    System.out.println(ex);
                    continue;
                }
            }
        }
        
        // Fuege der Welt alle Dreiecke hinzu
        for (Group g : groups) {
            for (Triangle d : g.dreiecke) {
                world.addGeometry(d);
            }
        }
        System.out.println("Fertig. " + anzahlDreiecke + " Dreiecke importiert.");
        System.out.println("Gruppen: " + groupCounter);
    }

    @Override
    public Hit hit(Ray r) {
        /*
         Der Strahl wird zuerst gegen die Begrenzungs-Geometrien getestet,
         da dieser sonst gegen alle Geometrien der Figur getestet werden muss.
        */
        World w = new World(null, null);
        for(Group g : groups) {
            if(g.hitsGroup(r)) {
                for(Triangle d : g.dreiecke) {
                    w.addGeometry(d);
                }
            }
        }
        return w.hit(r);
    }

    @Override
    public Geometry changeMaterial(Material m) {
        return new ShapeFromFile(file, m);
    }
}
