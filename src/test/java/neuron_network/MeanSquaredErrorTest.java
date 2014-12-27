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
import java.util.Random;
import java.net.URL;
import java.io.IOException;

public class MeanSquaredErrorTest {
    private MultilayerPerceptron _xor_2_layers;
    private ArrayList<Double> _one_zero;
    private NetworkData data;
    private final double DELTA = 1e-10;

    @Before
    public void setUp() throws IOException {
	_xor_2_layers = new MultilayerPerceptron(2, 2, 1);
	_one_zero = new ArrayList<Double>();

	URL url = Thread.currentThread().getContextClassLoader().getResource("neuron_network/xor.dat");
	data = new NetworkData(url.getPath());

	_one_zero.add(1D);
	_one_zero.add(0D);
    }
    
    @Test
    public void checkGetMeanSquaredError() {
	_xor_2_layers.feed(_one_zero);
	_xor_2_layers.spreadOut();

	ArrayList<Double> desired = new ArrayList<Double>();
	double xor2Output = _xor_2_layers.getOutputs().get(0);
	Random randomGenerator = new Random();

	for(int i = 0; i < 20; i++) {
	    double expected2Output = randomGenerator.nextDouble();
	    double mse = Math.pow(xor2Output - expected2Output, 2);

	    desired.clear();
	    desired.add(expected2Output);
	    
	    assertEquals(mse, _xor_2_layers.getMeanSquaredError(desired), DELTA);
	}
    }

    @Test
    public void checkMeanSquaredErrorWithANetworkData() {
	assertEquals(0.3388368051, _xor_2_layers.getMeanSquaredError(data), DELTA);
    }
}