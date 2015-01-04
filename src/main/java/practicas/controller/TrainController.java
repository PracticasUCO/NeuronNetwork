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

import javax.swing.SwingUtilities;
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
		this.testMean = testDesviation;
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

	public TrainController() {
		super();
		network = new MultilayerPerceptron();
	}

	public void setup(int hiddenLayers, int hiddenNeurons, int outputSize,
			boolean useBias) {
		network.use_bias = useBias;
		network.setHiddenLayersSize(hiddenLayers, hiddenNeurons);
		network.setOutputLayerSize(outputSize);
	}

	@Override
	protected FinalReport doInBackground() throws Exception {
		ArrayList<Double> trainError = new ArrayList<Double>();
		ArrayList<Double> testError = new ArrayList<Double>();

		for (int i = 0; i < times; i++) {
			double[] results = network.trainByBackpropagation(trainData,
					testData, maxiter, minimumImprovement);
			trainError.add(results[0]);
			testError.add(results[1]);
			System.out.println("Acabado entrenamiento " + (i + 1)
					+ " con error en test de " + results[1]);
			publish(new TrainResults(i, results[0], results[1]));
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

		MainController.processFinalTrain(report.trainMean,
				report.trainDesviation, report.testMean, report.testDesviation,
				report.network);
	}

	@Override
	public void process(List<TrainResults> results) {
		TrainResults result = results.get(results.size() - 1);
		MainController.processIntermidiateTrain(result.iteration,
				result.trainError, result.testError);
	}

}
