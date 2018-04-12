/** 
 * Interpolated palette
 * A palette is a list of colors used to colorize a fractal
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

public final class InterpolatedPalette implements Palette {
	private List<Color> palette;
	private int paletteSize;

    /**
     * Builder
     * 
	 * @param palette A palette of colors
	 * @throws IllegalArgumentException When palette contains less than 2 colors
	 */
	public InterpolatedPalette(List<Color> palette) throws IllegalArgumentException {
		if(palette.size() < 2) {
			throw new IllegalArgumentException("There must be at least 2 colors");
		}
		this.palette = new ArrayList<Color>(palette);
		paletteSize = this.palette.size();
	}

	/**
	 * Determines a color for the given color index
	 * 
	 * @param index Color index of a point
	 * @throws IllegalArgumentException When index isn't included in [0,1]
	 * @return The color at the position defined by the index on the palette
	 */
	public Color colorForIndex(double index) throws IllegalArgumentException {
		if(index < 0 || index > 1) {
			throw new IllegalArgumentException("The index must be between 0 and 1");
		}

		int cLeft = (int)Math.floor(index * (paletteSize - 1));
		int cRight = (int)Math.ceil(index * (paletteSize - 1));
		double proportion = (index * (paletteSize - 1)) - cLeft;

		return (palette.get(cLeft)).mixWith(palette.get(cRight), proportion);
	}
}
