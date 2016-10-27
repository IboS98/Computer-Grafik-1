package geometry;

/**
 * Farbklasse fuer den Raytracer
 */
public class Color {

    public final double r;
    public final double g;
    public final double b;

    /**
     * Konstruktor fuer ein Color-Objekt
     * Alle Werte liegen zwischen 0 und 1. Ist ein Wert groesser als 1
     * oder kleiner als 0, wird entstpechend auf- oder abgerundet
     *
     * @param r Rot
     * @param g Gruen
     * @param b Blau
     */
    public Color(final double r, final double g, final double b) {

        if(r < 0) this.r = 0;
        else if(r > 1) this.r = 1;
        else this.r = r;

        if(g < 0) this.g = 0;
        else if(g > 1) this.g = 1;
        else this.g = g;

        if(b < 0) this.b = 0;
        else if(b > 1) this.b = 1;
        else this.b = b;
    }

    /**
     * Addiert diese Farbe mit einer anderen Farbe
     * @param c die zu addierende Farbe
     * @return die neue Farbe
     */
    public Color add(Color c) {
        return new Color(this.r + c.r, this.g + c.g, this.b + c.b);
    }

    /**
     * Subtrahiert eine andere Farbe von dieser Farbe
     * @param c der Subtrahend
     * @return die neue Farbe
     */
    public Color sub(Color c) {
        return new Color(this.r - c.r, this.g - c.g, this.b - c.b);
    }

    /**
     * Multipliziert diese Farbe mit einer anderen Farbe
     * @param c die zu multiplizierende Farbe
     * @return die neue Farbe
     */
    public Color mul(Color c) {
        return new Color(this.r * c.r, this.g * c.g, this.b * c.b);
    }

    /**
     * Multipliziert diese Farbe mit einem Wert
     * @param v der zu multiplizierende Wert
     * @return die neue Farbe
     */
    public Color mul(double v) {
        return new Color(this.r * v, this.g * v, this.b * v);
    }

    @Override
    public String toString() {
        return "(" + r + ", " + g + ", " + b + ")";
    }
}
