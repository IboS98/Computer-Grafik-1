package raytracer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import camera.Camera;
import camera.FishEyeCamera;
import camera.OrthographicCamera;
import camera.PerspectiveCamera;
import geometry.AxisAlignedBox;
import geometry.Color;
import geometry.Geometry;
import geometry.Hit;
import geometry.Node;
import geometry.Plane;
import geometry.Sphere;
import geometry.Triangle;
import geometry.YAxisStar;
import imageViewer.ImageSaver;
import light.DirectionalLight;
import light.PointLight;
import light.SpotLight;
import material.DepthMaterial;
import material.LambertMaterial;
import material.NormalMaterial;
import material.PhongMaterial;
import material.ReflectiveMaterial;
import material.ShadedMaterial;
import material.SingleColorMaterial;
import material.TransparentMaterial;
import math.Normal3;
import math.Point3;
import math.Vector3;

import static java.lang.Math.abs;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;
import material.Material;
import math.Transformation;
import objImport.ShapeFromFile;

/**
 * Das ist die Klasse Raytracer
 */
public class Raytracer extends Canvas {

    /**
     * Legt die Tiefe des Fensters fest
     */
    public static final int WIDTH = 640;

    /**
     * Legt die Hoehe des Fenster fest
     */
    public static final int HEIGHT = 480;

    /**
     * Objekt fuer das zu speichernde Bild
     */
    private BufferedImage bImage;

    /**
     * Die Kamera fuer den Raytracer
     */
    private final Camera camera;

    /**
     * Die Welt
     */
    private final World world;

    /**
     * Der Konstruktor der Klasse Raytracer
     *
     * @param c Die Kamera
     * @param w Die Welt
     */
    public Raytracer(final Camera c, final World w, final String titel) {
        camera = c;
        world = w;
        bImage = null;
        initialize(titel);
    }

    /**
     * Die Erweiterung des Konstruktors
     *
     * @param img Die BufferedImage
     * @param titel Der Titel
     */
    public Raytracer(final BufferedImage img, final String titel) {
        camera = null;
        world = null;
        bImage = img;
        initialize(titel);
    }

    /**
     * Initialisiert das Fenster
     *
     * @param titel Der Titel
     */
    private void initialize(String titel) {

        // Erstellt ein neues Fenster
        final JFrame window = new JFrame();

        // Fuegt dem Fenster das Canvas hinzu
        window.add(this);

        // Setzt den Titel
        window.setTitle(titel);

        // Erstellt eine JMenuBar
        final JMenuBar menu = new JMenuBar();

        // Erstellt eine JMenu-Datei
        final JMenu save = new JMenu("Datei");
        menu.add(save);

        // Erstellt ein JMenuItem Speichern
        final JMenuItem saveitem = new JMenuItem("Speichern");
        saveitem.addActionListener(new Raytracer.AL());
        save.add(saveitem);

        // Setzt die JMenuBar am PAGE_START des Fensters
        window.add(menu, BorderLayout.PAGE_START);

        // Schließt das Fenster
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Bestimmt die Groesse des Fensters
        window.setSize(WIDTH, HEIGHT);

        // Macht den Inhalt des ImagePainters sichtbar
        window.setVisible(true);
    }

