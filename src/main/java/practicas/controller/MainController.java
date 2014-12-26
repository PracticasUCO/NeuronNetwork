package practicas.controller;

import java.io.IOException;

import neuron_network.NetworkData;
import practicas.gui.MainWindow;


public class MainController {
    
    private static MainWindow window;
    private static NetworkData trainData;
    private static NetworkData testData;
    
    public static void setTrainData(String path) {
	System.out.println("Leyendo " + path + " como datos de entrenamiento");
	
	try {
	    trainData.reload_data(path);
	}
	catch (IOException e) {
	    System.err.println("Error al cargar el archivo.");
	    trainData.clearData();
	}
	
	checkData();
    }
    
    public static void setTestData(String path) {
	System.out.println("Leyendo " + path + " como datos de test");
	
	try {
	    testData.reload_data(path);
	}
	catch (IOException e) {
	    System.err.println("Error al cargar el archivo.");
	    testData.clearData();
	}
	
	checkData();
    }
    
    public static void trainData() {
	window.clearOutput();
	window.appendOutput("Configurando red con los siguientes parametros: ");
	window.appendOutput("Factor de aprendizaje: " + window.getLearningFactor());
	window.appendOutput("Factor de inercia: " + window.getInertiaFactor());
	window.appendOutput("Mejora mínimia: " + window.getMinimumImprovement());
	window.appendOutput("Capas ocultas: " + window.getHiddenLayers());
	window.appendOutput("Neuronas por capa oculta: " + window.getHiddenNeurons());
	window.appendOutput("Iteraciones máximas: " + window.getMaxiter());
	window.appendOutput("Repeticiones del algoritmo: " + window.getTimes());
	window.appendOutput("");
    }

    public static void initController() {
	window = new MainWindow();
	trainData = new NetworkData();
	testData = new NetworkData();
	checkData();
    }

    public static void run() {
	window.setVisible(true);
    }
    
    private static void checkData() {
	if((trainData.patrons_length() == 0) && (testData.patrons_length() == 0)) {
	    window.setStatus("Faltan los datos de entrenamiento y de test");
	    window.disableTrain();
	}
	else if(trainData.patrons_length() == 0) {
	    window.setStatus("Faltan los datos de entrenammiento");
	    window.disableTrain();
	}
	else if(testData.patrons_length() == 0) {
	    window.setStatus("Faltan los datos de test");
	    window.disableTrain();
	}
	else if(trainData.inputs_length() != testData.inputs_length()) {
	    System.err.println("Los datos de entrenamiento y test no coinciden: ");
	    System.err.println("Numero de entradas de los datos de entrenamiento: " + trainData.inputs_length());
	    System.err.println("Numero de entradas de los datos de test: " + testData.inputs_length());
	    
	    window.setStatus("Datos de entrenamiento y test con distintas entradas");
	    window.disableTrain();
	}
	else if(trainData.outputs_length() != testData.outputs_length()) {
	    System.err.println("Los datos de entrenamiento y test no coinciden: ");
	    System.err.println("Numero de salidas de los datos de entrenamiento: " + trainData.outputs_length());
	    System.err.println("Numero de salidas de los datos de test: " + testData.outputs_length());
	    
	    window.setStatus("Los datos de entrenammiento y test con distintas salidas.");
	    window.disableTrain();
	}
	else {
	    window.setStatus("Listo!");
	    window.enableTrain();
	}
    }

}
