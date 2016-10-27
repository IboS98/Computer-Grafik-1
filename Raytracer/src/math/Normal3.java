package math;

/**
 * Implementiert eine Normale auf einer Oberfläche.
 */
public class Normal3 {

    // Das ist der Wert der Normalen in X-Richtung.
    public final double x;

    // Das ist der Wert der Normalen in Y-Richtung.
    public final double y;

    // Das ist der Wert der Normalen in Z-Richtung.
    public final double z;

    /**
     * Der Konstruktor der Klasse Normal3.
     *
     * @param x Das ist der Wert der Normalen in X-Richtung
     * @param y Das ist der Wert der Normalen in Y-Richtung
     * @param z Das ist der Wert der Normalen in Z-Richtung
     */
    public Normal3(final double x, final double y, final double z) {
        double magnitude = Math.sqrt(x*x + y*y + z*z);
        this.x = x/magnitude;
        this.y = y/magnitude;
        this.z = z/magnitude;
    }

    /**
     * Multipliziert jede Komponente der Normalen mit einem Wert c.
     *
     * @return Ergebnis als neues Normal3-Objekt
     */
    public Normal3 mul(final double c) {
        return new Normal3(x * c, y * c, z * c);
    }

    /**
     * Addiert jede Komponente der Normalen mit denen des übergebenen
     * Normal3-Objekts.
     *
     * @return Ergebnis als neues Normal3-Objekt
     */
    public Normal3 add(final Normal3 n) {
        return new Normal3(n.x + x, n.y + y, n.z + z);
    }

    /**
     * Berechnet das Skalarprodukt eines Vektors mit der Normalen.
     *
     * @return Ergebnis als Double-Wert
     */
    public double dot(final Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
