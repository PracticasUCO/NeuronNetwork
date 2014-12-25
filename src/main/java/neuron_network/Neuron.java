package neuron_network;

import java.util.ArrayList;

/**
 * The Neuron class represents a neuron inside the neuron network. This object
 * will have a lot of information useful for the correct management of the
 * network.
 * 
 * You can access without any problem to the following attributes: - inputs: It
 * is the inputs coefficients of the neuron. - inputs_changes: You can store
 * here the last input changes that you made. - last_input_changes: You can
 * store the before last input changes that you made. - bias: The bias of the
 * neuron. By default zero. - bias_change: The last change that you made to the
 * bias. - last_bias_change: The before last change that you made to the bias. -
 * delta: You can store here de change that each input must have to improve the
 * final result - output: Here you can store the output of the neuron. Each
 * neuron has one, and only one, output
 * 
 * @author Pedro Jose Piquero Plaza
 * @version 1.0
 */
public class Neuron {
    /**
     * The inputs coefficients of the neuron. It is stored as a ArrayList.
     */
    public ArrayList<Double> inputs;

    /**
     * The last input changes made to the inputs. It must be manually filled.
     */
    public ArrayList<Double> inputsChanges;

    /**
     * The before last input changes. It must be manually filled.
     */
    public ArrayList<Double> lastInputsChanges;

    /**
     * The bias of the neuron.
     */
    public double bias;

    /**
     * The last bias change made to the neuron. It must be manually changed.
     */
    public double biasChange;

    /**
     * The before last bias change to the neuron. It must be manually changed.
     */
    public double lastBiasChange;

    /**
     * The change tha must be done to improve the output of the neuron
     */
    public double delta;

    /**
     * The neuron's output
     */
    public double output;

    /**
     * Basic constructor. It simple intilialized all attributes to zero, except
     * output that will has the value of 0.5.
     * 
     * Notice that inputs, inputs_changes and last_input_changes are double's
     * vectors and will be initialized with a length of zero.
     */
    public Neuron() {
	inputs = new ArrayList<Double>();
	inputsChanges = new ArrayList<Double>();
	lastInputsChanges = new ArrayList<Double>();
	output = 0.5;
    }

    /**
     * It prints the neuron on a String and return it.
     * 
     * @return a String with the neuron information
     **/
    public String toString() {
	String n = "";

	if (inputs.size() > 0) {
	    n += "(";
	    for (int i = 0; i < inputs.size(); i++) {
		n = n + inputs.get(i) + " x i" + i;

		if (i < inputs.size() - 1) {
		    n += ", ";
		} else {
		    n += ") + ";
		}
	    }
	}

	n += bias + " ---Neuron---> " + output;

	return n;
    }
}