/**
 * Flame accumulator class
 * Used to store all the points computed
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.*;
import ch.epfl.flamemaker.geometry2d.*;

final public class FlameAccumulator {
	private int[][] accumulator;
    private double[][] colorIndexAccumulator;
    private final int width, height;
    private final double logMaxPoints;
	
	private FlameAccumulator(int[][] hitCount, double[][] colorIndexSum) {
		width = hitCount[0].length; 
        height = hitCount.length;
		accumulator = new int[height][width];
        colorIndexAccumulator = new double[height][width];
        int max = 0;
		
        /*
         * Copy of the two arrays. Could be done by the method clone()
         * but we profit of the two loops to determine the maximum
         * number of points in a box of the accumulator
         */
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				accumulator[i][j] = hitCount[i][j];
                this.colorIndexAccumulator[i][j] = colorIndexSum[i][j];
                
                if(accumulator[i][j] > max) {
                    max = accumulator[i][j];
                }
			}
		}

        logMaxPoints = Math.log(max + 1);
	}
	
	/**
	 * Width getter
	 * 
	 * @return The width of the accumulator
	 */
	public int width() {
		return width;
	}
	
	/**
	 * Height getter
	 * 
	 * @return The height of the accumulator
	 */
	public int height() {
		return height;
	}
	
	/**
	 * Gets the intensity of a pixel at coordinates (x,y)
	 * 
	 * @param x The x coordinate of the accumulator
	 * @param y The y coordinate of the accumulator
	 * @return The intensity of a pixel of the fractal
	 */
	public double intensity(int x, int y) {
        if(x < 0 || x > width || y < 0 || y > height) {
           throw new IndexOutOfBoundsException();
        } 
		
        return (Math.log(accumulator[y][x] + 1)/logMaxPoints); // applying a log to smooth the contrast of the image
	}
	
	/**
	 * @param palette
	 * @param background
	 * @param x
	 * @param y
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public Color color(Palette palette, Color background, int x, int y) throws IndexOutOfBoundsException {
        if(x < 0 || x > width || y < 0 || y > height) {
            throw new IndexOutOfBoundsException();
        }
        
        if(colorIndexAccumulator[y][x] != 0) {
            Color properColor = palette.colorForIndex(colorIndexAccumulator[y][x]/accumulator[y][x]);
            double intensity = intensity(x, y);
            return background.mixWith(properColor, intensity);
        }
        else return background;
    }

	/**
     * Builder class
     * Needed to build a FlameAccumulator by incrementation. It makes it temporary mutable but guarantees the OO encapsulation.
     */
    final public static class Builder {
        private int[][] accumulator;
        private double[][] colorIndexAccumulator;
        private Rectangle frame;
        private AffineTransformation scale, translate;

        /**
         * @param frame The frame where all points that are contained by him are stored
         * @param width The width of the accumulator (or the "image")
         * @param height The height of the accumulator (or the "image)
         */
        public Builder(Rectangle frame, int width, int height) {
            if(width <= 0 || height <= 0) {
                throw new IllegalArgumentException();
            }
            
            this.frame = frame;
            accumulator = new int[height][width];
            colorIndexAccumulator = new double[height][width];

            double scaleToAccumulatorX = (width/(frame.right()-frame.left()));
            double scaleToAccumulatorY = (height/(frame.top()-frame.bottom()));

            scale = AffineTransformation.newScaling(scaleToAccumulatorX, scaleToAccumulatorY);
            translate = AffineTransformation.newTranslation(-frame.left(), -frame.bottom());
        }

        /**
         * Determines if the given point is in the frame. If true, it stores it with his color index in the accumulator.
         * 
         * @param p The point to store
         * @param colorIndex The point's color index
         */
        public void hit(Point p, double colorIndex) {
            if(colorIndex < 0 || colorIndex > 1) {
                throw new IllegalArgumentException();
            }

            Point displacedPoint;
            int x, y;

            if(frame.contains(p)) {
            	/* Move the point and scales to the dimension of the accumulator */
                displacedPoint = translate.transformPoint(p);
                displacedPoint = scale.transformPoint(displacedPoint);

                x = (int)Math.floor(displacedPoint.x());
                y = (int)Math.floor(displacedPoint.y());

                accumulator[y][x] += 1; //There is a new point in this box
                colorIndexAccumulator[y][x] += colorIndex; // We do then the average after the computation of all points
            }
        }

        /**
         * @return A new immutable FlameAccumulator
         */
        public FlameAccumulator build() {
            return new FlameAccumulator(accumulator, colorIndexAccumulator);
        }
    }
}