    /**
     * Repraesentiert die Klasse AL welches einen ActionListener implementiert
     * Es soll einen Speicherdialog oeffnen
     *
     * @exception IOException Wenn eine Datenuebertragung einer Datei nicht hergestellt werden kann
     */
    private class AL implements ActionListener {
        @Override
        public final void actionPerformed(final ActionEvent e) {
            try {
                new ImageSaver(bImage);
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Die Methode Paint
     *
     * @param g Die Grafik
     */
    @Override
    public void paint(final Graphics g) {
        if(bImage == null) {
            // Ein BufferedImage holt sich die Breite und die Höhe der Grafik, dabei wird es auf den Typ RGB gesetzt.
            this.bImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            
            renderImage(bImage, camera, world);
        }
        g.drawImage(this.bImage, 0, 0, null);
    }

    /**
     * Rendert die zu erzeugende Szene
     *
     * @param img Die BufferedImage
     * @param c Die Camera
     * @param w Die Welt
     */
    private static void renderImage(BufferedImage img, Camera c, World w) {

        // Ein Raster wird für die Grafik ertellt
        final WritableRaster raster = img.getRaster();

        // Diese For-Schleife setzt die Farben der einzelnen Pixel fest
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                
                // Setze Default-Farbe -> schwarz
                double[] farbe = new double[3];
                
                // Erzeuge fuer jeden Pixel einen Strahl
                Ray r = c.rayFor(img.getWidth(), img.getHeight(), x, y);
                if(r == null) {
                }
                else {
                    Hit h = w.hit(r);
                    
                    // Setze die Farbe der getroffenen Geometrie
                    if (h != null) {
                        Color col = h.geo.material.colorFor(h, w);
                        farbe[0] = 255 * col.r;
                        farbe[1] = 255 * col.g;
                        farbe[2] = 255 * col.b;
                    }
                    else {
                        farbe[0] = 255 * w.bgcolor.r;
                        farbe[1] = 255 * w.bgcolor.g;
                        farbe[2] = 255 * w.bgcolor.b;
                    }
                }
                
                // Setze den Pixel an die richtige Position
                raster.setPixel(x, HEIGHT - 1 - y, farbe);
            }
        }
    }

    /**
     * Erzeugt eine celShading Szene
     *
     * @param c Die Kamera
     * @param w Die Welt
     */
    public static void celShading(Camera c, World w) {
        double tiefenFaktor = 0.05;
        double kantenToleranz = 16;
        int farbRundungswert = 32;
        
        // Rendert das eigentliche Bild
        BufferedImage normal = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        renderImage(normal, c, w);
        
        // Rendert das Normalenbild
        BufferedImage normalen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        renderImage(normalen, c, w.changeMaterial(new NormalMaterial()));
        new Raytracer(normalen, "Normalen");
        
        // Rendert das Tiefenbild
        BufferedImage tiefen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        renderImage(tiefen, c, w.changeMaterial(new DepthMaterial(tiefenFaktor)));
        new Raytracer(tiefen, "Tiefen");
        
        // Erstellt ein Kantenbild aus dem Normalen- und dem Tiefenbild und mischt dieses mit dem eigentlichen Bild
        BufferedImage kanten = kantenBild(normalen, tiefen, kantenToleranz);
        BufferedImage celShaded = mischeBilder(kanten, normal);
        
        // Zeichnet das vollstaendige Bild
        new Raytracer(celShaded, "Cel-Shading");
    }
    
    /**
     * Erzeugt ein Kantenbild aus einem Normalenbild und einem Tiefenbild
     *
     * @param nBild Das Normalenbild
     * @param tBild Das Tiefenbild
     * @return Das Kantenbild
     */
    private static BufferedImage kantenBild(BufferedImage nBild, BufferedImage tBild, double toleranz) {
        return mischeBilder(kantenBild(nBild, toleranz*8), kantenBild(tBild, toleranz));
    }
    
    /**
     * Erzeugt ein Kantenbild aus einem Bild
     *
     * @param bild Das Bild
     * @return Das Kantenbild
     */
    private static BufferedImage kantenBild(BufferedImage bild, double toleranz) {
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        final WritableRaster rasterB = bild.getRaster();
        final WritableRaster rasterR = ret.getRaster();
        
        double[] schwarz = {0, 0, 0};
        double[] weiss = {255, 255, 255};
        
        // Initialisiert alle Pixel mit weiss
        for (int x = 0; x < ret.getWidth(); x++) {
            for (int y = 0; y < ret.getHeight(); y++) {
                rasterR.setPixel(x, y, weiss);
            }
        }
        
        // Zeichnet die Kanten
        for (int x = 0; x < ret.getWidth(); x++) {
            for (int y = 0; y < ret.getHeight(); y++) {
                double[] farbeA = rasterB.getPixel(x, y, new double[3]);
                
                // Vergleicht nebeneinander liegende Pixel und zeichnet die Kanten
                if(x < ret.getWidth()-1) {
                    double[] farbeB = rasterB.getPixel(x+1, y, new double[3]);
                    if(farbDifferenz(farbeA, farbeB) >= toleranz) {
                        rasterR.setPixel(x, y, schwarz);
                        rasterR.setPixel(x+1, y, schwarz);
                    }
                }
                
                // Vergleicht untereinander liegende Pixel und zeichnet die Kanten
                if(y < ret.getHeight()-1) {
                    double[] farbeC = rasterB.getPixel(x, y+1, new double[3]);
                    if(farbDifferenz(farbeA, farbeC) >= toleranz) {
                        rasterR.setPixel(x, y, schwarz);
                        rasterR.setPixel(x, y+1, schwarz);
                    }
                }
            }
        }
        new Raytracer(ret, "Kanten");
        return ret;
    }
    
