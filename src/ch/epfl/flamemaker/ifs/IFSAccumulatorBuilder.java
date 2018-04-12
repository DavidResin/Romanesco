package ch.epfl.flamemaker.ifs;
import ch.epfl.flamemaker.geometry2d.*;

public class IFSAccumulatorBuilder {
    private boolean[][] accumulator;
    private Rectangle frame;
    private AffineTransformation scale, translate;

    /* Constructeur */
	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) throws IllegalArgumentException {
		if(height <= 0 || width <= 0) {
			throw new IllegalArgumentException();
		}
        
        this.frame = frame;
        accumulator = new boolean[height][width];
        
        double scaleToAccumulatorX = (width/(frame.right()-frame.left()));
        double scaleToAccumulatorY = (height/(frame.top()-frame.bottom()));

        scale = AffineTransformation.newScaling(scaleToAccumulatorX, scaleToAccumulatorY);
        translate = AffineTransformation.newScaling(scaleToAccumulatorX, scaleToAccumulatorY);
	}

    /* Methode mettant a true la case de l'accumulateur que le point touche */
    public void hit(Point p) {
        Point displacedPoint;
        int x;
        int y;

        if(frame.contains(p)) {
        	displacedPoint = translate.transformPoint(p);
            displacedPoint = scale.transformPoint(displacedPoint);  

            x = (int)Math.floor(displacedPoint.x());
            y = (int)Math.floor(displacedPoint.y());

            accumulator[y][x] = true;
        }
    }

    /* Methode construisant l'accumulateur final */
    public IFSAccumulator build() {
        return new IFSAccumulator(accumulator);
    }
}
