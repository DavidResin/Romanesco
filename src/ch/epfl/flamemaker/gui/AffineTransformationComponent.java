package ch.epfl.flamemaker.gui;

import javax.swing.JComponent;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.geometry2d.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color.*;
import java.awt.geom.Line2D;
import java.awt.Dimension;

@SuppressWarnings("all")
public class AffineTransformationComponent extends JComponent implements FlameMakerGUI.TransformationListListener {
	private final int UNIT_SIZE = 55;
	private int highlightedTransformationIndex;
	private Flame.Builder fractal;
	private Rectangle frame;
	
	public AffineTransformationComponent(Flame.Builder fractal, Rectangle frame) {
		this.fractal = fractal;
		this.frame = frame;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g1 = (Graphics2D)g;
		
    	g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Antialiasing !!!
		
        paintGrid(g1);
        paintTransformations(g1);
	}
	
	public int highLightedTransformationIndex() {
		return highlightedTransformationIndex;
	}
	
	public void setHighlightedTransformationIndex(int index) {
		highlightedTransformationIndex = index;
		repaint();
	}

    private void paintGrid(Graphics g) {
    	Graphics2D g1 = (Graphics2D)g;
    	
		/* The grid */
		g1.setColor(new Color((float)0.9, (float)0.9, (float)0.9));
		
		/* On abscisse */
		for(int i = ((getWidth()/2)%UNIT_SIZE); i < getWidth(); i += UNIT_SIZE) {
			g1.draw(new Line2D.Double(i, 0, i, getHeight()));
		}
		/* On ordonnee */
		for(int i = ((getHeight()/2)%UNIT_SIZE); i < getHeight(); i += UNIT_SIZE) {
			g1.draw(new Line2D.Double(0, i, getWidth(), i));
		}
		
		g1.setColor(Color.white);
		g1.draw(new Line2D.Double(0, getHeight()/2, getWidth(), getHeight()/2));
		g1.draw(new Line2D.Double(getWidth()/2, 0, getWidth()/2, getHeight()));
    }	

    private void paintTransformations(Graphics g) {
		Graphics2D g1 = (Graphics2D)g;
  
		for(int i = 0; i < fractal.transformationCount(); i++) {
			if(i != highlightedTransformationIndex) {
				paintArrows(g1, i, Color.black);
			}
		}
		
		paintArrows(g1, highlightedTransformationIndex, Color.red);
    }
    
    private void paintArrows(Graphics g, int index, Color color) {
    	Graphics2D g1 = (Graphics2D)g;
    	
    	g1.setColor(color);
    	 	
    	int x1 = -1*UNIT_SIZE;
        int x2 = 1*UNIT_SIZE;
        int y1 = 0;
        int y2 = 0;
        AffineTransformation rotation = AffineTransformation.newRotation(-Math.PI/2);
		AffineTransformation transformation = fractal.affineTransformation(index);
		AffineTransformation displace = AffineTransformation.newTranslation(getWidth()/2 + transformation.translationX()*UNIT_SIZE-transformation.translationX(), 
																			getHeight()/2 -transformation.translationY()*UNIT_SIZE+transformation.translationY());
        
    	// First arrow
		Point arrowTail = transformation.transformPoint(new Point(x1, y1));
		Point arrowHead = transformation.transformPoint(new Point(x2, y2));
		Point arrowHeadLeft = transformation.transformPoint(new Point(x2-0.1*UNIT_SIZE, y2-0.1*UNIT_SIZE));
		Point arrowHeadRight = transformation.transformPoint(new Point(x2-0.1*UNIT_SIZE, y2+0.1*UNIT_SIZE));
		arrowTail = new Point(arrowTail.x(), -arrowTail.y());
		arrowHead = new Point(arrowHead.x(), -arrowHead.y());
		arrowHeadLeft = new Point(arrowHeadLeft.x(), -arrowHeadLeft.y());
		arrowHeadRight = new Point(arrowHeadRight.x(), -arrowHeadRight.y());
		arrowTail = displace.transformPoint(arrowTail);
		arrowHead = displace.transformPoint(arrowHead);
		arrowHeadLeft = displace.transformPoint(arrowHeadLeft);
		arrowHeadRight = displace.transformPoint(arrowHeadRight);
		g1.draw(new Line2D.Double(arrowTail.x(), arrowTail.y(), arrowHead.x(), arrowHead.y()));	
		g1.draw(new Line2D.Double(arrowHead.x(), arrowHead.y(), arrowHeadLeft.x(), arrowHeadLeft.y()));
		g1.draw(new Line2D.Double(arrowHead.x(), arrowHead.y(), arrowHeadRight.x(), arrowHeadRight.y()));

		// Second arrow
		arrowTail = transformation.transformPoint(new Point(x1, y1));
		arrowHead = transformation.transformPoint(new Point(x2, y2));
		arrowHeadLeft = transformation.transformPoint(new Point(x2-0.1 * UNIT_SIZE, y2-0.1 * UNIT_SIZE));
		arrowHeadRight = transformation.transformPoint(new Point(x2-0.1 * UNIT_SIZE, y2+0.1 * UNIT_SIZE));
		// Invert for the panel
		arrowTail = new Point(arrowTail.x(), -arrowTail.y());
		arrowHead = new Point(arrowHead.x(), -arrowHead.y());
		arrowHeadLeft = new Point(arrowHeadLeft.x(), -arrowHeadLeft.y());
		arrowHeadRight = new Point(arrowHeadRight.x(), -arrowHeadRight.y());
		// Rotate for the second arrow
		arrowTail = rotation.transformPoint(arrowTail);
		arrowHead = rotation.transformPoint(arrowHead);
		arrowHeadLeft = rotation.transformPoint(arrowHeadLeft);
		arrowHeadRight = rotation.transformPoint(arrowHeadRight);
		
		// Displace to the coordinate of the plane
		arrowTail = displace.transformPoint(arrowTail);
		arrowHead = displace.transformPoint(arrowHead);
		arrowHeadLeft = displace.transformPoint(arrowHeadLeft);
		arrowHeadRight = displace.transformPoint(arrowHeadRight);
		// Finally draw
		g1.draw(new Line2D.Double(arrowTail.x(), arrowTail.y(), arrowHead.x(), arrowHead.y()));	
		g1.draw(new Line2D.Double(arrowHead.x(), arrowHead.y(), arrowHeadLeft.x(), arrowHeadLeft.y()));
		g1.draw(new Line2D.Double(arrowHead.x(), arrowHead.y(), arrowHeadRight.x(), arrowHeadRight.y()));
    }
    
    private Point[] transformShape(Point[] shape, AffineTransformation transformation) {
    	Point[] newShape = new Point[shape.length];
    	
    	for(int i = 0; i < shape.length; i++) {
    		newShape[i] = transformation.transformPoint(shape[i]);
    	}
    	
    	return newShape;
    }
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}
	
	public void update(int index) {
		setHighlightedTransformationIndex(index);
	}
}
