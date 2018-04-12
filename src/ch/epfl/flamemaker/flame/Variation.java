/**
 * Variation class
 * Non-linear transformations used to draw a flame type fractal
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.flame;

import java.util.List;
import java.util.Arrays;

import ch.epfl.flamemaker.geometry2d.*;

public abstract class Variation implements Transformation {
	private final int index;
	private final String name;
	
	/**
	 * Variation constructor
	 * 
	 * @param index The index of the current variation
	 * @param name The name of the current variation
	 */
	public Variation(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	/**
	 * @return the index of the current variation
	 */
	public int index() {
		return index;
	}
	
	/**
	 * @return the name of the current variation
	 */
	public String name() {
		return name;
	}
	
	abstract public Point transformPoint(Point p);
	
	/**
	 * List of all the possible variations as anonymous classes
	 * 
	 * Linear
	 * Sinusoidal
	 * Spherical
	 * Swirl
	 * Horseshoe
	 * Bubble
	 */
	public final static List<Variation> ALL_VARIATIONS =
		Arrays.asList(
			new Variation(0, "Linear") {
                public Point transformPoint(Point p) {
				    return new Point(p.x(), p.y());
                }
			},
			new Variation(1, "Sinusoidal") {
                public Point transformPoint(Point p) {
                    double x = Math.sin(p.x());
                    double y = Math.sin(p.y());

                    return new Point(x, y);
                }
            },
			new Variation(2, "Spherical") {
                public Point transformPoint(Point p) {
                	if (p.r() == 0.0) {
                		return p;
                	}
                	
				    double x = p.x() / (p.r() * p.r());
				    double y = p.y() / (p.r() * p.r());
				
                    return new Point(x, y);
                }
			},	
			new Variation(3, "Swirl") {
				public Point transformPoint(Point p) {
                    double x = (p.x() * Math.sin(p.r() * p.r())) - (p.y() * Math.cos(p.r() * p.r()));
                    double y = (p.x() * Math.cos(p.r() * p.r())) + (p.y() * Math.sin(p.r() * p.r()));

                    return new Point(x, y);
                }
			},	
			new Variation(4, "Horseshoe") {
                public Point transformPoint(Point p) {
                	if (p.r() == 0.0) {
                		return p;
                	}
                	
                    double x = ((p.x() - p.y()) * (p.x() + p.y())) / p.r();
                    double y = (2 * p.x() * p.y()) / p.r();

                    return new Point(x, y);
                }
			},	
			new Variation(5, "Bubble") {
				public Point transformPoint(Point p) {
                    double x = (4 * p.x()) / ((p.r() * p.r()) + 4);
                    double y = (4 * p.y()) / ((p.r() * p.r()) + 4);

                    return new Point(x, y);
                }
			}
		);
}
