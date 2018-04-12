package ch.epfl.flamemaker.gui;

public class FlameMaker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FlameMakerGUI().start();
            }
        });
	}

}
