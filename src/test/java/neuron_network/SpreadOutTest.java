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

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;

public class SpreadOutTest {
    private MultilayerPerceptron _simple_xor;
    private MultilayerPerceptron _xor_1_layer;
    private MultilayerPerceptron _xor_2_layers;
    private ArrayList<Double> _zero_zero;
    private ArrayList<Double> _zero_one;
    private ArrayList<Double> _one_zero;
    private ArrayList<Double> _one_one;
    private double delta;

    @Before
    public void setUp() {
	_xor_1_layer = new MultilayerPerceptron(1, 2, 1);
	_xor_2_layers = new MultilayerPerceptron(2, 2, 1);
	_simple_xor = new MultilayerPerceptron(1,1,1);
	_zero_zero = new ArrayList<Double>();
	_zero_one = new ArrayList<Double>();
	_one_zero = new ArrayList<Double>();
	_one_one = new ArrayList<Double>();

	_zero_zero.add(0D);
	_zero_zero.add(0D);
	_zero_one.add(0D);
	_zero_one.add(1D);
	_one_zero.add(1D);
	_one_zero.add(0D);
	_one_one.add(1D);
	_one_one.add(1D);

	delta = 1e-10;
    }

    @Test
    public void checkSpreadOutWithZeroZero() {
	_simple_xor.feed(_zero_zero);
	_simple_xor.spreadOut();
	
	_xor_1_layer.feed(_zero_zero);
	_xor_1_layer.spreadOut();

	_xor_2_layers.feed(_zero_zero);
	_xor_2_layers.spreadOut();

	double simpleOutput = _simple_xor.getOutputs().get(0);
	double xor1Output = _xor_1_layer.getOutputs().get(0);
	double xor2Output = _xor_2_layers.getOutputs().get(0);
	
	assertEquals(0.6224593312, simpleOutput, delta);
	assertEquals(0.7310585786, xor1Output, delta);
	assertEquals(0.8118562749, xor2Output, delta);
    }

    @Test
    public void checkSpreadOutWithZeroOne() {
	_simple_xor.feed(_zero_one);
	_simple_xor.spreadOut();

	_xor_1_layer.feed(_zero_one);
	_xor_1_layer.spreadOut();

	_xor_2_layers.feed(_zero_one);
	_xor_2_layers.spreadOut();

	double simpleOutput = _simple_xor.getOutputs().get(0);
	double xor1Output = _xor_1_layer.getOutputs().get(0);
	double xor2Output = _xor_2_layers.getOutputs().get(0);
	
	assertEquals(0.6750375273, simpleOutput, delta);
	assertEquals(0.8118562749, xor1Output, delta);
	assertEquals(0.8353064996, xor2Output, delta);
    }

    @Test
    public void checkSpreadOutWithOneZero() {
	_simple_xor.feed(_one_zero);
	_simple_xor.spreadOut();

	_xor_1_layer.feed(_one_zero);
	_xor_1_layer.spreadOut();

	_xor_2_layers.feed(_one_zero);
	_xor_2_layers.spreadOut();

	double simpleOutput = _simple_xor.getOutputs().get(0);
	double xor1Output = _xor_1_layer.getOutputs().get(0);
	double xor2Output = _xor_2_layers.getOutputs().get(0);
	
	assertEquals(0.6750375273, simpleOutput, delta);
	assertEquals(0.8118562749, xor1Output, delta);
	assertEquals(0.8353064996, xor2Output, delta);
    }

    @Test
    public void checkSpreadOutWithOneOne() {
	_simple_xor.feed(_one_one);
	_simple_xor.spreadOut();

	_xor_1_layer.feed(_one_one);
	_xor_1_layer.spreadOut();

	_xor_2_layers.feed(_one_one);
	_xor_2_layers.spreadOut();

	double simpleOutput = _simple_xor.getOutputs().get(0);
	double xor1Output = _xor_1_layer.getOutputs().get(0);
	double xor2Output = _xor_2_layers.getOutputs().get(0);
	
	assertEquals(0.7069873680, simpleOutput, delta);
	assertEquals(0.8534092045, xor1Output, delta);
	assertEquals(0.8464231617, xor2Output, delta);
    }

    @Test
    public void checkSpreadOutInABigNetwork() {
	MultilayerPerceptron mp = new MultilayerPerceptron(20, 20, 3);
	ArrayList<Double> inputs = new ArrayList<Double>();

	inputs.add(-1D);
	inputs.add(-2D);
	inputs.add(0D);
	inputs.add(1D);
	inputs.add(2D);

	final double expectedOutput0 = 0.9999999979;
	final double expectedOutput1 = 0.9999999979;
	final double expectedOutput2 = 0.9999999979;

	mp.feed(inputs);
	mp.spreadOut();

	double mpOutput0 = mp.getOutputs().get(0);
	double mpOutput1 = mp.getOutputs().get(1);
	double mpOutput2 = mp.getOutputs().get(2);

	assertEquals(expectedOutput0, mpOutput0, delta);
	assertEquals(expectedOutput1, mpOutput1, delta);
	assertEquals(expectedOutput2, mpOutput2, delta);
    }

    @Test
    public void checkSpreadOutDontAffectOutputNetworkSize() {
	_simple_xor.feed(_one_one);
	_xor_1_layer.feed(_one_one);
	_xor_2_layers.feed(_one_one);

	_simple_xor.spreadOut();
	_xor_1_layer.spreadOut();
	_xor_2_layers.spreadOut();

	assertEquals(1, _simple_xor.getOutputLayerSize());
	assertEquals(1, _xor_1_layer.getOutputLayerSize());
	assertEquals(1, _xor_2_layers.getOutputLayerSize());
    }

    @Test
    public void checkConsistentOfGetOutputsSizeAfterSpreadOut() {
	_simple_xor.feed(_one_one);
	_xor_1_layer.feed(_one_one);
	_xor_2_layers.feed(_one_one);

	_simple_xor.spreadOut();
	_xor_1_layer.spreadOut();
	_xor_2_layers.spreadOut();

	assertEquals(_simple_xor.getOutputs().size(), _simple_xor.getOutputLayerSize());
	assertEquals(_xor_1_layer.getOutputs().size(), _xor_1_layer.getOutputLayerSize());
	assertEquals(_xor_2_layers.getOutputs().size(), _xor_2_layers.getOutputLayerSize());
    }
}