package geometry;

import java.util.ArrayList;
import java.util.List;
import material.Material;
import math.Normal3;
import math.Transformation;
import raytracer.Ray;
import raytracer.World;

/**
 * Implementiert eine transformierbare Sammlung von Geometrien
 */
public class Node extends Geometry{
    
    public final Transformation transformation;
    public final List<Geometry> geometryList;

    /**
     * Der Konstruktor der Klasse Node
     *
     * @param t Die Transformation
     * @param list Liste Geometrien
     */
    public Node(Transformation t, List<Geometry> list) {
        super(null);
        transformation = t;
        geometryList = list;
    }

    /**
     * Der erweiternde Konstruktor der Klasse Node
     *
     * @param t Die Transformation
     * @param geo Die Geometrie
     */
    public Node(Transformation t, Geometry geo) {
        super(null);
        transformation = t;
        geometryList = new ArrayList<>();
        geometryList.add(geo);
    }
    
    @Override
    public Hit hit(Ray r) {
        // Der transformierte Strahl
        Ray rayT = transformation.mul(r);
        
        /* Erstellt eine Welt aus der Liste der Geometrien
           und bestimmt den Hit mit dem transformierten Strahl */
        World w = new World(null, null);
        for(Geometry geo : geometryList) {
            w.addGeometry(geo);
        }
        
        Hit h = w.hit(rayT);
        if(h == null) return null;
        
        // Die transformierte Normale
        Normal3 normalT = transformation.mul(h.n);
        
        // Transformierter Hit
        return new Hit(h.t, r, h.geo, normalT);
    }
    
    @Override
    public Geometry changeMaterial(Material m) {
        List<Geometry> newList = new ArrayList<>();
        for(Geometry geo : geometryList) {
            newList.add(geo.changeMaterial(m));
        }
        return new Node(transformation, newList);
    }
}