/**
 * Color object. Represents a color with his 3 prime colors : red, green, blue
 * 
 * @author Sydney Hauke
 * @author David Resin
 */
package ch.epfl.flamemaker.color;

public final class Color {
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color WHITE = new Color(1, 1, 1);
	public final static Color RED = new Color(1, 0, 0);
	public final static Color GREEN = new Color(0, 1, 0);
	public final static Color BLUE = new Color(0, 0, 1);

	private double r, g, b;

	/**
	 * Color constructor
	 * 
	 * @param r The red component
	 * @param g The green component
	 * @param b The blue component
	 * @throws IllegalArgumentException When one of the components isn't included in [0,1]
	 */
	public Color(double r, double g, double b) {
		if(r < 0 || g < 0 || b < 0 || r > 1 || g > 1 || b > 1)
			throw new IllegalArgumentException();

		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * @return The red component
	 */
	public double red() {
		return r;
	}

	/**
	 * @return The green component
	 */
	public double green() {
		return g;
	}

	/**
	 * @return The blue component
	 */
	public double blue() {
		return b;
	}

	/**
	 * Mixes the current color with an another color with a certain proportion
	 * 
	 * @param that The color to mix the first color with
	 * @param proportion The proportion of the color "that" to be sed in the mix
	 * @throws IllegalArgumentException When proportion isn't included in [0,1]
	 * @return A color mix of the 2 given colors with the appropriate proportions
	 */
	public Color mixWith(Color that, double proportion) {
		double r, g, b;

		if(proportion < 0 || proportion > 1) 
			throw new IllegalArgumentException();

		r = that.r * proportion + this.r * (1 - proportion);
		g = that.g * proportion + this.g * (1 - proportion);
		b = that.b * proportion + this.b * (1 - proportion);

		return new Color(r, g, b);
	}

	/**
	 * Encodes the 3 components of a color in one integer
	 * 
	 * @param r The red component
	 * @param g The green component
	 * @param b The blue component
	 * @return A 24-bit value constitued of 8 "red" bits, 8 "green" bits and 8 "blue" bits in that order
	 */
	public int asPackedRGB() {
		int r = sRGBEncode(this.r, 255);
		int g = sRGBEncode(this.g, 255);
		int b = sRGBEncode(this.b, 255);

		return (r << 16) | (g << 8) | b; // Packs all 3 values into one integer
	}

	/**
	 * Encodes a color into an integer sRGB value
	 * @param v The color index between 0 and 1
	 * @param max The maximum value of the result
	 * @return The integer part of the product of the encoded value and the maximum
	 */
	public static int sRGBEncode(double v, int max) {
		double encodedValue;

		if(v <= 0.0031308) {
			encodedValue = 12.92 * v;
		}
		else {
			encodedValue = 1.055 * Math.pow(v, 1 / 2.4) - 0.055;
		}

		return (int)(encodedValue * max);
	}
}
