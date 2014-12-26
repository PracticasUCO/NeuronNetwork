package practicas.controller;

import neuron_network.MultilayerPerceptron;

public class TrainController extends Thread {
    
    private MultilayerPerceptron network;

    public TrainController() {
	network = new MultilayerPerceptron();
    }

    public TrainController(Runnable arg0) {
	super(arg0);
	network = new MultilayerPerceptron();
    }

    public TrainController(String arg0) {
	super(arg0);
	network = new MultilayerPerceptron();
    }

    public TrainController(ThreadGroup arg0, Runnable arg1) {
	super(arg0, arg1);
	network = new MultilayerPerceptron();
    }

    public TrainController(ThreadGroup arg0, String arg1) {
	super(arg0, arg1);
	network = new MultilayerPerceptron();
    }

    public TrainController(Runnable arg0, String arg1) {
	super(arg0, arg1);
	network = new MultilayerPerceptron();
    }

    public TrainController(ThreadGroup arg0, Runnable arg1, String arg2) {
	super(arg0, arg1, arg2);
	network = new MultilayerPerceptron();
    }

    public TrainController(ThreadGroup arg0, Runnable arg1, String arg2,
	    long arg3) {
	super(arg0, arg1, arg2, arg3);
	network = new MultilayerPerceptron();
    }
    
    public synchronized void configureNetwork(int hiddenLayers, int hiddenNeurons, int outputNeurons, double minimumImprovement) {
	
    }

}
