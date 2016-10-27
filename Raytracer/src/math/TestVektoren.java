package math;

/**
 * Das ist die Testklasse der Matrizen- Vektorenbibliothek.
 */
public class TestVektoren {

    /**
     * Die Mainmethode der Testklasse
     * Ueberprueft einzelne Berechnungen
     */
	public static void main(String[] args) {

		final Normal3 n_a = new Normal3(1,2,3);
		System.out.println(n_a.mul(0.5));
		System.out.println();

		final Normal3 n_b = new Normal3(3,2,1);
		System.out.println(n_a.add(n_b));
		System.out.println();

		final Normal3 n1 = new Normal3(1,0,0);
		final Vector3 v1 = new Vector3(1,0,0);
		final Normal3 n2 = new Normal3(0,1,0);
		final Vector3 v2 = new Vector3(0,1,0);
		System.out.println(n1.dot(v1));
		System.out.println(n1.dot(v2));
		System.out.println(v1.dot(n1));
		System.out.println(v1.dot(n2));
		System.out.println(v1.dot(v1));
		System.out.println(v1.dot(v2));
		System.out.println();

		final Point3 p0 = new Point3(1,1,1);
		final Point3 p_c = new Point3(2,2,0);
		final Vector3 v_c = new Vector3(4,3,2);
		System.out.println(p0.sub(p_c));
		System.out.println(p0.sub(v_c));
		System.out.println(p0.add(v_c));
		System.out.println();

		System.out.println(new Vector3(1,1,1).magnitude);
		System.out.println();

		final Vector3 v_a = new Vector3(-0.707,0.707,0);
		final Vector3 v_b = new Vector3(0.707,0.707,0);
		System.out.println(v_a.reflectedOn(n2));
		System.out.println(v_b.reflectedOn(n1));
		System.out.println();

		final Mat3x3 mat = new Mat3x3(1,0,0, 0,1,0, 0,0,1);
		final Point3 p_d = new Point3(3,2,1);
		final Vector3 v_d = new Vector3(3,2,1);
		System.out.println(mat.mul(p_d));
		System.out.println(mat.mul(v_d));
		System.out.println();

		final Mat3x3 mat1 = new Mat3x3(1,2,3, 4,5,6, 7,8,9);
		final Mat3x3 mat0 = new Mat3x3(0,0,1, 0,1,0, 1,0,0);
		System.out.println(mat1.mul(mat));
		System.out.println(mat1.mul(mat0));
		System.out.println();

		final Vector3 v0 = new Vector3(8,8,8);
		System.out.println(mat1.changeCol1(v0));
		System.out.println(mat1.changeCol2(v0));
		System.out.println(mat1.changeCol3(v0));
		System.out.println();
	}
}