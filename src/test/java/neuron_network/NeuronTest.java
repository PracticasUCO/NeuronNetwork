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

import org.junit.Test;
import org.junit.Before;

import java.util.Random;

/**
 * Tests for {@link Neuron}.
 *
 * @author gowikel@gmail.com {Pedro José Piquero Plaza}
 */
public class NeuronTest {
	private Neuron _neuron;
	private String empty_neuron_regex;
	private String neuron_regex;
	Random random_generator;

	@Before
	public void setUp() {
		_neuron = new Neuron();
		/*
		 * An empty neuron will start with a number (the bias) followed by
		 * ---Neuron---> followed by a number (the output).
		 */
		empty_neuron_regex = "^-{0,1}\\d+(\\.\\d+)? ---Neuron---> Y$";

		/*
		 * A non empty neuron will start with ( followed by the number of each
		 * input. Each of these numbers will be followed by an i plus a number.
		 * Also these numbers will be separated by a ",". When all inputs are
		 * listed you must find a ) plus a number (the bias) plus the string
		 * ---Neuron---> plus a number (the output).
		 */
		neuron_regex = "^\\(-{0,1}\\d+(\\.\\d+)? x X\\d+(, -{0,1}\\d+(\\.\\d+)? x X\\d+)*\\) \\+ -{0,1}\\d+(\\.\\d+)? ---Neuron---> Y$";
		random_generator = new Random();
	}

	@Test
	public void allParamsAreEqualToZero() {
		org.junit.Assert.assertTrue("A neuron must start with zero inputs",
				_neuron.inputs.size() == 0);
		org.junit.Assert.assertTrue(
				"A neuron with zero inputs must have a output of 0.5",
				_neuron.output == 0.5d);
		org.junit.Assert.assertTrue(
				"Neuron must start with bias equal to zero", _neuron.bias == 0);
		org.junit.Assert.assertTrue(
				"Neuron must start with delta equal to zero",
				_neuron.delta == 0);
		org.junit.Assert.assertTrue(
				"Neuron must start with zero input changes",
				_neuron.inputsChanges.size() == 0);
		org.junit.Assert.assertTrue(
				"Neuron must start with zero last input changes",
				_neuron.lastInputsChanges.size() == 0);
		org.junit.Assert.assertTrue(
				"Neuron must start with a bias change equal to zero",
				_neuron.biasChange == 0);
		org.junit.Assert.assertTrue(
				"Neuron must start with a last bias change equal to zero",
				_neuron.lastBiasChange == 0);
	}

	@Test
	public void checkToString() {
		org.junit.Assert.assertTrue(_neuron.toString().matches(
				empty_neuron_regex));

		int nInputs = 10;
		for (int i = 0; i < nInputs; i++) {
			_neuron.inputs.add((random_generator.nextDouble() * 4) - 4);
		}

		_neuron.bias = (random_generator.nextDouble() * 4) - 2;

		org.junit.Assert.assertTrue(_neuron.toString().matches(neuron_regex));
	}
}