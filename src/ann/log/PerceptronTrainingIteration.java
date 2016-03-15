/*
 * PerceptionTrainingIteration
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */package ann.log;

import java.util.HashMap;

public class PerceptronTrainingIteration {
	protected HashMap<Integer,Double> inputValues,targetOutput,actualOutput;
	protected HashMap<String,Double> initalWeights,finalWeights;
	protected Double error, correction;
	
	public PerceptronTrainingIteration() {
		inputValues = new HashMap<Integer,Double>();
		actualOutput = new HashMap<Integer,Double>();
		targetOutput = new HashMap<Integer,Double>();
		initalWeights = new HashMap<String,Double>();
		finalWeights = new HashMap<String,Double>();
		error = null;
		correction = null;
	}
	
	public void addInputValue(int index, double value) {
		inputValues.put(index, value);
	}
	
	public HashMap<Integer,Double> getInputValues() {
		return inputValues;
	}
	
	public void addActualOutput(int index, double value) {
		actualOutput.put(index, value);
	}
	
	public HashMap<Integer,Double> getActualOutputs() {
		return actualOutput;
	}
	
	public void addTargetOutput(int index, double value) {
		targetOutput.put(index, value);
	}
	
	public HashMap<Integer,Double> getTargetOuputs() {
		return targetOutput;
	}
	
	public void addInitialWeight(String key, double value) {
		initalWeights.put(key, value);
	}
	
	public HashMap<String,Double> getInitialWeight() {
		return initalWeights;
	}
	
	public void addFinalWeight(String key, double value) {
		finalWeights.put(key, value);
	}
	
	public HashMap<String,Double> getFinalWeights() {
		return finalWeights;
	}
	
	public void setError(double e) {
		error = new Double(e);
	}
	
	public Double getError() {
		return error;
	}
	
	public void setCorrection(double c) {
		correction = new Double(c);
	}
	
	public double Correction() {
		return correction;
	}
}
