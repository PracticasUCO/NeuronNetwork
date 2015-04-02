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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultilayerPerceptron}
 **/
public class MultilayerPerceptronTest {
	private MultilayerPerceptron _empty_mp;
	private MultilayerPerceptron _xor_mp;
	private MultilayerPerceptron _xor_2_mp;

	@Before
	public void setUp() {
		_empty_mp = new MultilayerPerceptron();
		_xor_mp = new MultilayerPerceptron(2, 2, 1);
		_xor_2_mp = new MultilayerPerceptron(3, 2, 2);
	}

	@Test
	public void checkEmptyConstructor() {
		assertTrue("learning factor must be by default 0.9",
				_empty_mp.getLearningFactor() == 0.9);
		assertTrue("inertia must be by default 0.1",
				_empty_mp.getInertiaValue() == 0.1);
		assertTrue("default constructor must create one input to the network.",
				_empty_mp.getCurrentInput().size() == 1);
		assertTrue(
				"default constructor must create one output to the network.",
				_empty_mp.getOutputs().size() == 1);
		assertTrue("default hidden layers is 1",
				_empty_mp.getNumberOfHiddenLayers() == 1);
	}

	@Test
	public void checkParametrizedConstructor() {
		assertTrue("learning factor must be by default 0.9",
				_xor_mp.getLearningFactor() == 0.9);
		assertTrue("learning factor must be by default 0.9",
				_xor_2_mp.getLearningFactor() == 0.9);
		assertTrue("inertia must be by default 0.1",
				_xor_mp.getInertiaValue() == 0.1);
		assertTrue("inertia must be by default 0.1",
				_xor_2_mp.getInertiaValue() == 0.1);
		assertTrue("the constructor must create one input to the network",
				_xor_mp.getCurrentInput().size() == 1);
		assertTrue("the constructor must create one input to the network",
				_xor_2_mp.getCurrentInput().size() == 1);
		assertTrue(
				"the constructor must create the specified number of output neurons",
				_xor_mp.getOutputs().size() == 1);
		assertTrue(
				"the constructor must create the specified number of output neurons",
				_xor_2_mp.getOutputs().size() == 2);
		assertTrue(
				"the number of hidden layers must be 2, but is "
						+ _xor_mp.getNumberOfHiddenLayers(),
				_xor_mp.getNumberOfHiddenLayers() == 2);
		assertTrue(
				"the number of hidden layers must be 3, but is "
						+ _xor_2_mp.getNumberOfHiddenLayers(),
				_xor_2_mp.getNumberOfHiddenLayers() == 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkThrowsExceptionWhenYouTryToSetALearningFactorHigherThanOne() {
		_xor_mp.setLearningFactor(2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkThrowsExceptionWhenYouTryToSetALearningFactorLowerThanZero() {
		_xor_mp.setLearningFactor(-0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkThrowsExceptionWhenYouTryToSetAInertiaValueHigherThanOne() {
		_xor_mp.setInertiaValue(1.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkThrowsExceptionWhenYouTryToSetAInertiaValueLowerThanZero() {
		_xor_mp.setInertiaValue(-0.1);
	}

	@Test
	public void checkFeed() {
		ArrayList<Double> two_inputs = new ArrayList<Double>();
		ArrayList<Double> five_inputs = new ArrayList<Double>();

		two_inputs.add(-1D);
		two_inputs.add(1D);

		five_inputs.add(1D);
		five_inputs.add(-1D);
		five_inputs.add(1D);
		five_inputs.add(-1D);
		five_inputs.add(-1D);

		_xor_mp.feed(five_inputs);

		assertTrue("Network was feed with " + five_inputs
				+ " but it is not in the input layer", _xor_mp
				.getCurrentInput().equals(five_inputs));

		_xor_mp.feed(two_inputs);

		assertTrue("Network was feed with " + two_inputs
				+ " but it is not in the input layer", _xor_mp
				.getCurrentInput().equals(two_inputs));

		_xor_mp.feed(five_inputs);

		assertTrue("Network was feed with " + five_inputs
				+ " but it is not in the input layer", _xor_mp
				.getCurrentInput().equals(five_inputs));
	}

	@Test
	public void checkGetOutputLayerSize() {
		assertTrue(
				"A network initialized with the default constructor must have 1 neuron in output layer",
				_empty_mp.getOutputLayerSize() == 1);
		assertTrue("the xor network must have one neuron in the output layer",
				_xor_mp.getOutputLayerSize() == 1);
		assertTrue("the xor network must have two neurons in the output layer",
				_xor_2_mp.getOutputLayerSize() == 2);
	}

	@Test
	public void checkGetNumberOfHiddenLayers() {
		assertTrue(
				"A network initialized with the default constructor must have 1 hidden layer",
				_empty_mp.getNumberOfHiddenLayers() == 1);
		MultilayerPerceptron t = new MultilayerPerceptron(15, 3, 4);
		assertTrue(t.getNumberOfHiddenLayers() == 15);
	}

	@Test
	public void checkGetAndSetBias() {
		assertTrue(_xor_mp.getBias(0, 0) == 0);
		_xor_mp.setBias(0, 0, 3);

		assertTrue(_xor_mp.getBias(0, 0) == 3);
	}

	@Test
	public void checkGetNeuronInputs() {
		assertTrue(_xor_mp.getNeuronInputs(0, 0).size() == _xor_mp
				.getCurrentInput().size());

		ArrayList<Double> nInputs = _xor_mp.getNeuronInputs(0, 0);

		assertTrue(nInputs.size() == _xor_mp.getNeuronInputsSize(0, 0));
	}

	@Test
	public void checkSetAndGetInputs() {
		ArrayList<Double> inputs = new ArrayList<Double>();

		for (int i = 0; i < _xor_mp.getNeuronInputsSize(0, 0); i++) {
			inputs.add(-1D);
		}

		_xor_mp.setNeuronInputs(0, 0, inputs);
		assertTrue(_xor_mp.getNeuronInputs(0, 0).equals(inputs));
		assertFalse(_xor_mp.getNeuronInputs(0, 0) == inputs);

		inputs.add(5D);
		_xor_mp.setNeuronInputs(0, 0, inputs);
		assertFalse(_xor_mp.getNeuronInputs(0, 0).equals(inputs));
	}

	@Test
	public void checkSoftmaxMethod() {
		MultilayerPerceptron mp = new MultilayerPerceptron(2, 2, 2);
		ArrayList<Double> inputs = new ArrayList<Double>();
		ArrayList<Double> output = null;
		double sum = 0;

		inputs.add(-2D);
		inputs.add(2D);

		mp.feed(inputs);
		mp.spreadOut();
		mp.applySoftmax();
		output = mp.getOutputs();

		for (Double v : output) {
			sum += v;
		}

		assertEquals(1, sum, 1e-20);
	}

	@Test
	public void checkRandomInputs() {
		_xor_2_mp.setRandomInputs();
		HashSet<Double> generatedInputs = new HashSet<Double>();
		int numberOfInputs = 0;

		for (int i = 0; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);

				for (Double input : n.inputs) {
					generatedInputs.add(input);
					numberOfInputs++;
					assertTrue("" + input + " is not <= 1", input <= 1);
					assertTrue("" + input + " is not >= -1", input >= -1);
				}

				assertEquals("bias must not change", 0, n.bias, 0);
			}
		}

		for (int i = 0; i < _xor_2_mp.getLayerSize(-1); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);

			for (Double input : n.inputs) {
				generatedInputs.add(input);
				numberOfInputs++;
				assertTrue("" + input + " is not <= 1", input <= 1);
				assertTrue("" + input + " is not >= -1", input >= -1);
			}

			assertEquals(0, n.bias, 0);
		}

		assertTrue("only generated a " + generatedInputs.size() + " of "
				+ numberOfInputs + " inputs different",
				generatedInputs.size() >= numberOfInputs * 0.9999D);

		_xor_2_mp.use_bias = true;
		_xor_2_mp.setRandomInputs();

		generatedInputs.clear();
		numberOfInputs = 0;

		for (int i = 0; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);
				numberOfInputs++;
				generatedInputs.add(n.bias);
				assertTrue("bias must be <= 1 but is " + n.bias, n.bias <= 1);
				assertTrue("bias must be >= -1 but is " + n.bias, n.bias >= -1);
			}
		}

		for (int i = 0; i < _xor_2_mp.getOutputLayerSize(); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);
			numberOfInputs++;
			generatedInputs.add(n.bias);
			assertTrue("bias must be <= 1 but is " + n.bias, n.bias <= 1);
			assertTrue("bias must be >= -1 but is " + n.bias, n.bias >= -1);
		}

		assertTrue("only generated a " + generatedInputs.size() + " of "
				+ numberOfInputs + " bias different",
				generatedInputs.size() >= numberOfInputs * 0.9999D);
	}

	@Test
	public void checkCorrectSizeOfLayers() {
		_xor_2_mp.setHiddenLayersSize(12, 18);

		assertEquals("the number of hidden layers must be 12", 12,
				_xor_2_mp.getNumberOfHiddenLayers());

		for (int i = 0; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			assertEquals("the size of the layer must be 18", 18,
					_xor_2_mp.getLayerSize(i));
		}

		_xor_2_mp.setHiddenLayersSize(6, 3);

		assertEquals("the number of hidden layers must be 6", 6,
				_xor_2_mp.getNumberOfHiddenLayers());

		for (int i = 0; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			assertEquals("the size of the layer  must be 3", 3,
					_xor_2_mp.getLayerSize(i));
		}

		_xor_2_mp.setOutputLayerSize(16);

		assertEquals("the output layer size must be 16", 16,
				_xor_2_mp.getLayerSize(-1));

		_xor_2_mp.setOutputLayerSize(4);

		assertEquals("the output layer size must be 4", 4,
				_xor_2_mp.getLayerSize(-1));
	}

	@Test
	public void checkRelatedNeuronInputs() {
		ArrayList<Double> inputs = new ArrayList<Double>();
		_xor_2_mp.setHiddenLayersSize(12, 18);

		inputs.add(1D);
		inputs.add(1D);
		inputs.add(0D);

		_xor_2_mp.feed(inputs);

		for (int i = 0; i < _xor_2_mp.getLayerSize(0); i++) {
			Neuron n = _xor_2_mp.getNeuron(0, i);

			assertEquals("the length of the neuron inputs must be 3", 3,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 3",
					3, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 3", 3,
					n.lastInputsChanges.size());
		}

		for (int i = 1; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);

				assertEquals("the length of the neuron inputs must be 18", 18,
						n.inputs.size());
				assertEquals(
						"the length of the neuron inputs changes must be 18",
						18, n.inputsChanges.size());
				assertEquals(
						"the length of the neuron last input changes must be 18",
						18, n.lastInputsChanges.size());
			}
		}

		for (int i = 0; i < _xor_2_mp.getLayerSize(-1); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);

			assertEquals("the length of the neuron inputs must be 18", 18,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 18",
					18, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 18",
					18, n.lastInputsChanges.size());
		}

