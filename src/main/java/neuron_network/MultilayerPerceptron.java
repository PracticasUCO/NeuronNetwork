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

package neuron_network;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.security.SecureRandom;
import java.lang.IllegalArgumentException;

/**
 * The MultilayerPerceptron class defines a NeuronNetwork that organizes the
 * neurons by layers.
 * 
 * <p>
 * Usually this network will need almost 3 layers, but it can be higher if
 * needed.
 * </p>
 * 
 * <p>
 * The first layer is called the input layer, because is the layer that will
 * store the network's input at a given moment. This layer has no processing in
 * the given inputs.
 * </p>
 * 
 * <p>
 * Last layer is called output layer, because it determines the output of the
 * neuron network for a specific input.
 * </p>
 * 
 * <p>
 * The rest of the layers are called hidden layers, because you don't have real
 * control over them. They make an process over the data to try to determine an
 * accurate output in the output layer.
 * </p>
 * 
 * <p>
 * Hidden layers are the most useful part of the Neuron Network, because they
 * can process the info in the input layer and try to make an accurate
 * prediction.
 * </p>
 * 
 * @version 1.0.0
 */
public class MultilayerPerceptron {
	private ArrayList<Double> _inputs;
	private ArrayList<Double> _outputs;
	private ArrayList<Neuron> _outputLayer;
	private ArrayList<ArrayList<Neuron>> _hiddenLayers;
	private SecureRandom _randomGenerator;
	
	
	public enum neuronType { SIGMOIDE, SOFTMAX };
	public enum errorToMinimize { MSE, ENTROPY };
	
	/**
	 * It indicates the neuron type of the network. By default
	 * it will be SIGMOIDE.
	 * @see enum neuronType
	 */
	public neuronType neuronType;
	
	/**
	 * It indicates what will be try to minimize in the backpropagation
	 * phase. By default it will be MSE.
	 * @see errorToMinimize
	 */
	public errorToMinimize minimize;

	/**
	 * Learning factor is a param that modify how much the neuron network will
	 * learn of the errors made. By default it is 0.9
	 */
	private double _learningFactor;

	/**
	 * Inertia is a param that affect in the direction of the learning trying to
	 * continue it in the same direction over the time. By default it is 0.1
	 **/
	private double _inertia;

	/**
	 * A flag to config the network to use neurons with bias or without bias.
	 * When true it will use neurons with bias.
	 */
	public boolean use_bias;

	/**
	 * Basic constructor. It initializes a simple neuron network with one input
	 * in the inputs layer, one hidden layer with one neuron and one neuron in
	 * the output layer.
	 */
	public MultilayerPerceptron() {
		_inputs = new ArrayList<Double>();
		_outputs = new ArrayList<Double>();
		_outputLayer = new ArrayList<Neuron>();
		_hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		_randomGenerator = new SecureRandom();

		setHiddenLayersSize(1, 1);
		setOutputLayerSize(1);
		_inputs.add(0D);

		setLearningFactor(0.9);
		setInertiaValue(0.1);

		checkAndConnectAllLayers();
		
		neuronType = neuronType.SIGMOIDE;
		minimize = errorToMinimize.MSE;
	}

	/**
	 * It initializes the neuron network with the specified number of hidden
	 * layers, with the specified number of neurons in each hidden layer and the
	 * specified number of output neurons in the output layer.
	 * 
	 * @param hidden_layers the number of hidden layers
	 * @param hidden_neurons the number of hidden neurons in each hidden_layer
	 * @param output_neurons the number of output neurons in the output layer
	 **/
	public MultilayerPerceptron(int hidden_layers, int hidden_neurons, int output_neurons) {
		_inputs = new ArrayList<Double>();
		_outputs = new ArrayList<Double>();
		_outputLayer = new ArrayList<Neuron>();
		_hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		_randomGenerator = new SecureRandom();

		setHiddenLayersSize(hidden_layers, hidden_neurons);
		setOutputLayerSize(output_neurons);
		_inputs.add(0D);

		setLearningFactor(0.9);
		setInertiaValue(0.1);

		checkAndConnectAllLayers();
		
		neuronType = neuronType.SIGMOIDE;
		minimize = errorToMinimize.MSE;
	}

	/**
	 * It sets the number of hidden layers to the specified n_hidden_layers.
	 * Also it add n_neurons in each layer. It resets all layers, so saved data
	 * will be deleted
	 * 
	 * @param n_hidden_layers the number of hidden layers to be sets
	 * @param n_neurons the number of neurons in each hidden layer.
	 **/
	public void setHiddenLayersSize(int n_hidden_layers, int n_neurons) {
		_hiddenLayers.clear();

		for (int i = 0; i < n_hidden_layers; i++) {
			ArrayList<Neuron> hidden_layer = new ArrayList<Neuron>();

			for (int j = 0; j < n_neurons; j++) {
				Neuron n = new Neuron();
				hidden_layer.add(n);
			}

			_hiddenLayers.add(hidden_layer);
		}

		// checkAndCorrectConnectorsOfHiddenLayers();
		// checkAndCorrectConnectorsBetweenHiddenAndOutputLayer();
		checkAndConnectAllLayers();
	}

