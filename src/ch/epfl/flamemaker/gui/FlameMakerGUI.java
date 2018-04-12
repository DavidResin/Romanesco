package ch.epfl.flamemaker.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import ch.epfl.flamemaker.geometry2d.*;
import ch.epfl.flamemaker.color.*;
import ch.epfl.flamemaker.flame.*;
import java.util.ArrayList;

public class FlameMakerGUI {
	/* Fractal related attributes */
    private Flame.Builder fractal;
    private ArrayList<FlameTransformation> transformations = new ArrayList<FlameTransformation>(); 
    private Rectangle frame = new Rectangle(new Point(0.1, 0.1), 3, 3);
    private Palette palette;
    
    private int selectedTransformationIndex;
    
    private ArrayList<TransformationListListener> observers = new ArrayList<TransformationListListener>();

	public void start() {
		createFractal();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}catch (Exception e) {}
		
		/* Declaring all the components of the UI */
        JFrame window = new JFrame("FlameMaker");
        
	    JPanel supPanel = new JPanel();		
	    JPanel transformationPanel = new JPanel(); 
	    JPanel fractalPanel = new JPanel();	
	    JPanel infPanel = new JPanel();	
	    JPanel editorPanel = new JPanel();
	    JPanel buttonPanel = new JPanel();
	            
        JComponent fractalVisualizer = new FlameBuilderPreviewComponent(fractal, Color.BLACK, palette, frame, 50);
        JComponent transformationVisualizer = new AffineTransformationComponent(fractal, frame);
        
        final JButton suppressButton = new JButton("Supprimer");
        final JButton addButton = new JButton("Ajouter");

	    final TransformationsListModel transformationListModel = new TransformationsListModel();
        final JList<String> transformationList = new JList<String>(transformationListModel);
        JScrollPane listPanel = new JScrollPane(transformationList);
		
        /* Configuring a listener to update the selected transformation in the JList */
        ListSelectionListener listSelection = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                setSelectedTransformationIndex(transformationList.getSelectedIndex());
            }
        };
        transformationList.addListSelectionListener(listSelection); // transformationList (JList) will notify the listener

        /* Configuring the buttons */
        suppressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int lastSelectedIndex = transformationList.getSelectedIndex();
            	
                transformationListModel.removeTransformation(selectedTransformationIndex);
                if(lastSelectedIndex < transformationListModel.getSize()-1) {
                	transformationList.setSelectedIndex(lastSelectedIndex);
                }
                else {
                	transformationList.setSelectedIndex(transformationListModel.getSize()-1);
                }
                
                if(transformationListModel.getSize() == 1) {
                	suppressButton.setEnabled(false);
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transformationListModel.addTransformation();
                transformationList.setSelectedIndex(transformationListModel.getSize()-1);
                transformationList.ensureIndexIsVisible(transformationList.getSelectedIndex());
                suppressButton.setEnabled(true);
            }
        });
        
        /* Adding the affine Transformation visualizer as an observer.
         * It can be notified later when the selected transformation has been modified
         */
        addObserver((TransformationListListener)transformationVisualizer);

        /* Setting the borders of all the 3 main panels */
        transformationPanel.setBorder(BorderFactory.createTitledBorder("Transformations affines"));
        fractalPanel.setBorder(BorderFactory.createTitledBorder("Fractale"));
        editorPanel.setBorder(BorderFactory.createTitledBorder("Transformations"));

        /* Setting all panels' layouts */
        transformationPanel.setLayout(new BorderLayout());
        infPanel.setLayout(new BoxLayout(infPanel, BoxLayout.LINE_AXIS));
        supPanel.setLayout(new GridLayout(1, 2));
        fractalPanel.setLayout(new BorderLayout());				
        editorPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(1, 2));
        window.getContentPane().setLayout(new BorderLayout());
		
		transformationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		transformationList.setVisibleRowCount(3);
		transformationList.setSelectedIndex(0);
		
        buttonPanel.add(suppressButton);
        buttonPanel.add(addButton);
		fractalPanel.add(fractalVisualizer);
		transformationPanel.add(transformationVisualizer);
		supPanel.add(transformationPanel);
		supPanel.add(fractalPanel);
		editorPanel.add(listPanel, BorderLayout.CENTER);
		editorPanel.add(buttonPanel, BorderLayout.PAGE_END);
		infPanel.add(editorPanel);
		window.getContentPane().add(supPanel, BorderLayout.CENTER);
		window.getContentPane().add(infPanel, BorderLayout.PAGE_END);
		
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);  
	}
    
    private void createFractal() {
        /* Variations to be applied to the affine transformations */
        double[] variation1 = {0.5, 0, 0, 0.4, 0, 0};
        double[] variation2 = {1, 0, 0.1, 0, 0, 0};
        double[] variation3 = {1, 0, 0, 0, 0, 0};
        AffineTransformation transformation1 = new AffineTransformation(0.7124807, -0.4113509, -0.3, 0.4113513, 0.7124808, -0.7);
        AffineTransformation transformation2 = new AffineTransformation(0.3731079, -0.6462417, 0.4, 0.6462414, 0.3731076, 0.3);
        AffineTransformation transformation3 = new AffineTransformation(0.0842641, -0.314478, -0.1, 0.314478, 0.0842641, 0.3);

        /* Creating a list of transformations */
        transformations.add(new FlameTransformation(transformation1, variation1));
        transformations.add(new FlameTransformation(transformation2, variation2));
        transformations.add(new FlameTransformation(transformation3, variation3));

        fractal = new Flame.Builder(new Flame(transformations));	

        ArrayList<Color> colorList = new ArrayList<Color>();
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        palette = new InterpolatedPalette(colorList);
    }
    
    public int getSelectedTransformationIndex() {
        return selectedTransformationIndex;
    }
    
    public void setSelectedTransformationIndex(int index) {
        selectedTransformationIndex = index;
        /* Is our notifier, implements the observer pattern */
        for(TransformationListListener o : observers)
        	o.update(selectedTransformationIndex);
    }
    
    public void addObserver(TransformationListListener observer) {
    	observers.add(observer);
    }
    
    public void removeObserver(int index) {
    	observers.remove(index);
    }
	
	@SuppressWarnings("serial")
	private class TransformationsListModel extends AbstractListModel<String> {		
		public TransformationsListModel() {
		}
		
		public int getSize() {
			return fractal.transformationCount();
		}
		
		public String getElementAt(int index) {
			return "Transformation n° " + (index+1);
		}
		
		public void addTransformation() {
			double[] linearVariation = {1, 0, 0, 0, 0, 0};
			fractal.addTransformation(new FlameTransformation(AffineTransformation.IDENTITY, linearVariation));
			fireIntervalAdded(this, getSize()-1, getSize()-1);
		}
		
		public void removeTransformation(int index) {
			fractal.removeTransformation(index);
			fireIntervalRemoved(this, index, index);
		}
	}

    public interface TransformationListListener {
        public void update(int index);
    }
}

