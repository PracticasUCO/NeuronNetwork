package neuron_network;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CCRTest {
	private MultilayerPerceptron network;
	private NetworkData data;

	@Before
	public void setUp() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("neuron_network/xor_2_outputs.dat");
		data = new NetworkData(url.getPath());

		network = new MultilayerPerceptron(2, 20, data.outputs_length());
	}

	@Ignore
	public void checkCCRResults() {
		
		double correct = 0;
		
		for(ArrayList<Double> input : data) {
			network.feed(input);
			network.spreadOut();
			
			if(network.getOutputs().equals(data.get_output(input))) {
				correct++;
			}
		}
		
		correct /= data.patrons_length();
		double ccr = network.getCCR(data, true);
		
		assertEquals(String.format("Expected: <%s> but was <%s>", correct, ccr), correct, ccr, 0.0);
	}
	
	@Test
	public void checkCCRResultsAfterTrainning() {
		network.use_bias = true;
		network.trainByBackpropagation(data, data, 1000, 1e-10);

		double correct = 0;

		for(ArrayList<Double> input : data) {
			network.feed(input);
			network.spreadOut();
			network.applySoftmax();
			network.applyPrediction();

			if(network.getOutputs().equals(data.get_output(input))) {
				correct++;
			}
		}

		correct /= data.patrons_length();
		double ccr = network.getCCR(data, true);

		assertEquals(String.format("Expected: <%s> but was <%s>", correct, ccr), correct, ccr, 0.0);
	}
	
	@Test
	public void checkCCRIsBetterAfterTrainning() {
		double ccrBeforeTrain = network.getCCR(data, true);
		network.trainByBackpropagation(data, data, 1000, 1e-10);
		double ccrAfterTrain = network.getCCR(data, true);
		
		assertTrue(ccrAfterTrain > ccrBeforeTrain);
	}

}