	/**
	 * It feeds the network with the inputs given. You can use without problems
	 * the inputs array because it won't affect to the neuron network.
	 * 
	 * @param inputs the list of inputs to the neuron network
	 **/
	public void feed(ArrayList<Double> inputs) {
		_inputs = (ArrayList<Double>) inputs.clone();
		checkAndCorrectConnectorsBetweenInputAndHiddenLayer();
	}

	/**
	 * It returns the output layer.
	 * 
	 * @return a copy of the actual outputs of the neuron.
	 **/
	public ArrayList<Double> getOutputs() {
		return (ArrayList<Double>) _outputs.clone();
	}

	/**
	 * It sets the value of the learning factor. This value must be a double
	 * between 0 and 1 otherwise will throw a InvalidArgumentException
	 * 
	 * @param learning_value the value of the learning param
	 * @throws IllegalArgumentException if the learning_value is not between 0 and 1
	 **/
	public void setLearningFactor(double learning_value) {
		if ((learning_value < 0) || (learning_value > 1)) {
			throw new IllegalArgumentException(
					"learning_value must be between 0 and 1. Actual value: "
							+ learning_value);
		}

		_learningFactor = learning_value;
	}

	/**
	 * It returns the current learning factor of the neuron network
	 * 
	 * @return the learning factor of the neuron network
	 **/
	public double getLearningFactor() {
		return _learningFactor;
	}

	/**
	 * It sets the value of the inertia. This value must be a double between 0
	 * and 1 otherwise will throw a IllegalArgumentException
	 * 
	 * @param inertia_value the new inertia value of the neuron network
	 * @throws IllegalArgumentException if the inertia is not between 0 and 1
	 **/
	public void setInertiaValue(double inertia_value) {
		if ((inertia_value < 0) || (inertia_value > 1)) {
			throw new IllegalArgumentException(
					"inertia value must be between 0 and 1. Actual value: "
							+ inertia_value);
		}

		_inertia = inertia_value;
	}

	/**
	 * It returns current inertia value
	 * 
	 * @return the current inertia value
	 **/
	public double getInertiaValue() {
		return _inertia;
	}

	/**
	 * It gets the current input
	 * 
	 * @return the current input in the network
	 **/
	public ArrayList<Double> getCurrentInput() {
		return _inputs;
	}

	/**
	 * It spread out the inputs from the input layer to the output layer
	 **/
	public void spreadOut() {
		spreadOutBetweenInputLayerAndFirstHiddenLayer();
		spreadOutBetweenHiddenLayers();
		spreadOutBetweenLastHiddenLayerAndOutputLayer();
	}

	/**
	 * It sets the specified inputs on the specified layer. To take effect, the
	 * inputs given must have the same length than the inputs of the neuron.
	 * Otherwise this method will not make anything.
	 * 
	 * @param layer_index the index of the hidden layer to set the inputs or a negative value if it is the output layer
	 * @param neuron_index the index of the neuron inside the layer where to set the inputs
	 * @param inputs the inputs to set
	 * @return a true value if the values were set or false otherwise.
	 **/
	public boolean setNeuronInputs(int layer_index, int neuron_index, ArrayList<Double> inputs) {
		Neuron selectedNeuron = getNeuron(layer_index, neuron_index);

		if (selectedNeuron.inputs.size() == inputs.size()) {
			selectedNeuron.inputs = (ArrayList<Double>) inputs.clone();
			return true;
		}

		return false;
	}

	/**
	 * It returns the inputs of the specified neuron in the specified layer.
	 * 
	 * @param layer_index the index of the layer where the neuron is of a negative value to search in the output layer
	 * @param neuron_index the index of the neuron inside the layer
	 * @return the array with the inputs of the neuron.
	 **/
	public ArrayList<Double> getNeuronInputs(int layer_index, int neuron_index) {
		Neuron selectedNeuron = getNeuron(layer_index, neuron_index);
		return selectedNeuron.inputs;
	}

	/**
	 * It returns the size of the inputs in the specified neuron.
	 * 
	 * @param layer_index the index where the neuron is or a negative index to seach in the output layer
	 * @param neuron_index the index of the neuron inside the layer.
	 * @return the size of the inputs in the neuron
	 **/
	public int getNeuronInputsSize(int layer_index, int neuron_index) {
		Neuron selectedNeuron = getNeuron(layer_index, neuron_index);

		return selectedNeuron.inputs.size();
	}

	/**
	 * It sets the bias on the specified neuron
	 * 
	 * @param layer_index the index where the neuron is or a negative index to search in the output layer
	 * @param neuron_index the index of the neuron inside the layer
	 * @param bias the bias value to be set.
	 **/
	public void setBias(int layer_index, int neuron_index, double bias) {
		Neuron selectedNeuron = getNeuron(layer_index, neuron_index);
		selectedNeuron.bias = bias;
	}

	/**
	 * It returns the bias of the specified neuron
	 * 
	 * @param layer_index the index where the neuron is or a negative index to search in the output layer
	 * @param neuron_index the index of the neuron inside the layer
	 * @return the bias value of the specified neuron
	 **/
	public double getBias(int layer_index, int neuron_index) {
		Neuron selectedNeuron = getNeuron(layer_index, neuron_index);
		return selectedNeuron.bias;
	}

