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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BackpropagationTest {
	private MultilayerPerceptron _network;
	private ArrayList<Double> _inputs;
	private ArrayList<Double> _outputs;
	private final double SOFT_DELTA = 1e-3;
	
	@Before
	public void setUp() {
		_network = new MultilayerPerceptron(3, 3, 1);
		_inputs = new ArrayList<Double>();
		_outputs = new ArrayList<Double>();
		
		_inputs.add(1D);
		_inputs.add(-1D);
		
		_outputs.add(0D);
		_network.feed(_inputs);
	}
	
	@Test
	public void checkMSEIsLowerAfterBackpropagate() {
		for(int i = 0; i < 100000; i++){
			_network.setRandomInputs();
			_network.spreadOut();
			double actualMse = _network.getMeanSquaredError(_outputs);
			_network.onlineBackpropagation(_inputs, _outputs);
			_network.spreadOut();
			double nextMse = _network.getMeanSquaredError(_outputs);
			
			assertTrue("the network must improve their error over time", nextMse < actualMse);
		}
	}
	
	@Test
	public void checkMSEIsNearToZeroIfYouTryALot() {
		
		for(int j = 0; j < 100; j++) {
			_network.setRandomInputs();
			
			for(int i = 0; i < 1000; i++) {
				_network.onlineBackpropagation(_inputs, _outputs);
			}
			
			_network.spreadOut();
			double mse = _network.getMeanSquaredError(_outputs);
			
			assertEquals(0, mse, SOFT_DELTA);
		}
	}
	
	@Test
	public void checkMSEisLowerAfterBackpropagateWithBias() {
		_network.use_bias = true;
		
		//To be sure that the test was succesfull I repeat it
		// 10000 times.
		
		for(int i = 0; i < 100000; i++){
			_network.setRandomInputs();
			_network.spreadOut();
			double actualMse = _network.getMeanSquaredError(_outputs);
			_network.onlineBackpropagation(_inputs, _outputs);
			_network.spreadOut();
			double nextMse = _network.getMeanSquaredError(_outputs);
			
			assertTrue("the network must improve their error over time. Last error was " + nextMse + " that is >= that " + actualMse, nextMse < actualMse);
		}
	}
	
	@Test
	public void checkMSEIsNearToZeroIfYouTryALotWithBias() {
		_network.use_bias = true;
		
		for(int j = 0; j < 100; j++) {
			_network.setRandomInputs();
			
			for(int i = 0; i < 1000; i++) {
				_network.onlineBackpropagation(_inputs, _outputs);
			}
			
			_network.spreadOut();
			double mse = _network.getMeanSquaredError(_outputs);
			
			assertEquals(0, mse, SOFT_DELTA);
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void checkBackpropagationThrowsExceptionWithUpperDesiredOutput() {
		_outputs.add(2D);
		
		_network.onlineBackpropagation(_inputs, _outputs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void checkBackpropagationThrowsExceptionWithLowerDesiredOutput() {
		_outputs.remove(0);
		
		_network.onlineBackpropagation(_inputs, _outputs);
	}
	
	@Test
	public void checkBackpropagationImprovesGeneralErrors() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("neuron_network/xor.dat");
		NetworkData xor_input = new NetworkData(url.getPath());
		_network.setHiddenLayersSize(2, 25);
		_network.setInertiaValue(0.9);
		
		//Use of bias because in a Xor network will get better results with bias rather that without it
		_network.use_bias = true;
		
		for(int i = 0; i < 100; i++) {
			_network.setRandomInputs();
			double startMse = _network.getMeanSquaredError(xor_input);
			
			for(int j = 0; j < 1000; j++) {
				for(ArrayList<Double> input : xor_input) {
					_network.feed(input);
					_network.spreadOut();
					_network.onlineBackpropagation(input, xor_input.get_output(input));
				}
			}
			
			double endMse = _network.getMeanSquaredError(xor_input);
			
			assertTrue("mse error must be lower after the training. Ending error: " + endMse + " >= " + startMse, endMse < startMse);
		}
	}
	
	@Test
	public void checkTrainByBackpropagation() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("neuron_network/xor.dat");
		NetworkData xor_input = new NetworkData(url.getPath());
		_network.setHiddenLayersSize(2, 25);
		_network.setInertiaValue(0.9);
		_network.use_bias = true;
		
		double startError = _network.getMeanSquaredError(xor_input);
		
		double[] results = _network.trainByBackpropagation(xor_input, xor_input, 1000, 1e-5);
		
		double endError = _network.getMeanSquaredError(xor_input);
		
		assertEquals("trainByBackpropagation must return an array with two elements. It returns " + results.length + " elements", 2, results.length);
		assertTrue("train error must be <= than test error. train error: " + results[0] + " test error: " + results[1], results[0] <= results[1]);
		assertTrue("network error must be lower after the training. Starting error: " + startError + " End error: " + endError, endError < startError);
	}

}