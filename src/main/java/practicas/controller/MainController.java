/*
 *  NeuronNetwork: A class collection to build neuron networks
 *  Copyright (C) 2014  Pedro José Piquero Plaza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package practicas.controller;

import java.io.IOException;

import javax.swing.SwingUtilities;

import neuron_network.NetworkData;
import practicas.gui.MainWindow;

public class MainController {

	private static MainWindow window;
	private static NetworkData trainData;
	private static NetworkData testData;
	private static TrainController worker;

	public static void setTrainData(String path) {
		System.out.println("Leyendo " + path + " como datos de entrenamiento");

		try {
			trainData.reload_data(path);
		} catch (IOException e) {
			System.err.println("Error al cargar el archivo.");
			trainData.clearData();
		}

		checkData();
	}

	public static void setTestData(String path) {
		System.out.println("Leyendo " + path + " como datos de test");

		try {
			testData.reload_data(path);
		} catch (IOException e) {
			System.err.println("Error al cargar el archivo.");
			testData.clearData();
		}

		checkData();
	}

	public static void trainData() {
		window.clearOutput();
		window.appendOutput("Configurando red con los siguientes parametros: ");
		window.appendOutput("Factor de aprendizaje: "
				+ window.getLearningFactor());
		window.appendOutput("Factor de inercia: " + window.getInertiaFactor());
		window.appendOutput("Mejora mínimia: " + window.getMinimumImprovement());
		window.appendOutput("Capas ocultas: " + window.getHiddenLayers());
		window.appendOutput("Neuronas por capa oculta: "
				+ window.getHiddenNeurons());
		window.appendOutput("Neuronas con bias: " + window.getUseBias());
		window.appendOutput("Iteraciones máximas: " + window.getMaxiter());
		window.appendOutput("Repeticiones del algoritmo: " + window.getTimes());
		window.appendOutput("");

		window.setProgressBarMaximumValue(window.getTimes() + 2);
		window.setProgressBarValue(0);

		worker = new TrainController();
		worker.times = window.getTimes();
		worker.maxiter = window.getMaxiter();
		worker.minimumImprovement = window.getMinimumImprovement();
		worker.trainData = trainData;
		worker.testData = testData;
		worker.setup(window.getHiddenLayers(), window.getHiddenNeurons(),
				trainData.outputs_length(), window.getUseBias());
		window.setStatus("Entrenamiento 1 de " + window.getTimes());
		window.setProgressBarValue(1);
		window.disableAllButtons();
		worker.execute();
	}

	public static void processIntermidiateTrain(int iteration,
			double trainError, double testError) {
		window.appendOutput("Acabado entrenamiento " + (iteration + 1));
		window.appendOutput("\tError en entrenamiento: " + trainError);
		window.appendOutput("\tError en test: " + testError);
		window.appendOutput("");
		window.setProgressBarValue(iteration + 2);

		if (iteration + 1 < window.getTimes()) {
			window.setStatus("Entrenamiento " + (iteration + 2) + " de "
					+ window.getTimes() + ".");
		} else {
			window.setStatus("Calculando estadisticas.");
		}
	}

	public static void processFinalTrain(double trainMean,
			double trainDesviation, double testMean, double testDesviation,
			String network) {
		window.appendOutput("Entrenamiento finalizado.");
		window.appendOutput("Red neuronal: ");
		window.appendOutput(network);
		window.appendOutput("Estadisticas: ");
		window.appendOutput("Error en entrenamiento: " + trainMean + " +- "
				+ trainDesviation);
		window.appendOutput("Error en test: " + testMean + " +- "
				+ testDesviation);
		window.setProgressBarValue(window.getProgressBarMaximumValue());
		window.setStatus("Entrenamiento finalizado");
		window.enableAllButtons();
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
		if ((trainData.patrons_length() == 0)
				&& (testData.patrons_length() == 0)) {
			window.setStatus("Faltan los datos de entrenamiento y de test");
			window.disableTrain();
		} else if (trainData.patrons_length() == 0) {
			window.setStatus("Faltan los datos de entrenammiento");
			window.disableTrain();
		} else if (testData.patrons_length() == 0) {
			window.setStatus("Faltan los datos de test");
			window.disableTrain();
		} else if (trainData.inputs_length() != testData.inputs_length()) {
			System.err
					.println("Los datos de entrenamiento y test no coinciden: ");
			System.err
					.println("Numero de entradas de los datos de entrenamiento: "
							+ trainData.inputs_length());
			System.err.println("Numero de entradas de los datos de test: "
					+ testData.inputs_length());

			window.setStatus("Datos de entrenamiento y test con distintas entradas");
			window.disableTrain();
		} else if (trainData.outputs_length() != testData.outputs_length()) {
			System.err
					.println("Los datos de entrenamiento y test no coinciden: ");
			System.err
					.println("Numero de salidas de los datos de entrenamiento: "
							+ trainData.outputs_length());
			System.err.println("Numero de salidas de los datos de test: "
					+ testData.outputs_length());

			window.setStatus("Los datos de entrenammiento y test con distintas salidas.");
			window.disableTrain();
		} else {
			window.setStatus("Listo!");
			window.enableTrain();
		}
	}

}