	/**
	 * It returns the number of hidden layers in the network.
	 * 
	 * @return the number of hidden layers in the network
	 **/
	public int getNumberOfHiddenLayers() {
		return _hiddenLayers.size();
	}

	/**
	 * It returns the number of neurons in the output layer.
	 * 
	 * @return the number of neurons in the output layer
	 **/
	public int getOutputLayerSize() {
		return _outputLayer.size();
	}

	/**
	 * It returns the error of the current network's output if the desired
	 * output is desired
	 * 
	 * @param desired the desired output for the neuron network
	 * @return the MSE of the current output
	 **/
	public double getMeanSquaredError(ArrayList<Double> desired) {
		ArrayList<Double> currentOutput = getOutputs();
		double acc = 0;

		for (int i = 0; i < currentOutput.size(); i++) {
			acc += Math.pow(currentOutput.get(i) - desired.get(i), 2);
		}

		acc /= currentOutput.size();

		return acc;
	}

	/**
	 * It returns the mean error of the network with each patron in data. It
	 * spread out all data, so you don't need to do it before
	 * 
	 * @param data a NetworkData with all patrons to be tested
	 * @param applySoftmax Indicate if softmax will be applied to each input
	 * @return the mean of all MSE of all patrons.
	 **/
	protected double getMeanSquaredError(NetworkData data, boolean applySoftmax) {
		double mse = 0;

		for (ArrayList<Double> input : data) {
			feed(input);
			spreadOut();
			
			if(applySoftmax) {
				applySoftmax();
				applyPrediction();
			}
			
			mse += getMeanSquaredError(data.get_output(input));
		}

		mse /= data.patrons_length();

		return mse;
	}
	
	/**
	 * It returns the mean error of the network with each patron in data. It
	 * spread out all data, so you don't need to do it before
	 * 
	 * @param data a NetworkData with all patrons to be tested
	 * @return the mean of all MSE of all patrons.
	 **/
	public double getMeanSquaredError(NetworkData data) {
		if(neuronType == neuronType.SIGMOIDE) {
			return getMeanSquaredError(data, false);
		}
		else {
			return getMeanSquaredError(data, true);
		}
	}
	
	/**
	 * It returns the entropy of the given data.
	 * @param data a NetworkData with all patrons to be tested
	 * @param applySoftmax a flag to indicate if the network must apply a softmax
	 * @return the entropy of the given data
	 */
	protected double getEntropy(NetworkData data, boolean applySoftmax) {
		double entropy = 0;
		
		for(ArrayList<Double> input : data) {
			double e = 0;
			ArrayList<Double> expected = data.get_output(input);
			
			feed(input);
			spreadOut();
			
			if(applySoftmax) {
				applySoftmax();
			}
			
			for(int i = 0; i < this.getOutputLayerSize(); i++) {
				if(this.getOutput(i) != 0D) {
					e += expected.get(i) * Math.log(this.getOutput(i));
				}
			}
			
			entropy += e;
		}
		
		
		entropy *= -1;
		entropy /= (data.patrons_length() * data.outputs_length());
		
		return entropy;
	}
	
	/**
	 * It returns the entropy of the given data.
	 * @param data a NetworkData with all patrons to be tested
	 * @return the entropy of the given data
	 */
	public double getEntropy(NetworkData data) {
		if(neuronType.equals(neuronType.SIGMOIDE)) {
			return getEntropy(data, false);
		}
		else {
			return getEntropy(data, true);
		}
	}
	
	/**
	 * It returns CCR measure to the given data
	 * @param data a NetworkData used to test the network
	 * @param applySoftmax a flag to indicate when apply a softmax to the output
	 * @return the CCR measure
	 */
	protected double getCCR(NetworkData data, boolean applySoftmax) {
		double result = 0;
		
		for(ArrayList<Double> input : data) {
			feed(input);
			spreadOut();
			
			if(applySoftmax) {
				applySoftmax();
				applyPrediction();
			}
			
			if(getOutputs().equals(data.get_output(input))) {
				result++;
			}
		}
		
		
		result /= data.patrons_length();
		
		return result;
	}
	
	/**
	 * It returns CCR measure to the given data
	 * @param data a NetworkData used to test the network
	 * @return the CCR measure
	 */
	public double getCCR(NetworkData data) {
		if(neuronType == neuronType.SIGMOIDE) {
			return getCCR(data, false);
		}
		else {
			return getCCR(data, true);
		}
	}
	
	/**
	 * It puts an 1 to the max value in the outputs and 0 in all rest values 
	 **/
	public void applyPrediction() {
		int bestIndex = Integer.MIN_VALUE;
		double bestValue = Double.MIN_VALUE;
		
		for(int i = 0; i < getOutputLayerSize(); i++) {
			if(getOutput(i) > bestValue) {
				bestValue = getOutput(i);
				bestIndex = i;
			}
		}
		
		for(int i = 0; i < getOutputLayerSize(); i++) {
			if(i == bestIndex) {
				_outputs.set(i, 1D);
			}
			else {
				_outputs.set(i, 0D);
			}
		}
	}

