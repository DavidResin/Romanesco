package ch.epfl.flamemaker.gui;

import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import ch.epfl.flamemaker.flame.*;
import ch.epfl.flamemaker.color.*;
import ch.epfl.flamemaker.geometry2d.*;
import java.awt.Dimension;

@SuppressWarnings("all")
public class FlameBuilderPreviewComponent extends JComponent {
    private Flame.Builder fractalBuild;	
    private Color background;
    private Palette palette;
    private Rectangle frame;
    private int density;

	public FlameBuilderPreviewComponent(Flame.Builder fractalBuild, Color background, Palette palette, Rectangle frame, int density) {
        this.fractalBuild = fractalBuild;
        this.background = background;
        this.palette = palette;
        this.frame = frame;
        this.density = density;        
	}

    @Override
    public void paintComponent(Graphics g) {
    	
    	Graphics2D g1 = (Graphics2D)g;
    	
    	BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    	
        Rectangle idealFrame = frame.expandToAspectRatio((double)getWidth()/(double)getHeight());
        FlameAccumulator fractal = fractalBuild.build().compute(idealFrame, getWidth(), getHeight(), density);

        for(int i = 0; i < fractal.height(); i++) {
            for(int k = 0; k < fractal.width(); k++) {
                image.setRGB(k, i, fractal.color(palette, background, k, fractal.height()-i-1).asPackedRGB());
            }
        }
        
        g1.drawImage(image, 0, 0, null);				
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200); 
    }
}
