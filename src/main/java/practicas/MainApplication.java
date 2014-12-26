/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicas;

import javax.swing.SwingUtilities;

import practicas.controller.MainController;

public class MainApplication {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		MainController.initController();
		MainController.run();
	    }
	});
    }
}
