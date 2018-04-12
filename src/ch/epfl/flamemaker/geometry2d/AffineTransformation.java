/**
 * AffineTransformation class
 * Class for representing an affine transformation with a 3x3 matrix
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.geometry2d;

final public class AffineTransformation {	
	private final double a, b, c, d , e, f;	
	public static final AffineTransformation IDENTITY = new AffineTransformation(1, 0, 0, 0, 1, 0); // Identity matrix
	
	/**
	 * AffineTransformation constructor
	 * 
	 * @param a a component of the matrix
	 * @param b b component of the matrix
	 * @param c	c component of the matrix
	 * @param d d component of the matrix
	 * @param e e component of the matrix
	 * @param f f component of the matrix
	 */
	public AffineTransformation(double a, double b, double c, double d, double e, double f) {		
		this.a = a;
		this.b = b;
		this.c = c;		
		this.d = d;
		this.e = e;
		this.f = f;
	}
	
	/**
	 * @param dx Translation on x to be applied
	 * @param dy Translation on y to be applied
	 * @return 	 A new transformation 
	 */
	public static AffineTransformation newTranslation(double dx, double dy) {
		return new AffineTransformation(1, 0, dx, 0, 1, dy);
	}
	
	/**
	 * @param theta Angle of the rotation 
	 * @return A new transformation
	 */
	public static AffineTransformation newRotation(double theta) {
		return new AffineTransformation(Math.cos(theta), -Math.sin(theta), 0, Math.sin(theta), Math.cos(theta), 0);
	}
	
	/**
	 * @param sx Scale on x to be applied
	 * @param sy Scale on y to be applied
	 * @return A new transformation
	 */
	public static AffineTransformation newScaling(double sx, double sy) {
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}
	
	/**
	 * @param sx Shear on x to be applied
	 * @return A new transformation
	 */
	public static AffineTransformation newShearX(double sx) {
		return new AffineTransformation(1, sx, 0, 0, 1, 0);
	}
	
	/**
	 * @param sy Shear on y to be applied
	 * @return A new transformation
	 */
	public static AffineTransformation newShearY(double sy) {
		return new AffineTransformation(1, 0, 0, sy, 1, 0);
	}
	
	/**
	 * @param that AffineTransformation multiplied by the actual AffineTransformation
	 * @return A new transformation
	 */
	public AffineTransformation composeWith(AffineTransformation that) {		
		double aNew = (a * that.a) + (b * that.d);
		double bNew = (a * that.b) + (b * that.e);
		double cNew = (a * that.c) + (b * that.f) + c;
		double dNew = (d * that.a) + (e * that.d);
		double eNew = (d * that.b) + (e * that.e);
		double fNew = (d * that.c) + (e * that.f) + f;
		
		return new AffineTransformation(aNew, bNew, cNew, dNew, eNew, fNew);
	}
	
	/**
	 * @param p Point to be transformed by the actual AffineTransformation
	 * @return A new transformedPoint
	 */
	public Point transformPoint(Point p) {
		double x = (a * p.x()) + (b * p.y()) + c;
		double y = (d * p.x()) + (e * p.y()) + f;
		
		return new Point(x, y);
	}
	
	/**
	 * @return c component of the matrix (representing the x translation)
	 */
	public double translationX() {
		return c;
	}
	
	/**
	 * @return f component of the matrix (representing the y translation)
	 */
	public double translationY() {
		return f;
	}
}
