package raytracer;

import geometry.Color;
import geometry.Geometry;
import geometry.Hit;
import light.Light;

import java.util.ArrayList;
import material.Material;

/**
 * Das ist die Klasse World
 */
public class World {
    
    // Liste aller Geometrien
    public final ArrayList<Geometry> koerper;
    
    // Liste aller Lichtquellen
    public final ArrayList<Light> lichtquellen;
    
    // Die Farbe der Welt
    public final Color bgcolor;
    
    // Die Farbe des ambienten Lichts
    public final Color ambient;

    /**
     * Das ist der Konstruktor der Klasse World
     *
     * @param bgcolor Die Farbe der Welt
     * @param ambient Die Farbe des ambienten Lichts
     */
    public World(Color bgcolor, Color ambient){
        koerper = new ArrayList<Geometry>();
        lichtquellen = new ArrayList<Light>();
        if(bgcolor == null) {
            this.bgcolor = new Color(0, 0, 0);
        }
        else {
            this.bgcolor = bgcolor;
        }
        if(ambient == null) {
            this.ambient = new Color(0, 0, 0);
        }
        else {
            this.ambient = ambient;
        }
    }

    /**
     * Fügt eine Geometrie hinzu
     *
     * @param g Die Geometrie
     */
    public void addGeometry(Geometry g) {
        koerper.add(g);
    }

    /**
     * Fügt eine Lichtquelle hinzi
     *
     * @param l Das Licht
     */
    public void addLight(Light l) {
        lichtquellen.add(l);
    }

    /**
     * Schnittpunktberechnung
     *
     * @param r der Strahl
     * @return Hit des kleinsten positiven t
     */
    public Hit hit(Ray r) {
        Hit ret = null;
        Hit i;
        for(Geometry g : koerper) {
            i = g.hit(r);
            if(i == null) continue;
            if(ret == null || (i.t > Geometry.EPS && i.t < ret.t)) {
                ret = i;
            }
        }
        return ret;
    }
    
    /**
     * Gibt die Welt mit einem anderen Material zurueck
     *
     * @param m Das neue Material
     * @return Die Welt mit dem neuen Material
     */
    public World changeMaterial(Material m) {
        World ret = new World(bgcolor, ambient);
        for(Geometry g : koerper) {
            ret.addGeometry(g.changeMaterial(m));
        }
        return ret;
    }
}
