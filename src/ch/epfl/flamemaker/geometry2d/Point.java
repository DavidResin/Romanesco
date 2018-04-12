/**
 * Point class
 * Represents a point with his cartesian and polar coordinates on a plan
 *
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.geometry2d;
import java.lang.Math;

final public class Point {	
	private final double x, y, r, theta;
	public static final Point ORIGIN = new Point(0, 0);
	
    /**
     * Point constructor
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		
		r = Math.sqrt((x * x) + (y * y)); // Computes the radius of this point
		theta = Math.atan(y / x); // Computes the angle of this point
	}
	
    /**
     * x getter
     * 
     * @return The x coordinate of the point
     */
	public double x() {
		return x;
	}
	
    /**
     * y getter
     * 
     * @return The y coordinate of the point
     */
	public double y() {
		return y;
	}
	
    /** 
     * r getter
     * 
     * @return The radius of the point
     */
	public double r() {
		return r;
	}
	
    /**
     * theta getter
     *
     * @return The angle of the point
     */
	public double theta() {
		return theta;
	}
	
    /**
     * Displays the coordinate of the point
     *
     * @return A string displaying the coordinates of the point
     */
	public String toString() {
		return "(" + x + "," + y + ")";	
	}
	
}
