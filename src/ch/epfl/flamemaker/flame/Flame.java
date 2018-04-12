/**
 * Flame class
 * Computes a fractal of type flame
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Flame {
	private List<FlameTransformation> transformationList; 

	public Flame(List<FlameTransformation> transformations) {
		transformationList = new ArrayList<FlameTransformation>(transformations);
	}

	/**
	 * Computes a fractal of type flame and returns his image with an accumulator
	 *
	 * @param frame A rectangle object that represents the part of the plan that saves the computed points
	 * @param width Width of the accumulator
	 * @param height Height of the accumulator
	 * @param density Affects the number of iterations. Gives a more fine image when it increases
	 * @return A Flame accumulator that holds the entire "image" of the fractal
	 */
	public FlameAccumulator compute(Rectangle frame, int width, int height, int density) {
		FlameAccumulator.Builder flame = new FlameAccumulator.Builder(frame, width, height);
		Random rand = new Random(2013); // Makes it determinist
		int iterations = density * width * height;
		int listSize = transformationList.size();
		int i;
		Point p = new Point(0, 0);
		double previousColorIndex = 0, actualColorIndex = 0, transformationColorIndex = 0;

		// Doing 20 iterations as demanded in the assignement
		for(int m = 0; m < 20; m++) {
			i = rand.nextInt(listSize);
			transformationColorIndex = colorIndex(i);
			actualColorIndex = 0.5*(transformationColorIndex + previousColorIndex);

			p = (transformationList.get(i)).transformPoint(p);

			previousColorIndex = actualColorIndex;
		}

		/*
		 * Chaos algorithm
		 *
		 * 1) Picks a random transformation in the list of transformations
		 * 2) Does the average of the previous transformation's color index and the actual transformation's color index
		 * 3) Transforms a point who's origin was (0,0)
		 * 4) Stores the transformed point with his color index in the accumulator
		 *
		 * Remarks : the transformed point is the same in the whole chaos algorithm. Its coordinates are continually modified.
		 */ 
		for(int m = 0; m < iterations; m++) {
			i = rand.nextInt(listSize);
			transformationColorIndex = colorIndex(i);
			actualColorIndex = 0.5*(transformationColorIndex + previousColorIndex);

			p = (transformationList.get(i)).transformPoint(p);
			flame.hit(p, actualColorIndex);

			previousColorIndex = actualColorIndex;
		}

		return flame.build(); // Builds the accumulator and makes it immutable
	}

	/**
	 * Method that determines the color index of a transformation from its index in the list
	 */
	private double colorIndex(int index) {
		if(index < 0 || index > transformationList.size()) {
			throw new IndexOutOfBoundsException();
		}

		if(index == 0 || index == 1) return index;
		else {
			double pow2log2 = Math.pow(2, (int)Math.ceil(Math.log(index)/Math.log(2)));

			return ((2 * index) - pow2log2 - 1)/pow2log2;
		}
	} 

	/**
	 * FlameAccumulator.Builder
	 * Class that builds a flame accumulator by incrementation
	 */
	public static class Builder {
		private List<FlameTransformation> transformationList;

		/**
		 * Builder constructor
		 * 
		 * @param flame A flame to build
		 */
		public Builder(Flame flame) {
			transformationList = new ArrayList<FlameTransformation>(flame.transformationList);    
		}

		/**
		 * Gets the number of transformation of the fractal
		 * 
		 * @return The number of transformations 
		 */
		public int transformationCount() {
			return transformationList.size();
		}

		/**
		 * Adds a transformation to the fractal
		 * 
		 * @param transformation A transformation to be added to the fractal
		 */
		public void addTransformation(FlameTransformation transformation) {
			transformationList.add(transformation);
		}

		/**
		 * AffineTransformation getter
		 * Gets an affine transformation at the given index
		 * 
		 * @param index The index of the transformation to fetch
		 * @return The transformation to fetch
		 */
		public AffineTransformation affineTransformation(int index) {
			checkIndex(index);
			return (new FlameTransformation.Builder(transformationList.get(index))).affineTransformation();
		}

		/**
		 * AffineTransformation setter
		 * Sets an affine transformation at the given index
		 * 
		 * @param index The index of the transformation to modify
		 * @param newTransformation The new transformation
		 */
		public void setAffineTransformation(int index, AffineTransformation newTransformation) {
			checkIndex(index);
			FlameTransformation.Builder newFlameTransformation = new FlameTransformation.Builder(transformationList.get(index));
			newFlameTransformation.setAffineTransformation(newTransformation);
			transformationList.set(index, newFlameTransformation.build());
		}

		/**
		 * Variation weight getter
		 * 
		 * @param index The index of the transformation from witch we get the variation weights
		 * @param variation The variation from witch we fetch the weight
		 * @return The weight of the given variation
		 */
		public double variationWeight(int index, Variation variation) {
			checkIndex(index);
			return (new FlameTransformation.Builder(transformationList.get(index))).variationWeight(variation.index()); 
		}

		/**
		 * Variation weight setter
		 * 
		 * @param index The index of the transformation to modify
		 * @param variation One of the six variations to modify. Simply by giving one of the variation, we can get his index with his index getter.
		 * @param newWeight The new weight of the given variation
		 */
		public void setVariationWeight(int index, Variation variation, double newWeight) {
			checkIndex(index);
			FlameTransformation.Builder newFlameTransformation = new FlameTransformation.Builder(transformationList.get(index));
			newFlameTransformation.setVariationWeight(variation.index(), newWeight);
			transformationList.set(index, newFlameTransformation.build());
		}

		/**
		 * Transformation remover
		 * 
		 * @param index The index of the transformation to remove
		 */
		public void removeTransformation(int index) {
			checkIndex(index);
			transformationList.remove(index);
		}

		/**
		 * Flame builder method
		 * 
		 * @return A new flame fractal
		 */
		public Flame build() {
			return new Flame(transformationList);
		}

		/*
		 * Needed to check the indexes of the list
		 */
		private void checkIndex(int index) {
			if(index < 0 || index > transformationList.size()) {
				throw new IndexOutOfBoundsException();
			}
		}
	}
}
