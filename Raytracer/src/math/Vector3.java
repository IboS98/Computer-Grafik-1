package math;

/**
 * Implementiert einen Raum-Vektor.
 */
public class Vector3 {

    // Das ist der Wert des Vectors in X-Richtung
    public final double x;

    // Das ist der Wert des Vectors in Y-Richtung
    public final double y;

    // Das ist der Wert des Vectors in Z-Richtung
    public final double z;

    // Die Laenge eines Raum-Vektor.
    public final double magnitude;

    /**
     * Der Konstruktor der Klasse Vector3
     *
     * @param x Das ist der Wert des Vectors in X-Richtung
     * @param y Das ist der Wert des Vectors in Y-Richtung
     * @param z Das ist der Wert des Vectors in Z-Richtung
     */
    public Vector3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        magnitude = Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Addiert jede Komponente des Vektors mit denen des übergebenen
     * Vector3-Objekts
     *
     * @return Ergebnis als neues Vector3-Objekt
     */
    public Vector3 add(final Vector3 v) {
        return new Vector3(v.x + x, v.y + y, v.z + z);
    }

    /**
     * Addiert jede Komponente des Vektors mit denen des übergebenen
     * Normal3-Objekts
     *
     * @return Ergebnis als neues Vector3-Objekt
     */
    public Vector3 add(final Normal3 n) {
        return new Vector3(n.x + x, n.y + y, n.z + z);
    }

    /**
     * Subtrahiert jede Komponente des übergebenen Normal3-Objekts vom denen des
     * Vektors
     *
     * @return Ergebnis als neues Vector3-Objekt
     */
    public Vector3 sub(final Normal3 n) {
        return new Vector3(x - n.x, y - n.y, z - n.z);
    }

    /**
     * Multipliziert jede Komponente des Vektors mit einem Wert c
     *
     * @return Ergebnis als neues Vector3-Objekt
     */
    public Vector3 mul(final double c) {
        return new Vector3(x * c, y * c, z * c);
    }

    /**
     * Berechnet das Skalarprodukt zweier Vektoren
     *
     * @return Ergebnis als Double-Wert
     */
    public double dot(final Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Berechnet das Skalarprodukt einer Normalen mit dem Vektor
     *
     * @return Ergebnis als Double-Wert
     */
    public double dot(final Normal3 n) {
        return x * n.x + y * n.y + z * n.z;
    }

    /**
     * Gibt eine Kopie des Vektors in Normalform zurück
     *
     * @return Der neue Vektor
     */
    public Vector3 normalized() {
        if (magnitude <= 0) {
            return this;
        }
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    /**
     * Konvertiert den Vektor in eine Normale
     *
     * @return Der Vektor als Normal3-Objekt
     */
    public Normal3 asNormal() {
        return new Normal3(x, y, z);
    }

    /**
     * Reflektiert den Vektor an einer Normalen
     *
     * @return Der reflektierte Vektor
     */
    public Vector3 reflectedOn(final Normal3 n) {
        // return: 2 * n * dot(n,v) - v
        Vector3 v = sub(n.mul(2 * dot(n)));
        return new Vector3(-(v.x), -(v.y), -(v.z));
    }

    /**
     * Berechnet das Kreuzprodukt zwischen 2 Vektoren
     *
     * @return Das Kreuzprodukt als neues Vector3-Objekt
     */
    public Vector3 x(final Vector3 v) {
        return new Vector3(
                this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z,
                this.x * v.y - this.y * v.x
        );
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
