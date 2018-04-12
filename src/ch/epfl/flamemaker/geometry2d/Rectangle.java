/**
 * Rectangle class
 * Represents a rectangle on plan with his center, width and height
 *
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.geometry2d;

final public class Rectangle {	
	private Point center;
	private final double width, height;
	private final double left, right, top, bottom;
	private final double aspectRatio;
	
    /**
     * Rectangle constructor
     *
     * @param center A point representing his center
     * @param width  The rectangle's width
     * @param height The rectangle's height
     * @throws IllegalArgumentException When width or height isn't a non-zero positive number
     */
	public Rectangle(Point center, double width, double height) {	
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.center = center;
		this.width = width;
		this.height = height;
		
        // Computing each side's coordinate
		left = center.x() - width/2;
		right = center.x() + width/2;
		bottom = center.y() - height/2;
		top = center.y() + height/2;
		
		aspectRatio = width/height;
	}

    /**
     * left getter
     *
     * @return The coordinate of the left side of the rectangle
     */
	public double left() {
		return left;
	}
	
    /**
     * right getter
     *
     * @return The coordinate of the right side of the rectangle
     */
	public double right() {
		return right;
	}
	
    /**
     * bottom getter
     *
     * @return The coordinate of the bottom side of the rectangle
     */
	public double bottom() {
		return bottom;
	}

    /**
     * top getter
     *
     * @return The coordinate of the top side of the rectangle
     */
	public double top() {
		return top;
	}
	
    /**
     * width getter
     *
     * @return The width of the rectangle
     */
	public double width() {
		return width;
	}
	
    /**
     * height getter
     *
     * @return The height of the rectangle
     */
	public double height() {
		return height;
	}
	
    /**
     * center getter
     *
     * @return A point representing the center of the rectangle
     */
	public Point center() {
		return center;
	}
	
    /**
     * aspectRatio getter
     *
     * @return the aspect ratio of the rectangle
     */
	public double aspectRatio() {
		return aspectRatio;
	}

    /**
     * Evaluates if a given point is contained in the actual rectangle
     *
     * @return A boolean, true if the point is in the rectangle, false otherwise
     */
	public boolean contains(Point p) {
		if (p.x() >= left && p.x() < right && p.y() < top && p.y() >= bottom) 
			return true;
		else 
			return false;
	}
	
    /**
     * Expands the ratio of the actual rectangle with a given aspectRatio
     *
     * @return A Rectangle with a new aspect ratio
     * @throws IllegalArgumentException If the aspectRatio is zero
     */
	public Rectangle expandToAspectRatio(double aspectRatio) {
        if(aspectRatio <= 0)
            throw new IllegalArgumentException();

        if (aspectRatio > this.aspectRatio)
			return new Rectangle(center, height * aspectRatio, height);
		else if (aspectRatio < this.aspectRatio)
			return new Rectangle(center, width, width / aspectRatio);
		else
			return new Rectangle(center, width, height);
	}
	
    /**
     * toString method
     * Displays the parameters of the rectangle
     *
     * @return a string representation of the rectangle
     */
	public String toString() {
		return "(" + center.x() + "," + center.y() + ")," + width + "," + height + ")";
	}
}