	/**
	 * It applies a softmax function to the current outputs in the outputs
	 * layer.
	 **/
	public void applySoftmax() {
		double sum = 0;

		for (Double v : _outputs) {
			sum += v;
		}

		for (int i = 0; i < getOutputLayerSize(); i++) {
			setOutput(i, getOutput(i) / sum);
		}
	}

	/**
	 * It sets all inputs at random between -1 and 1.
	 * 
	 * Note: the random generator used by the class is a strong generator that
	 * generate truly random number. For this reason is not easy to retreive the
	 * seed used to generate random number and set a random seed wont work.
	 **/
	public void setRandomInputs() {
		for (ArrayList<Neuron> hide_layer : _hiddenLayers) {
			for (Neuron n : hide_layer) {
				for (int i = 0; i < n.inputs.size(); i++) {
					double randomInput = (_randomGenerator.nextDouble() * 2) - 1;
					n.inputs.set(i, randomInput);
				}

				if (use_bias) {
					n.bias = (_randomGenerator.nextDouble() * 2) - 1;
				}
			}
		}

		for (Neuron n : _outputLayer) {
			for (int i = 0; i < n.inputs.size(); i++) {
				double randomInput = (_randomGenerator.nextDouble() * 2) - 1;
				n.inputs.set(i, randomInput);
			}

			if (use_bias) {
				n.bias = (_randomGenerator.nextDouble() * 2) - 1;
			}
		}
	}

	/**
	 * It retrives the number of neurons in the specified layer. You can use -1
	 * as index to retrieve the number of neurons in the output layer
	 * 
	 * @param layer_index the index of the layer to select.
	 * @return the size of the layer
	 **/
	public int getLayerSize(int layer_index) {
		if (layer_index < 0) {
			return _outputLayer.size();
		} else {
			return _hiddenLayers.get(layer_index).size();
		}
	}

	/**
	 * It returns the outputs at th specified index
	 * 
	 * @param index the index of the output to be retrieved
	 * @return the output value at index in the outputs layer
	 **/
	public double getOutput(int index) {
		return _outputs.get(index);
	}

	/**
	 * It converts the network in a string. Ready to be printed
	 * 
	 * @return the neuron network as a string
	 */
	@Override
	public String toString() {
		String network = "";

		network += "Learning factor: " + getLearningFactor() + "\n";
		network += "Inertia value: " + getInertiaValue() + "\n";
		network += "Number of hidden layers: " + getNumberOfHiddenLayers()
				+ "\n";
		network += "Size of hidden layers: " + getLayerSize(0) + "\n";
		network += "Size of output layer: " + getLayerSize(-1) + "\n";
		network += "\n";

		ArrayList<String> labels = new ArrayList<String>();

		for (int i = 0; i < getNumberOfHiddenLayers(); i++) {
			network += "Layer " + (i + 1) + " of " + getNumberOfHiddenLayers()
					+ "\n";

			for (int j = 0; j < getLayerSize(i); j++) {
				network += "\t" + getNeuron(i, j) + j + "\n";
			}

			network += "\n";
		}

		network += "Output layer\n";

		for (int i = 0; i < _outputLayer.size(); i++) {
			network += "\t" + _outputLayer.get(i) + i + "\n";
		}

		network += "\n";

		return network;
	}

	/**
	 * <p>
	 * It makes a online back propagation trying to improve the neuron network
	 * result for the given neuron.
	 * </p>
	 * 
	 * <p>
	 * Because all neuron coefficients will change after this, please, don't
	 * forget to call to spreadOut
	 * </p>
	 * 
	 * @param input the input to the neuron network
	 * @param desiredOutput the output desired for the given input
	 * @throws IllegalArgumentException desiredOutput length is not equal to the length of the output layer
	 */
	public void onlineBackpropagation(ArrayList<Double> input, ArrayList<Double> desiredOutput) {
		feed(input);
		spreadOut();
		
		if(neuronType.equals(neuronType.SOFTMAX)){
			applySoftmax();
		}
		setInputChangesToZero();
		updateDeltas(desiredOutput);
		updateInputChanges();
		adjustWeights();
	}
	
	/**
	 * <p>
	 * It makes a online back propagation trying to improve the neuron network
	 * result for the given neuron.
	 * </p>
	 * 
	 * <p>
	 * Because all neuron coefficients will change after this, please, don't
	 * forget to call to spreadOut
	 * </p>
	 * 
	 * @param data the data used to improve network's neurons
	 * @throws IllegalArgumentException desiredOutput length is not equal to the length of the output layer
	 */
	public void onlineBackpropagation(NetworkData data) {
		for(ArrayList<Double> input : data) {
			onlineBackpropagation(input, data.get_output(input));
		}
	}
	
