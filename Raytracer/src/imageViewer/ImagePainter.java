package imageViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * Diese Klasse erstellt und malt ein Bild mit schwarzen Hintergrund und einer roten Diagonale.
 * Zusaetzlich enthaelt es ein Speichermenue
 */
public class ImagePainter extends Canvas {

    // Legt die Tiefe des Fensters fest.
    public static final int WIDTH = 640;

    // Left die Hoehe des Fenster fest.
    public static final int HEIGHT = 480;

    // Objekt fuer das zu speichernde Bild.
    private BufferedImage bImage;

    /**
     * Der Konstruktor der Klasse ImagePainter
     */
    public ImagePainter() {

        // Erstellt ein neues Fenster.
        final JFrame window = new JFrame();

        // Fuegt dem Fenster das Canvas hinzu.
        window.add(this);

        // Setzt den Titel.
        window.setTitle("Rote Diagonale");

        // Erstellt eine JMenuBar.
        final JMenuBar menu = new JMenuBar();

        // Erstellt eine JMenu-Datei.
        final JMenu save = new JMenu("Datei");
        menu.add(save);

        // Erstellt ein JMenuItem Speichern.
        final JMenuItem saveitem = new JMenuItem("Speichern");
        saveitem.addActionListener(new AL());
        save.add(saveitem);

        // Setzt die JMenuBar am PAGE_START des Fensters.
        window.add(menu, BorderLayout.PAGE_START);

        // Schließt das Fenster.
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Bestimmt die Groesse des Fensters.
        window.setSize(WIDTH, HEIGHT);

        // Macht den Inhalt des ImagePainters sichtbar.
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
     * Das ist die Paintmethode der Klasse Canvas. Es erstellt die Grafik
     *
     * @param g Die Grafik der Paintmethode
     */
    @Override
    public void paint(final Graphics g) {

        // Ein BufferedImage holt sich die Breite und die Höhe der Grafik, dabei wird es auf den Typ RGB gesetzt.
        this.bImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Ein Raster wird für die Grafik ertellt.
        final WritableRaster raster = bImage.getRaster();

        // Das Farbmodel wird für die Grafik erstellt.
        final ColorModel model = bImage.getColorModel();

        // Diese For-Schleife setzt die Farben der einzelnen Pixel fest.
        // Diese Farbe ist immer Schwarz, außer wenn x und y gleich sind, dann ist das Pixel Rot.
        for (int x = 0; x < this.bImage.getWidth(); x++) {
            for (int y = 0; y < this.bImage.getHeight(); y++) {
                if (x == y) {
                    raster.setDataElements(x, y, model.getDataElements(Color.RED.getRGB(), null));
                }
            }
        }
        // Die Grafik wird gezeichnet.
        g.drawImage(this.bImage, 0, 0, null);
    }
    
    /**
     * Die Mainmethode des ImagePainters
     * Erstellt eine ImagePainter-Instanz zum Anzeigen der Grafik
     *
     * @throws IOException Wenn eine Datenuebertragung einer Datei nicht hergestellt werden kann
     */
    public static void main(final String[] args) throws IOException {
        new ImagePainter();
    }

}