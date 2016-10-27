package math;

/**
 * Implementiert einen Punkt im Raum.
 */
public class Point3 {

    // Das ist der Wert des Punktes in X-Richtung
	public final double x;

	// Das ist der Wert des Punktes in Y-Richtung
	public final double y;

	// Das ist der Wert des Punktes in Z-Richtung
	public final double z;

    /**
     * Der Konstruktor der Klasse Point3.
     *
     * @param x Das ist der Wert des Punktes in X-Richtung
     * @param y Das ist der Wert des Punktes in Y-Richtung
     * @param z Das ist der Wert des Punktes in Z-Richtung
     */
	public Point3(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Subtrahiert 2 Punkte und gibt das Ergebnis als Vector3-Objekt zurück.
	 * @return Ergebnis als neues Vector3-Objekt
	 */
	public Vector3 sub(final Point3 p) {
		return new Vector3(x - p.x, y - p.y, z - p.z);
	}
	
	/**
	 * Subtrahiert einen Vektor und gibt das Ergebnis als neues Point3-Objekt zurück.
	 * @return Ergebnis als neues Point3-Objekt
	 */
	public Point3 sub(final Vector3 v) {
		return new Point3(x - v.x, y - v.y, z - v.z);
	}
	
	/**
	 * Addiert einen Vektor und gibt das Ergebnis als neues Point3-Objekt zurück.
	 * @return Ergebnis als neues Point3-Objekt
	 */
	public Point3 add(final Vector3 v) {
		return new Point3(x + v.x, y + v.y, z + v.z);
	}
	
	/**
	 * Konvertiert den Punkt in einen Ortsvektor.
	 * @return Punkt als neues Vector3-Objekt
	 */
	public Vector3 asVector() {
		return new Vector3(x, y, z);
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
}