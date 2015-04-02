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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class stores a lot of inputs of the Neuron Network with their desired
 * output. That info is useful to be used in a MultilayerPerceptron network.
 * 
 * @version 1.0
 */
public class NetworkData implements Iterable<ArrayList<Double>> {
	private int inputsLength; // Number of inputs in each patron
	private int outputsLength; // Number of outputs in each patron
	private HashMap<ArrayList<Double>, ArrayList<Double>> inputs; // The inputs

	// hash map

	/**
	 * Basic constructor. It creates a NetworkData without patrons.
	 */
	public NetworkData() {
		inputs = new HashMap<ArrayList<Double>, ArrayList<Double>>();
	}

	/**
	 * It creates a new NetworkData from the file passed as stream
	 * 
	 * @param file
	 *            The path of the file to be loaded as a String
	 * @throws IOException
	 *             it will be throw a IOException if the file does not exist
	 * @see reload_data
	 */
	public NetworkData(String file) throws IOException {
		inputs = new HashMap<ArrayList<Double>, ArrayList<Double>>();
		reload_data(file);
	}

	/**
	 * It returns the length of each input vector.
	 * 
	 * @return the length of each input vector.
	 */
	public int inputs_length() {
		return inputsLength;
	}

	/**
	 * It returns the length of each output vector.
	 * 
	 * @return the length of each output vector.
	 */
	public int outputs_length() {
		return outputsLength;
	}

	/**
	 * It returns the number of patrons saved
	 * 
	 * @return the number of patrons saved.
	 */
	public int patrons_length() {
		return inputs.size();
	}

	/**
	 * It returns the output of a given input
	 * 
	 * @param input
	 *            the input asociated with the output
	 * @return the output associated with the input
	 */
	public ArrayList<Double> get_output(ArrayList<Double> input) {
		return inputs.get(input);
	}

	/**
	 * It parses a file to load all data stored in it. The file must have a
	 * header with 3 numbers. First number will tell input's length. Second
	 * number will tell the ouput's length. And last number will tell the number
	 * of patrons stored.
	 * 
	 * Then each line will be an instance of the Neuron Network. First input
	 * values will be parsed as input and the rest output values will be parsed
	 * as output.
	 * 
	 * @param file
	 *            The path as string of the data file
	 * @throws IOException
	 *             it throws a IOException if the file does not exist
	 * @throws IOException
	 *             if the file has a wrong structure.
	 */
	public void reload_data(String file) throws IOException {
		File f = new File(file);
		FileReader fr = null;
		BufferedReader br = null;

		fr = new FileReader(f);
		br = new BufferedReader(fr);

		int nEntradas = 0;
		int nSalidas = 0;
		int nPatrons = 0;

		String header = br.readLine();

		if (header != null) {
			String[] fields = header.split("( |\t)+");

			if (fields.length >= 3) {
				nEntradas = Integer.valueOf(fields[0]);
				nSalidas = Integer.valueOf(fields[1]);
				nPatrons = Integer.valueOf(fields[2]);
			} else {
				br.close();
				fr.close();
				throw new IOException("Header is not valid.");
			}
		}

		for (int j = 0; j < nPatrons; j++) {
			String line = br.readLine();
			String[] fields;

			if (line != null) {
				fields = line.split("( |\t)+");
			} else {
				break;
			}

			if (fields.length == nEntradas + nSalidas) {
				ArrayList<Double> entradas = new ArrayList<Double>();
				ArrayList<Double> salidas = new ArrayList<Double>();
				for (int i = 0; i < fields.length; i++) {

					if (i < nEntradas) {
						entradas.add(Double.valueOf(fields[i]));
					} else {
						salidas.add(Double.valueOf(fields[i]));
					}

				}
				inputs.put(entradas, salidas);
			} else {
				br.close();
				fr.close();
				throw new IOException("Data is corrupted.");
			}
		}

		inputsLength = nEntradas;
		outputsLength = nSalidas;

		br.close();
		fr.close();
	}

	/**
	 * It removes all data stored
	 */
	public void clearData() {
		inputsLength = 0;
		outputsLength = 0;
		inputs.clear();
	}

	@Override
	public Iterator<ArrayList<Double>> iterator() {
		return inputs.keySet().iterator();
	}
}