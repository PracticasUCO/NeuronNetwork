/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicas.controller;

import neuron_network.NetworkData;
import practicas.gui.MainWindow;

/**
 *
 * @author gowikel
 */
public class MainController {
   
   private MainController() {
      trainInput = new NetworkData();
      testInput = new NetworkData();
      window = new MainWindow();
      trainInputIsCorrect = false;
      testInputIsCorrect = false;
      minimumImprovementIsCorrect = true;
   }
   
   public static void initController() {
      /* Set GTK+ look and feel. If not avaliable it sets default look and feel
       */
      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("GTK+".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               System.out.println("Using " + info.getName() + " look and feel");
               break;
            }
         }
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      getInstance();
   }
   
   public static void setTrainInput(String path) {
      try {
         trainInput.reload_data(path);
         trainInputIsCorrect = true;
         window.setStatus("Fichero " + path + " cargado con exito");
      }
      catch(Exception e) {
         window.setStatus("Fichero de entrenamiento invalido.");
         window.disableTrain();
         trainInputIsCorrect = false;
      }
      
      if(canTrain()) {
         window.enableTrain();
      }
   }
   
   public static void setMinimumImprovement(double value) {
      if((value < 0) || (value > 1)) {
         window.setStatus("Mejora mínima no valida");
         window.disableTrain();
         minimumImprovementIsCorrect = false;
      }
      else {
         minimumImprovement = value;
         minimumImprovementIsCorrect = true;
         window.setStatus("Mejora mínima: " + value);
         if(canTrain()) {
            window.enableTrain();
         }
      }
   }
   
   public static void setTestInput(String path) {
      try {
         testInput.reload_data(path);
         testInputIsCorrect = true;
         window.setStatus("Fichero " + path + " cargado con exito");
      }
      catch(Exception e) {
         window.setStatus("Fichero de test invalido");
         window.disableTrain();
         testInputIsCorrect = false;
      }
      
      if(canTrain()) {
         window.enableTrain();
      }
   }
   
   public static void trainData() {
      
   }
   
   public static void run() {
      java.awt.EventQueue.invokeLater(() -> {
         window.setVisible(true);
      });
   }
   
   public static MainController getInstance() {
      return MainControllerHolder.INSTANCE;
   }
   
   private static class MainControllerHolder {

      private static final MainController INSTANCE = new MainController();
   }
   
   private static boolean canTrain() {
      return trainInputIsCorrect && testInputIsCorrect && minimumImprovementIsCorrect;
   }
   
   private static NetworkData trainInput;
   private static NetworkData testInput;
   private static double minimumImprovement;
   private static MainWindow window;
   private static boolean trainInputIsCorrect;
   private static boolean testInputIsCorrect;
   private static boolean minimumImprovementIsCorrect;
}
