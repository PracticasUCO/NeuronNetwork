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

import javax.swing.SwingWorker;

import neuron_network.MultilayerPerceptron;
import neuron_network.NetworkData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class TrainResults {
	public int iteration;
	public double testError;
	public double trainError;

	public TrainResults(int iteration, double trainError, double testError) {
		this.iteration = iteration;
		this.trainError = trainError;
		this.testError = testError;
	}
}

class FinalReport {
	public double trainMean;
	public double trainDesviation;
	public double testMean;
	public double testDesviation;
	public String network;

	public FinalReport(double trainMean, double trainDesviation,
			double testMean, double testDesviation, String network) {
		this.trainMean = trainMean;
		this.trainDesviation = trainDesviation;
		this.testMean = testMean;
		this.testDesviation = testDesviation;
		this.network = network;
	}
}

public class TrainController extends SwingWorker<FinalReport, TrainResults> {

	private MultilayerPerceptron network;
	public int times;
	public NetworkData trainData;
	public NetworkData testData;
	public int maxiter;
	public double minimumImprovement;
	public MultilayerPerceptron.neuronType neuronType;
	public MultilayerPerceptron.errorToMinimize errorToMinimize;
	public boolean offlineBackpropagation;
	public double learningFactor;
	public double inertiaValue;
	public boolean useBias;
	public int hiddenLayers;
	public int hiddenNeurons;

	public TrainController() {
		super();
		network = new MultilayerPerceptron();
		neuronType = neuronType.SIGMOIDE;
		errorToMinimize = errorToMinimize.MSE;
		learningFactor = network.getLearningFactor();
		inertiaValue = network.getInertiaValue();
	}

	public void setup() {
		network.use_bias = this.useBias;
		network.setHiddenLayersSize(hiddenLayers, hiddenNeurons);
		network.setOutputLayerSize(trainData.outputs_length());
		network.neuronType = this.neuronType;
		network.minimize = this.errorToMinimize;
		network.setInertiaValue(inertiaValue);
		network.setLearningFactor(learningFactor);
		network.spreadOut();
	}

	@Override
	protected FinalReport doInBackground() throws Exception {
		ArrayList<Double> trainError = new ArrayList<Double>();
		ArrayList<Double> testError = new ArrayList<Double>();
		
		setup();
		
		System.out.println("Configurada red neuronal con: ");
		System.out.println(String.format("Numero de capas ocultas: %s - Neuronas por capa oculta: %s",
				network.getNumberOfHiddenLayers(), network.getLayerSize(0)));
		System.out.println(String.format("Tamaño de la capa de salida: %s neuronas",
				network.getOutputLayerSize()));
		System.out.println(String.format("Uso de bias: %s", network.use_bias));
		System.out.println(String.format("Tipo de neuronas: %s", network.neuronType));
		System.out.println(String.format("Funcion a minimizar: %s", network.minimize));
		System.out.println(String.format("Uso de la retropropagación offline: %s", offlineBackpropagation));
		System.out.println(String.format("Factor de aprendizaje: %s", network.getLearningFactor()));
		System.out.println(String.format("Factor de inercia: %s", network.getInertiaValue()));
		System.out.println("");

		for (int i = 0; i < times; i++) {
			network.trainByBackpropagation(trainData, maxiter, minimumImprovement, this.offlineBackpropagation);
			network.spreadOut();
			
			if(errorToMinimize.equals(errorToMinimize.MSE)) {
				trainError.add(network.getMeanSquaredError(trainData));
				testError.add(network.getMeanSquaredError(testData));
			}
			else {
				trainError.add(network.getEntropy(trainData));
				testError.add(network.getEntropy(testData));
			}
			
			System.out.println(String.format("Trainning %s finished with the following error: train: <%s> test: <%s>",
					i + 1, trainError.get(trainError.size() - 1), testError.get(testError.size() - 1)));
			
			publish(new TrainResults(i, trainError.get(trainError.size() - 1), testError.get(testError.size() - 1)));
		}

		double trainMean = 0;
		double testMean = 0;
		double trainDesviation = 0;
		double testDesviation = 0;

		for (Double error : trainError) {
			trainMean += error;
			trainDesviation += Math.pow(error, 2);
		}

		for (Double error : testError) {
			testMean += error;
			testDesviation += Math.pow(error, 2);
		}

		trainMean /= trainError.size();
		trainDesviation /= trainError.size();
		testMean /= testError.size();
		testDesviation /= testError.size();

		trainDesviation -= Math.pow(trainMean, 2);
		testDesviation -= Math.pow(testMean, 2);

		trainDesviation = Math.sqrt(trainDesviation);
		testDesviation = Math.sqrt(testDesviation);

		if (Double.isNaN(trainDesviation)) {
			trainDesviation = 0;
		}

		if (Double.isNaN(testDesviation)) {
			testDesviation = 0;
		}

		return new FinalReport(trainMean, trainDesviation, testMean,
				testDesviation, network.toString());
	}

	@Override
	public void done() {
		FinalReport report = null;
		try {
			report = this.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(String.format("Final reports"));
		System.out.println(String.format("Train phase: %s +- %s", report.trainMean, report.trainDesviation));
		System.out.println(String.format("Test phase: %s +- %s", report.testMean, report.testDesviation));
		
		MainController.processFinalTrain(report.trainMean,
				report.trainDesviation, report.testMean, report.testDesviation,
				report.network);
	}

	@Override
	public void process(List<TrainResults> results) {
		for(TrainResults result : results) {
			MainController.processIntermidiateTrain(result.iteration,
					result.trainError, result.testError);
		}
	}

}