    /**
     * Berechnet die Differenzen der RGB-Werte zweier Farben und addiert diese
     *
     * @param a Farbe A als Double-Array
     * @param b Farbe B als Double-Array
     * @return Die Farbdifferenz
     */
    private static double farbDifferenz(double[] a, double[] b) {
        double d = 0;
        d += abs(a[0] - b[0]);
        d += abs(a[1] - b[1]);
        d += abs(a[2] - b[2]);
        return d;
    }
    
    /**
     * Mischt zwei Bilder
     *
     * @param a Bild 1
     * @param b Bild 2
     * @return das gemischte Bild
     */
    private static BufferedImage mischeBilder(BufferedImage a, BufferedImage b) {
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        final WritableRaster rasterA = a.getRaster();
        final WritableRaster rasterB = b.getRaster();
        final WritableRaster rasterC = ret.getRaster();

        // Diese For-Schleife setzt die Farben der einzelnen Pixel fest.
        for (int x = 0; x < ret.getWidth(); x++) {
            for (int y = 0; y < ret.getHeight(); y++) {
                
                double[] farbeA = rasterA.getPixel(x, y, new double[3]);
                double[] farbeB = rasterB.getPixel(x, y, new double[3]);
                
                double cr = farbeA[0] * farbeB[0] / 255;
                double cg = farbeA[1] * farbeB[1] / 255;
                double cb = farbeA[2] * farbeB[2] / 255;
                
                double[] farbeC = {cr, cg, cb};
                rasterC.setPixel(x, y, farbeC);
            }
        }
        return ret;
    }
    
