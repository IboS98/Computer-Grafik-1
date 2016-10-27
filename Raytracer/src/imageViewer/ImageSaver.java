package imageViewer;

import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Diese Klasse repraesentiert die Speicherfunktioneines Bildes
 *
 * @exception IOException Wenn das Bild nicht gespeichert werden kann
 */
public class ImageSaver {

    /**
     * Das ist der Konstruktor der Klasse ImageSaver
     *
     * @param image Das zu speichernde Bild
     * @throws IOException Wenn das Bild nicht gespeichert werden kann wird diese Exception geworfen
     */
    public ImageSaver(final BufferedImage image) throws IOException {

        // Oeffnet einen FileChooser, um ein PNG zu speichern.
        final JFileChooser chooser = new JFileChooser();

        // Filtert Dateien nach PNG oder png.
        final FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG", "png");
        chooser.removeChoosableFileFilter(chooser.getFileFilter());
        chooser.setFileFilter(pngFilter);
        chooser.setSelectedFile(new File("image.png"));

        // Checkt, ob ein Nutzer eine Datei ausgewaehlt hat.
        final int ret = chooser.showSaveDialog(chooser);
        if (ret != JFileChooser.APPROVE_OPTION) return;

        // Holt sich die selektierte Datei.
        final File imagePath = chooser.getSelectedFile();

        // Schreibt die Datei
        ImageIO.write(image, "png", imagePath);
    }
}
