package ch.epfl.flamemaker.ifs;

public class IFSAccumulator {
	private boolean[][] accumulator;
	private int height;
	private int width;
	
	/* Constructeur */
	public IFSAccumulator(boolean[][] isHit) {
		height = isHit.length;
		width = isHit[0].length;
		accumulator = new boolean[height][width];
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				accumulator[i][j] = isHit[i][j];
			}
		}
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public boolean isHit(int x, int y) throws IndexOutOfBoundsException {
		if(x >= width || y >= height) {
			throw new IndexOutOfBoundsException();
		}
		
		return accumulator[y][x];
	}
}
