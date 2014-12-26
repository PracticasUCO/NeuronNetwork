package practicas.controller;

import javax.swing.SwingUtilities;

import practicas.gui.MainWindow;


public class MainController {
    
    private static MainWindow window;

    public static void initController() {
	window = new MainWindow();
    }

    public static void run() {
	window.setVisible(true);
    }

}