    /**
     * Die Mainmethode des Raytracers
     * Erstellt mehrere Raytracer-Instanzen zum Anzeigen der Grafiken.
     *
     * @throws IOException Wenn eine Datenuebertragung einer Datei nicht hergestellt werden kann
     */
    public static void main(final String[] args) throws IOException {

        World w;
        Camera c;
        
        // Farben fuer die Geometrien
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        Color blue = new Color(0, 0, 1);
        Color cyan = new Color(0, 1, 1);
        Color magenta = new Color(1, 0, 1);
        Color yellow = new Color(1, 1, 0);
        Color black = new Color(0, 0, 0);
        Color white = new Color(1, 1, 1);
        Color gray = new Color(0.3, 0.3, 0.3);
        Color skyblue = new Color(0, 0.5, 1);
        
        // Cel-Shading
        c = new PerspectiveCamera(new Point3(4, 2.5, 4), new Vector3(-1, -0.25, -1), new Vector3(0, 1, 0), Math.PI/3);
        w = new World(white, gray);
        w.addGeometry(new Plane(new ShadedMaterial(green.mul(0.75), white, 32)));
        w.addGeometry(new Node(new Transformation().translation(1, 0.5, 1).scale(0.5, 0.5, 0.5), new Sphere(new ShadedMaterial(red, white, 32))));
        w.addGeometry(new Node(new Transformation().translation(-1, 0.5, 1), new AxisAlignedBox(new ShadedMaterial(skyblue, white, 32))));
        w.addGeometry(new Triangle(new Point3(0, 0, -1), new Point3(1, 0, -1), new Point3(1, 1, -1), new ShadedMaterial(yellow, white, 32)));
        w.addLight(new DirectionalLight(white, new Vector3(0.3, -1.5, -1), true));
        celShading(c, w);
        
        /*
        // Billard-Tisch
        c = new PerspectiveCamera(new Point3(-8, 8, -8), new Vector3(1, -0.7, 1), new Vector3(0, 1, 0), Math.PI/4);
        w = new World(black, new Color(0.2, 0.2, 0.2));
        w.addGeometry(new Plane(new LambertMaterial(green.mul(0.5))));
        w.addGeometry(new Node(new Transformation().translation(0, 1, 0), new Sphere(new ReflectiveMaterial(white, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(3, 1, 0), new Sphere(new ReflectiveMaterial(red, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(0, 1, 3), new Sphere(new ReflectiveMaterial(yellow, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(6, 1, 0), new Sphere(new ReflectiveMaterial(green, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(3, 1, 3), new Sphere(new ReflectiveMaterial(cyan, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(0, 1, 6), new Sphere(new ReflectiveMaterial(blue, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(9, 1, 0), new Sphere(new ReflectiveMaterial(magenta, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(6, 1, 3), new Sphere(new ReflectiveMaterial(black, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(3, 1, 6), new Sphere(new ReflectiveMaterial(red, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(0, 1, 9), new Sphere(new ReflectiveMaterial(yellow, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(12, 1, 0), new Sphere(new ReflectiveMaterial(green, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(9, 1, 3), new Sphere(new ReflectiveMaterial(cyan, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(6, 1, 6), new Sphere(new ReflectiveMaterial(blue, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(3, 1, 9), new Sphere(new ReflectiveMaterial(magenta, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addGeometry(new Node(new Transformation().translation(0, 1, 12), new Sphere(new ReflectiveMaterial(white, white, 64, new Color(0.5, 0.5, 0.5)))));
        w.addLight(new PointLight(white, new Point3(8, 12, -8), true));
        new Raytracer(c, w, "Punktlicht + Reflection");
        */
        /*
        // OBJ-Import
        w = new World(white, new Color(0.5, 0.5, 0.5));
        c = new PerspectiveCamera(new Point3(400, 150, 400), new Vector3(-1, -0.2, -1), new Vector3(0, 1, 0), Math.PI/4);
        w.addGeometry(new Plane(new LambertMaterial(green)));
        w.addGeometry(new ShapeFromFile("obj/Peach.obj", new LambertMaterial(magenta.mul(0.7))));
        w.addLight(new DirectionalLight(white.mul(0.7), new Vector3(0, -1, -2), true));
        w.addLight(new PointLight(white.mul(0.3), c.e, true));
        new Raytracer(c, w, "Peach.obj");
        
        w = new World(white, new Color(0.5, 0.5, 0.5));
        c = new PerspectiveCamera(new Point3(400, 150, 400), new Vector3(-1, -0.2, -1), new Vector3(0, 1, 0), Math.PI/4);
        w.addGeometry(new Plane(new LambertMaterial(green)));
        w.addGeometry(new ShapeFromFile("obj/Rosalina.obj", new LambertMaterial(cyan.mul(0.5))));
        w.addLight(new DirectionalLight(white.mul(0.7), new Vector3(0, -1, -2), true));
        w.addLight(new PointLight(white.mul(0.5), c.e, true));
        new Raytracer(c, w, "Rosalina.obj");
        */
        w = new World(black, new Color(0.5, 0.5, 0.5));
        c = new PerspectiveCamera(new Point3(20, -3, 20), new Vector3(-1, 0, -1), new Vector3(0, 1, 0), Math.PI/4);
        w.addGeometry(new Node(new Transformation().translation(0, -5, 0), new Plane(new LambertMaterial(skyblue.mul(0.5)))));
        w.addGeometry(new ShapeFromFile("obj/bowser.obj", new LambertMaterial(red.mul(0.5))));
        w.addLight(new DirectionalLight(white.mul(0.7), new Vector3(0, -1, -2), true));
        w.addLight(new PointLight(white.mul(0.5), c.e, true));
        //new Raytracer(c, w, "Bowser.obj");
        
        w = new World(black, new Color(0.5, 0.5, 0.5));
        c = new PerspectiveCamera(new Point3(3, 0, 3), new Vector3(-1, 0, -1), new Vector3(0, 1, 0), Math.PI/4);
        //w.addGeometry(new Plane(new Point3(0, -5, 0), new Normal3(0, 1, 0), new LambertMaterial(skyblue.mul(0.5))));
        w.addGeometry(new ShapeFromFile("obj/teddy.obj", new LambertMaterial(white.mul(0.5))));
        w.addLight(new DirectionalLight(white.mul(0.7), new Vector3(0, -1, -2), true));
        w.addLight(new PointLight(white.mul(0.5), c.e, true));
        //new Raytracer(c, w, "Teddy.obj");
        /*
        w = new World(black, new Color(0.5, 0.5, 0.5));
        c = new PerspectiveCamera(new Point3(20, 15, 20), new Vector3(-1, -0.5, -1), new Vector3(0, 1, 0), Math.PI/4);
        //w.addGeometry(new Plane(new Point3(0, -5, 0), new Normal3(0, 1, 0), new LambertMaterial(skyblue.mul(0.5))));
        w.addGeometry(new ShapeFromFile("obj/bunny.obj", new LambertMaterial(white.mul(0.5))));
        w.addLight(new DirectionalLight(white.mul(0.7), new Vector3(0, -1, -2), true));
        w.addLight(new PointLight(white.mul(0.5), c.e, true));
        new Raytracer(c, w, "Bunny.obj");
        */
        /*
        // Scenegraf: Tisch
        Geometry box = new AxisAlignedBox(new LambertMaterial(green));
        Geometry kugel = new Sphere(new PhongMaterial(red, white, 32));
        Geometry ebene = new Plane(new LambertMaterial(skyblue));
        
        Geometry tischBein = new Node(new Transformation().translation(0, 5, 0).scale(1, 10, 1), box);
        Geometry tischBein1 = new Node(new Transformation().translation(-5, 0, -5), tischBein);
        Geometry tischBein2 = new Node(new Transformation().translation(-5, 0, 5), tischBein);
        Geometry tischBein3 = new Node(new Transformation().translation(5, 0, -5), tischBein);
        Geometry tischBein4 = new Node(new Transformation().translation(5, 0, 5), tischBein);
        Geometry tischPlatte = new Node(new Transformation().translation(0, 10.5, 0).scale(11, 1, 11), box);
        
        List<Geometry> tischListe = new ArrayList<>();
        tischListe.add(tischBein1);
        tischListe.add(tischBein2);
        tischListe.add(tischBein3);
        tischListe.add(tischBein4);
        tischListe.add(tischPlatte);
        Geometry tisch = new Node(new Transformation(), tischListe);
        
        w = new World(white, gray);
        w.addLight(new DirectionalLight(white, new Vector3(1, -2, -1), true));
        w.addGeometry(new Node(new Transformation().scale(0.7, 0.7, 0.7), tisch));
        w.addGeometry(ebene);
        w.addGeometry(new Node(new Transformation().translation(0, 12, 0).rotateX(PI/4).scale(3, 1, 3), kugel));
        
        c = new PerspectiveCamera(new Point3(20, 15, 20), new Vector3(-1, -0.5, -1), new Vector3(0, 1, 0), Math.PI/4);
        new Raytracer(c, w, "Tisch");
        */
        
        // Stern
        c = new PerspectiveCamera(new Point3(4, 4, 4.1), new Vector3(-4, -4, -4.1), new Vector3(0, 1, 0), Math.PI/4);
        w = new World(blue, gray);
        w.addLight(new PointLight(white, new Point3(3, 4, 5), true));
        w.addGeometry(new Node(new Transformation().scale(1.5, 0.5, 1.5), new YAxisStar(5, 0.5, new LambertMaterial(new Color(1, 0.75, 0)))));
        new Raytracer(c, w, "Stern");
        
        // Fischaugenkamera
        c = new FishEyeCamera(new Point3(2, 2, 2), new Vector3(-1, -1, -1), new Vector3(0, 1, 0), Math.PI);
        w = new World(white, gray);
        w.addGeometry(new Plane(new PhongMaterial(green.mul(0.75), white, 32)));
        w.addGeometry(new Node(new Transformation().translation(1, 0.5, 1).scale(0.5, 0.5, 0.5), new Sphere(new PhongMaterial(red, white, 32))));
        w.addGeometry(new Node(new Transformation().translation(-1, 0.5, 1), new AxisAlignedBox(new PhongMaterial(skyblue, white, 32))));
        w.addGeometry(new Triangle(new Point3(0, 0, -1), new Point3(1, 0, -1), new Point3(1, 1, -1), new PhongMaterial(yellow, white, 32)));
        w.addLight(new DirectionalLight(white, new Vector3(0.3, -1.5, -1), true));
        new Raytracer(c, w, "Fischaugenkamera");
        c = new FishEyeCamera(new Point3(2, 2, 2), new Vector3(-1, -1, -1), new Vector3(0, 1, 0), 2*Math.PI);
        new Raytracer(c, w, "Fischaugenkamera");
    }
}