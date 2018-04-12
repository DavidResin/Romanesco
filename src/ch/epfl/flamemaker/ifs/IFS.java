package ch.epfl.flamemaker.ifs;

import java.util.List;
import java.util.ArrayList;

import ch.epfl.flamemaker.geometry2d.*;

import java.util.Random;

final class IFS {
	List<AffineTransformation> transformationList = new ArrayList<AffineTransformation>();
	
	public IFS(List<AffineTransformation> transformations) {
        
        for(AffineTransformation t : transformations) {
            transformationList.add(t);
        } 
	}
	
	public IFSAccumulator compute(Rectangle frame, int width, int height, int density) {
        IFSAccumulatorBuilder ifs = new IFSAccumulatorBuilder(frame, width, height);
        Random rand = new Random();
		int iterations = density * width * height;
        int n = transformationList.size();
        int i;
        Point p = new Point(0, 0);

		for (int m = 0; m < iterations; m++) {
            i = rand.nextInt(n);
            p = (transformationList.get(i)).transformPoint(p);
            ifs.hit(p);
		}

        return ifs.build();
	}
}
