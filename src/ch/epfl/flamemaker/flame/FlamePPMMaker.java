/**
 * Main class of FlameMaker
 * 
 * @author Sydney Hauke
 * @author David Resin
 */

package ch.epfl.flamemaker.flame;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import ch.epfl.flamemaker.color.*;
import ch.epfl.flamemaker.geometry2d.*;

import java.util.ArrayList;

public class FlamePPMMaker {
    public static void main(String[] args) {

        /*
         * turbulence
         */

        Flame fractalGenerator;	
        FlameAccumulator fractal;

        /* Part of the space to be saved in the image */
        Rectangle frame = new Rectangle(new Point(0.1, 0.1), 3, 3);

        ArrayList<Color> colorList = new ArrayList<Color>();
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        Palette palette = new InterpolatedPalette(colorList);

        /* Variations to be applied to the affine transformations */
        double[] variation1 = {0.5, 0, 0, 0.4, 0, 0};
        double[] variation2 = {1, 0, 0.1, 0, 0, 0};
        double[] variation3 = {1, 0, 0, 0, 0, 0};
        AffineTransformation transformation1 = new AffineTransformation(0.7124807, -0.4113509, -0.3, 0.4113513, 0.7124808, -0.7);
        AffineTransformation transformation2 = new AffineTransformation(0.3731079, -0.6462417, 0.4, 0.6462414, 0.3731076, 0.3);
        AffineTransformation transformation3 = new AffineTransformation(0.0842641, -0.314478, -0.1, 0.314478, 0.0842641, 0.3);

        /* Creating a list of transformations */
        ArrayList<FlameTransformation> transformations = new ArrayList<FlameTransformation>(); 
        transformations.add(new FlameTransformation(transformation1, variation1));
        transformations.add(new FlameTransformation(transformation2, variation2));
        transformations.add(new FlameTransformation(transformation3, variation3));
        fractalGenerator = new Flame(transformations); // Builds a new fractal, ready to be computed
        
        System.out.println("Calcul turbulence en cours...");
        fractal = fractalGenerator.compute(frame, 500, 500, 50);
        generateImage("turbulence.ppm", fractal, palette);
        
        /* 
         * Shark fin
         */
        
        /* Same as the previous fractal */
        transformations = new ArrayList<FlameTransformation>();
        frame = new Rectangle(new Point(-0.25, 0), 5, 4);
        double[] variation11 = {1, 0.1, 0, 0, 0, 0};
        double[] variation12 = {0, 0, 0, 0, 0.8, 1};
        double[] variation13 = {1, 0, 0, 0, 0, 0};
        transformation1 = new AffineTransformation(-0.4113504, -0.7124804, -0.4, 0.7124795, -0.4113508, 0.8);
        transformation2 = new AffineTransformation(-0.3957339, 0, -1.6, 0, -0.3957337, 0.2);
        transformation3 = new AffineTransformation(0.4810169, 0, 1, 0, 0.4810169, 0.9);

        transformations.add(new FlameTransformation(transformation1, variation11));
        transformations.add(new FlameTransformation(transformation2, variation12));
        transformations.add(new FlameTransformation(transformation3, variation13));
        fractalGenerator = new Flame(transformations);	

        System.out.println("Calcul sharkfin en cours...");
        fractal = fractalGenerator.compute(frame, 500, 400, 50);
        generateImage("shark-fin.ppm", fractal, palette);
    }

    /**
     * Generates the final image of the fractal
     * 
     * @param fileName The name of the file to be created
     * @param fractal The finished fractal in a flame accumulator
     * @param palette The palette in order to colorize the fractal during the generation of the image
     */
    public static void generateImage(String fileName, FlameAccumulator fractal, Palette palette) {
        PrintStream file = null;
        Color color;

        try {
            file = new PrintStream(fileName);
        }
        catch(FileNotFoundException e) {
            System.out.println("Fichier non trouve !");
        }

        System.out.println("Ecriture du fichier " + fileName);
        file.println("P3");
        file.println(fractal.width() + " " + fractal.height());
        file.println("100");

        for(int i = fractal.height()-1; i >= 0; i--) {
            for(int j = 0; j < fractal.width(); j++) {
                color = fractal.color(palette, Color.BLACK, j, i);
                file.print(Color.sRGBEncode(color.red(), 100) + " " + Color.sRGBEncode(color.green(), 100) + " " + Color.sRGBEncode(color.blue(), 100) + " ");
            }

            file.println();
        }

        file.close();
    }
}
