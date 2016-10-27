package imageViewer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * Diese Klasse stellt ein BufferedImage dar
 */
public class ImageViewer extends JComponent {

    // Das Bild.
    private final BufferedImage image;

    // Der Name des Bildes.
    private final String imageName;

    /**
     * Konstruktor der Klasse ImageViewer
     *
     * @param image das anzuzeigende Bild
     * @param name  der Name dieser Bilddatei
     */
    public ImageViewer(final BufferedImage image, final String name) {
        this.image = image;
        this.imageName = name;
    }

    /**
     * Malt ein Bild und oeffnet es in einem Fenster
     *
     * @param g Die Grafik des Bildes
     */
    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, this);

        // Initialisiert das Fenster.
        this.setSize(image.getWidth(), image.getHeight());
        this.setName(imageName);
        this.setVisible(true);
    }
}