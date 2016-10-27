package imageViewer;

import java.awt.BorderLayout;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Diese Klasse startet den ImageViewer
 */
public class StartViewer {

    /**
     * Die Mainmethode des StartViewers
     *
     * @throws IOException Wenn eine Datenuebertragung einer Datei nicht hergestellt werden kann
     */
    public static void main(final String[] args) throws IOException {


        // Die Breite des Window Borders.
        final int WINDOW_BORDER_WIDTH = 16;

        // Die Hoehe des Window Borders.
        final int WINDOW_BORDER_HEIGHT = 38;

        // Oeffnet einen FileChooser, um ein PNG oder JPEG auszuwaehlen und anzuzeigen.
        final JFileChooser chooser = new JFileChooser();

        // Filtert nach jpg Dateien.
        final FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter(
                "JPG", "jpg", "jpeg");

        // Filtert nach png dateien.
        final FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
                "PNG", "png");

        // Entfernt den Standard-Dateifilter und fuegt die neuen hinzu.
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        chooser.setFileFilter(jpegFilter);
        chooser.setFileFilter(pngFilter);

        // Checkt, ob ein Nutzer eine Datei ausgewaehlt hat.
        final int ret = chooser.showOpenDialog(chooser);
        if (ret != JFileChooser.APPROVE_OPTION) return;

        // Gibt den Namen der Datei wieder.
        final File imagePath = chooser.getSelectedFile();
        System.out.println(imagePath.getName());

        // Erstellt ein Bild und eine Grafik von einer Datei für den ImageViewer.
        final BufferedImage bImage = ImageIO.read(imagePath);

        // Checkt, ob ein heruntergeladenes Bild nicht 'null' ist.
        if (bImage == null) return;

        // Erstellt eine ImageViewer-Instanz zum Anzeigen eines Bildes.
        final ImageViewer viewer = new ImageViewer(bImage, imagePath.getName());

        // Erstellt ein neues Fenster.
        final JFrame frame = new JFrame();
        frame.getContentPane().add(viewer, BorderLayout.CENTER);

        // Erstellt die Grafik.
        final Graphics imageGraphic = bImage.createGraphics();
        viewer.paint(imageGraphic);

        // Schließt das Fenster.
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Initialisiert das Fenster.
        frame.setSize(bImage.getWidth() + WINDOW_BORDER_WIDTH, bImage.getHeight() + WINDOW_BORDER_HEIGHT);
        frame.setVisible(true);
    }
}