	/**
	 * <p>
	 * It makes a offline back propagation trying to improve the neuron network
	 * result for the given data.
	 * </p>
	 * 
	 * <p>
	 * An offline back propagation is more efficient than an online 
	 * back propagation, because it will update delta values after
	 * look all patrons.
	 * </p>
	 * 
	 * <p>
	 * Because all neuron coefficients will change after this, please don't
	 * forget to call spreadOut
	 * </p>
	 * 
	 * @param data the data used to improve network's neurons
	 * @throws IllegalArgumentException when desiredOutput length is not equal to the length of the output layer
	 */
	public void offlineBackpropagation(NetworkData data) {
		setInputChangesToZero();
		
		for(ArrayList<Double> input : data) {
			feed(input);
			spreadOut();
			updateDeltas(data.get_output(input));
			updateInputChanges();
		}
		
		adjustWeights();
	}

	/**
	 * It trains the network data with the data given as train_data and check
	 * it's capacity with the data given as test_data.
	 * 
	 * @param trainData data to be used in the training process
	 * @param maxiter max number of iterations in the training process
	 * @param minimumImprovement minimum improvement to continue the training
	 * @param trainListener a Consumer that receives current training error
	 * @param offlineBackpropagation indicate if use an online back propagation or not
	 * @throws IllegalArgumentException if desiredOutput's length in data is not equal to the length of the output layer
	 */
	public void trainByBackpropagation(NetworkData trainData, int maxiter, double minimumImprovement, 
			boolean offlineBackpropagation, Consumer<Double> trainListener) {
		
		setRandomInputs();

		for (int i = 0; i < maxiter; i++) {
			double startError;

			if(minimize.equals(errorToMinimize.MSE)){
				startError = getMeanSquaredError(trainData);
			}
			else {
				startError = getEntropy(trainData);
			}
			
			if(offlineBackpropagation) {
				offlineBackpropagation(trainData);
			}
			else {
				onlineBackpropagation(trainData);
			}

			double endError;
			
			if(minimize.equals(errorToMinimize.MSE)){
				endError = getMeanSquaredError(trainData);
			}
			else {
				endError = getEntropy(trainData);
			}

			trainListener.accept(endError);

			if (Math.abs(endError - startError) < minimumImprovement) {
				break; // End of training
			}
		}	
	}

	/**
	 * It trains the network data with the data given as train_data and check
	 * it's capacity with the data given as test_data.
	 * 
	 * @param trainData data to be used in the training process
	 * @param maxiter max number of iterations in the training process
	 * @param minimumImprovement minimum improvement to continue the training
	 * @throws IllegalArgumentException if desiredOutput's length in data is not equal to the length of the output layer
	 */
	public void trainByBackpropagation(NetworkData trainData, int maxiter, double minimumImprovement) {
		trainByBackpropagation(trainData, maxiter, minimumImprovement, false, d -> {});
	}
	
	/**
	 * It trains the network data with the data given as train_data and check
	 * it's capacity with the data given as test_data.
	 * 
	 * @param trainData data to be used in the training process
	 * @param testData data to be used in the test process
	 * @param maxiter max number of iterations in the training process
	 * @param minimumImprovement minimum improvement to continue the training
	 * @param offlineBackpropagation if true it uses an offline back propagation otherwise it will use an online back propagation
	 * @return an double Array with two elements where first element is train error and last element is test error
	 * @throws IllegalArgumentException if desiredOutput's length in data is not equal to the length of the output layer
	 */
	public void trainByBackpropagation(NetworkData trainData, int maxiter, double minimumImprovement, boolean offlineBackpropagation) {
		trainByBackpropagation(trainData, maxiter, minimumImprovement, offlineBackpropagation, d -> {});
	}
	
	/**
	 * It trains the network data with the data given as train_data and check
	 * it's capacity with the data given as test_data.
	 * 
	 * @param trainData data to be used in the training process
	 * @param testData data to be used in the test process
	 * @param maxiter max number of iterations in the training process
	 * @param minimumImprovement minimum improvement to continue the training
	 * @param trainListener a Consumer that receives current training error
	 * @return an double Array with two elements where first element is train error and last element is test error
	 * @throws IllegalArgumentException if desiredOutput's length in data is not equal to the length of the output layer
	 */
	public void trainByBackpropagation(NetworkData trainData, int maxiter, double minimumImprovement, Consumer<Double> trainListener) {
		trainByBackpropagation(trainData, maxiter, minimumImprovement, false, trainListener);
	}

	/**
	 * It sets at the end of the output layer the specified value
	 * 
	 * @param value the value to be set
	 **/
	protected void addOutput(double value) {
		_outputs.add(value);
	}

	/**
	 * It sets at the specified index of the output layer the specified value
	 * 
	 * @param index the index of the value in the output layer
	 * @param value the value to be set
	 **/
	protected void setOutput(int index, double value) {
		_outputs.set(index, value);
	}

	/**
	 * It clears the outputs in the output layer
	 **/
	protected void clearOutput() {
		_outputs.clear();
	}

	/**
	 * It initializes the number the outputs in the output layer. It also reset
	 * the layer.
	 * 
	 * @param n_neurons the number of neurons to set in the output layer
	 **/
	public void setOutputLayerSize(int n_neurons) {
		_outputLayer.clear();
		clearOutput();

		for (int i = 0; i < n_neurons; i++) {
			Neuron n = new Neuron();
			_outputLayer.add(n);
			addOutput(n.output);
		}

		checkAndCorrectConnectorsBetweenHiddenAndOutputLayer();
	}