		_xor_2_mp.setHiddenLayersSize(9, 4);

		assertEquals("the number of inputs must be 3", 3, _xor_2_mp
				.getCurrentInput().size());

		for (int i = 0; i < _xor_2_mp.getLayerSize(0); i++) {
			Neuron n = _xor_2_mp.getNeuron(0, i);

			assertEquals("the length of the neuron inputs must be 3", 3,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 3",
					3, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 3", 3,
					n.lastInputsChanges.size());
		}

		for (int i = 1; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);

				assertEquals("the length of the neuron inputs must be 4", 4,
						n.inputs.size());
				assertEquals(
						"the length of the neuron inputs changes must be 4", 4,
						n.inputsChanges.size());
				assertEquals(
						"the length of the neuron last input changes must be 4",
						4, n.lastInputsChanges.size());
			}
		}

		for (int i = 0; i < _xor_2_mp.getLayerSize(-1); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);

			assertEquals("the length of the neuron inputs must be 4", 4,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 4",
					4, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 4", 4,
					n.lastInputsChanges.size());
		}

		inputs.add(13D);

		_xor_2_mp.feed(inputs);

		for (int i = 0; i < _xor_2_mp.getLayerSize(0); i++) {
			Neuron n = _xor_2_mp.getNeuron(0, i);

			assertEquals("the length of the neuron inputs must be 4", 4,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 4",
					4, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 4", 4,
					n.lastInputsChanges.size());
		}

		for (int i = 1; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);

				assertEquals("the length of the neuron inputs must be 4", 4,
						n.inputs.size());
				assertEquals(
						"the length of the neuron inputs changes must be 4", 4,
						n.inputsChanges.size());
				assertEquals(
						"the length of the neuron last input changes must be 4",
						4, n.lastInputsChanges.size());
			}
		}

		for (int i = 0; i < _xor_2_mp.getLayerSize(-1); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);

			assertEquals("the length of the neuron inputs must be 4", 4,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 4",
					4, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 4", 4,
					n.lastInputsChanges.size());
		}

		inputs.clear();
		inputs.add(-1D);
		inputs.add(1D);

		_xor_2_mp.feed(inputs);

		for (int i = 0; i < _xor_2_mp.getLayerSize(0); i++) {
			Neuron n = _xor_2_mp.getNeuron(0, i);

			assertEquals("the length of the neuron inputs must be 2", 2,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 2",
					2, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 2", 2,
					n.lastInputsChanges.size());
		}

		for (int i = 1; i < _xor_2_mp.getNumberOfHiddenLayers(); i++) {
			for (int j = 0; j < _xor_2_mp.getLayerSize(i); j++) {
				Neuron n = _xor_2_mp.getNeuron(i, j);

				assertEquals("the length of the neuron inputs must be 4", 4,
						n.inputs.size());
				assertEquals(
						"the length of the neuron inputs changes must be 4", 4,
						n.inputsChanges.size());
				assertEquals(
						"the length of the neuron last input changes must be 4",
						4, n.lastInputsChanges.size());
			}
		}

		for (int i = 0; i < _xor_2_mp.getLayerSize(-1); i++) {
			Neuron n = _xor_2_mp.getNeuron(-1, i);

			assertEquals("the length of the neuron inputs must be 4", 4,
					n.inputs.size());
			assertEquals("the length of the neuron inputs changes must be 4",
					4, n.inputsChanges.size());
			assertEquals(
					"the length of the neuron last input changes must be 4", 4,
					n.lastInputsChanges.size());
		}
	}

	@Test
	public void checkToStringMethod() {
		String network = _xor_2_mp.toString();
		String network_regex = "Learning factor: \\d+\\.\\d+\\nInertia value: \\d+\\.\\d+\\nNumber of hidden layers: \\d+\\nSize of hidden layers: \\d+\\nSize of output layer: \\d+\\n\\n(Layer \\d+ of \\d+\\n(\\t\\(.*\\) \\+ \\d+\\.\\d+ ---Neuron---> .*\\d+\n)+\\n)+Output layer\\n(.*\\n)+";
		assertTrue(network.matches(network_regex));
	}
}
