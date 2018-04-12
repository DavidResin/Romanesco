/**
 * FlameTransformation class
 * Used to transform points in order to draw a flame type fractal.
 * This type of transformation has an affine transformation assigned and an array of variation weight
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.*;

final public class FlameTransformation implements Transformation{
	private AffineTransformation transformation;
	private double[] variationWeight;
	
	/**
	 * FlameTransformation constructor
	 * 
	 * @param affineTransformation A new affine transformation to be assigned to the flame transformation
	 * @param variationWeight An array of variation weight
	 */
	public FlameTransformation(AffineTransformation affineTransformation, double[] variationWeight) {
		if(variationWeight.length != 6) {
			throw new IllegalArgumentException("6 variations sont nécessaires à la transformation");
		}
		
		this.variationWeight = variationWeight.clone();
		this.transformation = affineTransformation;
	}
	
	/**
	 * @see ch.epfl.flamemaker.geometry2d.Transformation#transformPoint(ch.epfl.flamemaker.geometry2d.Point)
	 */
	public Point transformPoint(Point p) {
		Point affineTransformedPoint = transformation.transformPoint(p);
		Point r;
		double finalX = 0, finalY = 0;
		
		for (int i = 0; i < 6; i++) {
			if(variationWeight[i] == 0.0) continue;
			
			r = (Variation.ALL_VARIATIONS.get(i)).transformPoint(affineTransformedPoint);
			finalX += r.x() * variationWeight[i];
			finalY += r.y() * variationWeight[i];
		}

		return new Point(finalX, finalY);
	}

    /**
     * Builder class
     * Needed to build a FlameTransformation by incrementation. It makes it temporary mutable but guarantees the OO encapsulation.
     */
    public static class Builder {
        private AffineTransformation transformation;
        private double[] variationWeight;

        /**
         * Builder constructor
         * 
         * @param flameTransformation A flame transformation to be build incrementally 
         */
        public Builder(FlameTransformation flameTransformation) {
            this.transformation = flameTransformation.transformation;
            this.variationWeight = flameTransformation.variationWeight.clone();
        }
        
        /**
         * AffineTransformation getter
         * 
         * @return An AffineTransformation
         */
        public AffineTransformation affineTransformation() {
            return this.transformation;
        }

        /**
         * Variation weight getter
         * 
         * @param index The index of the variation weight
         * @return The weight of the given variation
         */
        public double variationWeight(int index) {
            return this.variationWeight[index];
        }

        /**
         * Variation weight setter
         * 
         * @param index The index of the variation to set
         * @param variationWeight The variation weight to assign
         */
        public void setVariationWeight(int index, double variationWeight) {
            this.variationWeight[index] = variationWeight;
        }

        /**
         * AffineTransformation setter
         * 
         * @param affineTransformation The AffineTransformation to assign
         */
        public void setAffineTransformation(AffineTransformation affineTransformation) {
            this.transformation = affineTransformation;
        }

        /**
         * FlameTransformation builder method
         * 
         * @return A new immutable FlameTransformation
         */
        public FlameTransformation build() {
            return new FlameTransformation(this.transformation, this.variationWeight);
        }
    }
}