	/**
	 * It checks and correct the connectors between input layer and the first
	 * hidden layer. Usually must be called with each feed.
	 **/
	protected void checkAndCorrectConnectorsBetweenInputAndHiddenLayer() {
		ArrayList<Neuron> first_hidden_layer = _hiddenLayers.get(0);

		/*
		 * For each neuron in the first hidden layer I check if the inputs of
		 * this neuron is equal to the length of the inputs given. If the neuron
		 * have more inputs it will be deleted. If the neuron have less inputs,
		 * it will be added.
		 */
		for (Neuron n : first_hidden_layer) {
			if (_inputs.size() < n.inputs.size()) {
				do {
					n.inputs.remove(_inputs.size());
					n.inputsChanges.remove(_inputs.size());
					n.lastInputsChanges.remove(_inputs.size());
				} while (_inputs.size() < n.inputs.size());
			} else if (_inputs.size() > n.inputs.size()) {
				do {
					n.inputs.add(1D);
					n.inputsChanges.add(0D);
					n.lastInputsChanges.add(0D);
				} while (_inputs.size() > n.inputs.size());
			}
		}
	}

	/**
	 * It checks and correct the connector between hidden layers and between
	 * last hidden layer and output layer. Usually must be called once, when you
	 * change the structure of network.
	 **/
	protected void checkAndCorrectConnectorsOfHiddenLayers() {
		for (int i = 1; i < _hiddenLayers.size(); i++) {
			ArrayList<Neuron> currentLayer = _hiddenLayers.get(i);
			ArrayList<Neuron> lastLayer = _hiddenLayers.get(i - 1);

			/*
			 * For each neuron in the current hidden layer I check if the inputs
			 * of this neuron is equal to the length of the last hidden layer.
			 * If the neuron have more inputs it will be deleted. If the neuron
			 * have less inputs, it will be added.
			 * 
			 * Update: I don't need to check if the layer has less inputs
			 * because it won't be posible because before call this method
			 * always go a call to clear.
			 */

			for (Neuron n : currentLayer) {
				while (lastLayer.size() > n.inputs.size()) {
					n.inputs.add(1D);
					n.inputsChanges.add(0D);
					n.lastInputsChanges.add(0D);
				}
			}
		}
	}

	/**
	 * It checks and correct the connectors between hidden layers and output
	 * layers
	 **/
	protected void checkAndCorrectConnectorsBetweenHiddenAndOutputLayer() {
		ArrayList<Neuron> lastHiddenLayer = _hiddenLayers.get(_hiddenLayers
				.size() - 1);

		/*
		 * For each neuron in the output layer I check if the inputs of this
		 * neuron is equal to the length of the last hidden layer. If the neuron
		 * have more inputs it will be deleted. If the neuron have less inputs,
		 * it will be added.
		 */

		for (Neuron n : _outputLayer) {
			if (lastHiddenLayer.size() < n.inputs.size()) {
				do {
					n.inputs.remove(lastHiddenLayer.size());
					n.inputsChanges.remove(lastHiddenLayer.size());
					n.lastInputsChanges.remove(lastHiddenLayer.size());
				} while (lastHiddenLayer.size() < n.inputs.size());
			} else if (lastHiddenLayer.size() > n.inputs.size()) {
				do {
					n.inputs.add(1D);
					n.inputsChanges.add(0D);
					n.lastInputsChanges.add(0D);
				} while (lastHiddenLayer.size() > n.inputs.size());
			}
		}
	}

	/**
	 * It checks and connect all layers
	 **/
	protected void checkAndConnectAllLayers() {
		checkAndCorrectConnectorsBetweenInputAndHiddenLayer();
		checkAndCorrectConnectorsOfHiddenLayers();
		checkAndCorrectConnectorsBetweenHiddenAndOutputLayer();
	}

	/**
	 * It selects a neuron and return its reference.
	 * 
	 * @param layer_index the index of the hidden layer where is the neuron or a negative index to select it from output layer
	 * @param neuron_index the index inside the layer where is the neuron.
	 * @return the neuron specified
	 **/
	protected Neuron getNeuron(int layer_index, int neuron_index) {
		ArrayList<Neuron> selectedLayer;

		if (layer_index < 0) {
			selectedLayer = _outputLayer;
		} else {
			selectedLayer = _hiddenLayers.get(layer_index);
		}

		return selectedLayer.get(neuron_index);
	}

	/**
	 * It will update the deltas of each neuron in the network to register the
	 * change that must do the neuron to each input in order to improve his
	 * output
	 * 
	 * @param desiredOutput the output desired by the neuron network
	 */
	protected void updateDeltas(ArrayList<Double> desiredOutput) {
		spreadOut();
		updateOutputDeltas(desiredOutput);
		updateHiddenLayersDeltas();
	}

