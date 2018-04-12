/**
 * Random palette class
 * Auto-generated palette 
 * A palette is a list of colors used to colorize a fractal
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public final class RandomPalette implements Palette {
	private Palette palette;

	public RandomPalette(int numberOfColors) {
		List<Color> tmpPalette = new ArrayList<Color>();
		Random rand = new Random();
		double r, g, b;

		if(numberOfColors < 2) {
			throw new IllegalArgumentException("Minimum 2 colors are needed to create a palette");
		}

		/* Generating a random color */
		for(int i = 0; i < numberOfColors; i++) {
			r = rand.nextDouble();
			g = rand.nextDouble();
			b = rand.nextDouble();

			tmpPalette.add(new Color(r, g, b));
		}

		/* Using an interpolatedpalette with the list of random colors */
		palette = new InterpolatedPalette(tmpPalette);
	}

	public Color colorForIndex(double index) throws IllegalArgumentException {
		return palette.colorForIndex(index);
	}
}
