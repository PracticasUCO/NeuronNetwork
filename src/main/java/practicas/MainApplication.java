/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicas;

import practicas.controller.MainController;

/**
 *
 * @author gowikel
 */
public class MainApplication {
    public static void main(String[] args) {
	MainController.initController();
	MainController.run();
    }
}
