package geometry;

import material.Material;
import math.Point3;
import raytracer.Ray;
import raytracer.World;

/**
 * Implementiert eine Ebene
 */
public class AxisAlignedBox extends Geometry{

    // linker unterer hinterer Punkt
    public final Point3 luh;

    // rechter oberer vorderer Punkt
    public final Point3 rov;

    // Die Box aus Dreiecken
    private World box;

    /**
     * Der Konstruktor der AAB
     *
     * @param material Das Material
     */
    public AxisAlignedBox(Material material) {
        super(material);
        this.luh = new Point3(-0.5, -0.5, -0.5);
        this.rov = new Point3(0.5, 0.5, 0.5);
        initBox();
    }

    /**
     * Der erweiterte Konstruktor der AAB
     *
     * @param luh linker unterer hinterer Punkt
     * @param rov rechter oberer vorderer Punkt
     * @param material Das Material
     */
    public AxisAlignedBox(Point3 luh,  Point3 rov, Material material) {
        super(material);
        this.luh = luh;
        this.rov = rov;
        initBox();
    }

    /**
     * Die Erstellung einer Box
     */
    private void initBox() {
        box = new World(null, null);

        // Eckpunkte
        Point3 ruh = new Point3(rov.x, luh.y, luh.z);
        Point3 luv = new Point3(luh.x, luh.y, rov.z);
        Point3 ruv = new Point3(rov.x, luh.y, rov.z);
        Point3 loh = new Point3(luh.x, rov.y, luh.z);
        Point3 roh = new Point3(rov.x, rov.y, luh.z);
        Point3 lov = new Point3(luh.x, rov.y, rov.z);

        // Welt aus 12 Dreiecken -> Box
        box.addGeometry(new Triangle(luh, loh, luv,material));
        box.addGeometry(new Triangle(lov, loh, luv,material));
        box.addGeometry(new Triangle(ruh, roh, ruv,material));
        box.addGeometry(new Triangle(rov, roh, ruv,material));
        box.addGeometry(new Triangle(luh, ruh, luv,material));
        box.addGeometry(new Triangle(ruv, ruh, luv,material));
        box.addGeometry(new Triangle(loh, roh, lov,material));
        box.addGeometry(new Triangle(rov, roh, lov,material));
        box.addGeometry(new Triangle(luh, loh, ruh,material));
        box.addGeometry(new Triangle(roh, loh, ruh,material));
        box.addGeometry(new Triangle(luv, lov, ruv,material));
        box.addGeometry(new Triangle(rov, lov, ruv,material));
    }

    @Override
    public Hit hit(Ray r) {
        return box.hit(r);
    }

    @Override
    public Geometry changeMaterial(Material m) {
        return new AxisAlignedBox(m);
    }
}