	/**
	 * It will update the deltas of the output layer.
	 * 
	 * @param desiredOutput the output desired by the neuron network
	 * @throws IllegalArgumentException if desiredOutput's size is not equal to the output layer size
	 */
	private void updateOutputDeltas(ArrayList<Double> desiredOutput) {
		if (desiredOutput.size() != _outputLayer.size()) {
			String error = "size of desired outputs must be ";
			error += _outputLayer.size();
			error += " but is ";
			error += desiredOutput.size();
			throw new IllegalArgumentException(error);
		}

		if(neuronType.equals(neuronType.SIGMOIDE)) {
			for (int i = 0; i < _outputLayer.size(); i++) {
				Neuron n = _outputLayer.get(i);
				if(minimize.equals(errorToMinimize.MSE)) {
					n.delta = -(desiredOutput.get(i) - n.output);
					n.delta *= n.output;
					n.delta *= (1 - n.output);
				}
				else {
					if(n.output != 0) {
						n.delta = -(desiredOutput.get(i) / n.output);
						n.delta *= n.output;
						n.delta *= (1 - n.output);
					}
					else {
						n.delta = Double.MIN_NORMAL;
					}
				}
			}
		}
		else {
			applySoftmax();
			
			for(int j = 0; j < _outputLayer.size(); j++) {
				Neuron neuronaJ = _outputLayer.get(j);
				double delta = 0;
				
				for(int i = 0; i < _outputLayer.size(); i++) {
					double deltaInterno = 0;
					
					if(minimize.equals(errorToMinimize.MSE)){
						deltaInterno += (desiredOutput.get(i) - this.getOutput(i));
					}
					else {
						if(this.getOutput(i) != 0D){
							deltaInterno += (desiredOutput.get(i) / this.getOutput(i));
						}
						else {
							deltaInterno += Double.MIN_NORMAL;
						}
					}
					
					deltaInterno *= this.getOutput(j);
					
					if(i == j) {
						deltaInterno *= (1 - this.getOutput(i));
					}
					else {
						deltaInterno *= this.getOutput(i);
						deltaInterno *= -1;
					}
					
					delta += deltaInterno;
				}
				
				neuronaJ.delta = -1 * delta;
			}
		}
	}

	/**
	 * It will update the deltas of the hidden layers.
	 */
	private void updateHiddenLayersDeltas() {
		// Last hidden layer
		for (int i = 0; i < _hiddenLayers.get(getNumberOfHiddenLayers() - 1)
				.size(); i++) {
			Neuron n = _hiddenLayers.get(getNumberOfHiddenLayers() - 1).get(i);
			double delta = 0;

			for (int j = 0; j < _outputLayer.size(); j++) {
				Neuron next = _outputLayer.get(j);
				delta += next.delta * next.inputs.get(i);
			}

			delta *= n.output * (1 - n.output);

			n.delta = delta;
		}

		for (int h = getNumberOfHiddenLayers() - 2; h >= 0; h--) {
			ArrayList<Neuron> currentLayer = _hiddenLayers.get(h);
			ArrayList<Neuron> nextLayer = _hiddenLayers.get(h + 1);

			for (int i = 0; i < currentLayer.size(); i++) {
				Neuron n = currentLayer.get(i);
				double delta = 0;

				for (int j = 0; j < nextLayer.size(); j++) {
					Neuron next = nextLayer.get(j);
					delta += next.delta * next.inputs.get(i);
				}

				delta *= n.output * (1 - n.output);

				n.delta = delta;
			}
		}
	}

	/**
	 * Sets all input changes to zero
	 */
	protected void setInputChangesToZero() {
		for (ArrayList<Neuron> layer : _hiddenLayers) {
			for (Neuron n : layer) {
				for (int i = 0; i < n.inputsChanges.size(); i++) {
					n.inputsChanges.set(i, 0D);
				}

				n.biasChange = 0;
			}
		}

		for (Neuron n : _outputLayer) {
			for (int i = 0; i < n.inputsChanges.size(); i++) {
				n.inputsChanges.set(i, 0D);
			}

			n.biasChange = 0;
		}
	}

	/**
	 * It updates the input changes to improve the neuron output
	 */
	protected void updateInputChanges() {
		updateInputChangesBetweenInputLayerAndFirstHiddenLayer();
		updateInputChangesBetweenHiddenLayers();
		updateInputChangesOnOutputLayer();
	}

	/**
	 * It will adjusts the weights of the inputs in the neuron network
	 */
	protected void adjustWeights() {
		for (ArrayList<Neuron> layer : _hiddenLayers) {
			for (Neuron n : layer) {
				for (int i = 0; i < n.inputs.size(); i++) {
					double current = n.inputs.get(i);
					current -= getLearningFactor() * n.inputsChanges.get(i);
					current -= getLearningFactor() * getInertiaValue()
							* n.lastInputsChanges.get(i);
					
					n.inputs.set(i, current);
				}

				if (use_bias) {
					n.bias -= getLearningFactor() * n.biasChange;
					n.bias -= getLearningFactor() * getInertiaValue()
							* n.lastBiasChange;
				}

				n.lastInputsChanges = (ArrayList<Double>) n.inputsChanges
						.clone();
				n.lastBiasChange = n.biasChange;
			}
		}

		for (Neuron n : _outputLayer) {
			for (int i = 0; i < n.inputs.size(); i++) {
				double current = n.inputs.get(i);
				current -= getLearningFactor() * n.inputsChanges.get(i);
				current -= getLearningFactor() * getInertiaValue()
						* n.lastInputsChanges.get(i);
				
				n.inputs.set(i, current);
			}

			if (use_bias) {
				n.bias -= getLearningFactor() * n.biasChange;
				n.bias -= getLearningFactor() * getInertiaValue()
						* n.lastBiasChange;
			}

			n.lastBiasChange = n.biasChange;
			n.lastInputsChanges = (ArrayList<Double>) n.inputsChanges.clone();
		}
	}

