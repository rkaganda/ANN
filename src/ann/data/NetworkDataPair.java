/*
 * NetworkDataPair
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.data;

import java.util.Vector;

public class NetworkDataPair {
	protected Vector<Double> inputData, outputData;
	
	protected NetworkDataPair() {
		inputData = new Vector<Double>();
		outputData = new Vector<Double>();
	}
	
	public NetworkDataPair(Vector<Double> i, Vector<Double> o) {
		this();
		inputData = i;
		outputData = o;
	}
	
	public Vector<Double> getInputData() {
		return inputData;
	}
	
	public Vector<Double> getOutputData() {
		return outputData;
	}
}
