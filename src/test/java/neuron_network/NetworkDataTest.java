package neuron_network;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;
import java.net.URL;
import java.io.IOException;

/**
 * Tests for {@link NetworkData}.
 *
 * @author gowikel@gmail.com {Pedro José Piquero Plaza}
 */
public class NetworkDataTest {
    private NetworkData _nd;
    private NetworkData _xor_input;
    
    @Before
    public void setUp() throws IOException {
	_nd = new NetworkData();
	
	URL url = Thread.currentThread().getContextClassLoader().getResource("neuron_network/xor.dat");
	_xor_input = new NetworkData(url.getPath());
    }

    @Test
    public void checkDefaultParameters() {
	assertTrue("The initial number of loaded patrons must be zero.", _nd.patrons_length() == 0);
	assertTrue("The initial number of inputs length must be zero.", _nd.inputs_length() == 0);
	assertTrue("The initial number of outputs length must be zero.", _nd.outputs_length() == 0);
    }

    @Test
    public void checkTotalPatronsRead() {
	assertTrue("The number of patrons in the xor file is 4", _xor_input.patrons_length() == 4);
    }

    @Test
    public void checkInputsLength() {
	assertTrue("The length of the inputs in xor file is 2.", _xor_input.inputs_length() == 2);
    }

    @Test
    public void checkOutputsLength() {
	assertTrue("The length of the outputs in xor file is 1.", _xor_input.outputs_length() == 1);
    }

    @Test
    public void checkInputs() {
	ArrayList<ArrayList<Double>> allInputs = new ArrayList<ArrayList<Double>>();
	ArrayList<Double> keyA = new ArrayList<Double>();
	ArrayList<Double> keyB = new ArrayList<Double>();
	ArrayList<Double> keyC = new ArrayList<Double>();
	ArrayList<Double> keyD = new ArrayList<Double>();
	
	for(ArrayList<Double> key : _xor_input){
	    allInputs.add(key);
	}

	keyA.add(1d);
	keyA.add(-1d);
	keyB.add(-1d);
	keyB.add(-1d);
	keyC.add(-1d);
	keyC.add(1d);
	keyD.add(1d);
	keyD.add(1d);

	assertTrue("Network data must contain " + keyA.toString(), allInputs.contains(keyA));
	assertTrue("Network data must contain " + keyB.toString(), allInputs.contains(keyB));
	assertTrue("Network data must contain " + keyC.toString(), allInputs.contains(keyC));
	assertTrue("Network data must contain " + keyD.toString(), allInputs.contains(keyD));
    }

    @Test
    public void checkOutputs() {
	ArrayList<Double> keyA = new ArrayList<Double>();
	ArrayList<Double> keyB = new ArrayList<Double>();
	ArrayList<Double> keyC = new ArrayList<Double>();
	ArrayList<Double> keyD = new ArrayList<Double>();

	keyA.add(1d);
	keyA.add(-1d);
	keyB.add(-1d);
	keyB.add(-1d);
	keyC.add(-1d);
	keyC.add(1d);
	keyD.add(1d);
	keyD.add(1d);

	assertTrue("The output of [1, -1] is 1", _xor_input.get_output(keyA).get(0).doubleValue() == 1);
	assertTrue("The output of [-1, -1] is 0", _xor_input.get_output(keyB).get(0).doubleValue() == 0);
	assertTrue("The output of [-1, 1] is 1", _xor_input.get_output(keyC).get(0).doubleValue() == 1);
	assertTrue("The output of [1, 1] is 0", _xor_input.get_output(keyD).get(0).doubleValue() == 0);
    }
}