	/**
	 * It updates the input changes between input layer and hidden layer
	 */
	private void updateInputChangesBetweenInputLayerAndFirstHiddenLayer() {
		ArrayList<Neuron> layer = _hiddenLayers.get(0);

		for (int i = 0; i < layer.size(); i++) {
			Neuron n = layer.get(i);

			for (int j = 0; j < n.inputsChanges.size(); j++) {
				double actualChange = n.inputsChanges.get(j);
				double nextChange = actualChange + n.delta * _inputs.get(j);
				n.inputsChanges.set(j, nextChange);
			}

			if (use_bias) {
				n.biasChange += n.delta;
			}
		}
	}

	/**
	 * It updates the input changes between hidden layers
	 */
	private void updateInputChangesBetweenHiddenLayers() {
		for (int h = 1; h < getNumberOfHiddenLayers(); h++) {
			ArrayList<Neuron> currentLayer = _hiddenLayers.get(h);
			ArrayList<Neuron> lastLayer = _hiddenLayers.get(h - 1);

			for (int i = 0; i < currentLayer.size(); i++) {
				Neuron n = currentLayer.get(i);

				for (int j = 0; j < n.inputsChanges.size(); j++) {
					Neuron last = lastLayer.get(j);
					double actualChange = n.inputsChanges.get(j);
					double nextChange = actualChange + n.delta * last.output;
					n.inputsChanges.set(j, nextChange);
				}

				if (use_bias) {
					n.biasChange += n.delta;
				}
			}
		}
	}

	/**
	 * It updates the input changes on output layer
	 */
	private void updateInputChangesOnOutputLayer() {
		ArrayList<Neuron> lastLayer = _hiddenLayers
				.get(getNumberOfHiddenLayers() - 1);

		for (int i = 0; i < _outputLayer.size(); i++) {
			Neuron n = _outputLayer.get(i);

			for (int j = 0; j < n.inputsChanges.size(); j++) {
				Neuron last = lastLayer.get(j);
				double actualChange = n.inputsChanges.get(j);
				double nextChange = actualChange + n.delta * last.output;
				n.inputsChanges.set(j, nextChange);
			}

			if (use_bias) {
				n.biasChange += n.delta;
			}
		}
	}

	/**
	 * Spread out between input layer and first hidden layer
	 **/
	private void spreadOutBetweenInputLayerAndFirstHiddenLayer() {
		ArrayList<Neuron> first_hidden_layer = _hiddenLayers.get(0);

		// Spread out between input layer and first hidden layer

		for (int i = 0; i < first_hidden_layer.size(); i++) {
			Neuron n = first_hidden_layer.get(i);
			double acc = 0;

			for (int j = 0; j < _inputs.size(); j++) {
				acc += _inputs.get(j) * n.inputs.get(j);
			}

			if (use_bias) {
				acc += n.bias;
			}

			n.output = 1 / (1 + Math.exp(-1 * acc));
		}
	}

	/**
	 * Spread out between hidden layers
	 **/
	private void spreadOutBetweenHiddenLayers() {
		for (int i = 1; i < _hiddenLayers.size(); i++) {
			ArrayList<Neuron> currentLayer = _hiddenLayers.get(i);
			ArrayList<Neuron> lastHiddenLayer = _hiddenLayers.get(i - 1);

			for (int j = 0; j < currentLayer.size(); j++) {
				Neuron currentNeuron = currentLayer.get(j);
				double acc = 0;

				for (int h = 0; h < lastHiddenLayer.size(); h++) {
					Neuron lastNeuron = lastHiddenLayer.get(h);

					acc += lastNeuron.output * currentNeuron.inputs.get(h);
				}

				if (use_bias) {
					acc += currentNeuron.bias;
				}

				currentNeuron.output = (1 / (1 + Math.exp(-1 * acc)));
			}
		}
	}

	/**
	 * Spread out between last hidden layer and output layer
	 **/
	private void spreadOutBetweenLastHiddenLayerAndOutputLayer() {
		ArrayList<Neuron> lastHiddenLayer = _hiddenLayers.get(_hiddenLayers
				.size() - 1);

		for (int i = 0; i < _outputLayer.size(); i++) {
			Neuron n = _outputLayer.get(i);
			double acc = 0;

			for (int j = 0; j < lastHiddenLayer.size(); j++) {
				Neuron lastNeuron = lastHiddenLayer.get(j);
				acc += lastNeuron.output * n.inputs.get(j);
			}

			if (use_bias) {
				acc += n.bias;
			}

			n.output = 1 / (1 + Math.exp(-1 * acc));
			setOutput(i, n.output);
		}
	}
}
