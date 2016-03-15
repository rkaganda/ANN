/*
 * FeedForwardNetworkTrainingIteration
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.log;

import java.util.HashMap;

public class FeedFowardNetworkTrainingIteration {
	protected HashMap<String,Double> inputValues,targetOutput,actualOutput,nodeError,initalWeights,finalWeights,weightCorrection;
	
	public FeedFowardNetworkTrainingIteration() {
		inputValues = new HashMap<String,Double>();
		targetOutput = new HashMap<String,Double>();
		actualOutput = new HashMap<String,Double>();
		nodeError = new HashMap<String,Double>();
		initalWeights = new HashMap<String,Double>();
		finalWeights = new HashMap<String,Double>();
		weightCorrection = new HashMap<String,Double>();
	}
	
	public void logInputValue(int id, double value) {
		inputValues.put(FeedForwardNetworkTrainingLog.createInputValueKey(id), value);
	}
	
	public HashMap<String,Double> getInputValues() {
		return inputValues;
	}
	
	public void logActualOutput(int id, double value) {
		actualOutput.put(FeedForwardNetworkTrainingLog.createActualOutputKey(id), value);
	}
	
	public HashMap<String,Double> getActualOutputs() {
		return actualOutput;
	}
	
	public void logTargetOutput(int id, double value) {
		targetOutput.put(FeedForwardNetworkTrainingLog.createTargetOutputKey(id), value);
	}
	
	public HashMap<String,Double> getTargetOuputs() {
		return targetOutput;
	}
	
	public void logInitialWeight(int inputId, int outputId, double value) {
		initalWeights.put(FeedForwardNetworkTrainingLog.createInitialWeightKey(inputId, outputId), value);
	}
	
	public HashMap<String,Double> getInitialWeight() {
		return initalWeights;
	}
	
	public void logFinalWeight(int inputId, int outputId, double value) {
		finalWeights.put(FeedForwardNetworkTrainingLog.createFinalWeightKey(inputId, outputId), value);
	}
	
	public HashMap<String,Double> getFinalWeights() {
		return finalWeights;
	}
	
	public HashMap<String,Double> getNodeError() {
		return nodeError;
	}
	
	public void logNodeError(int id, double value) {
		nodeError.put(FeedForwardNetworkTrainingLog.createNodeErrorKey(id), value);
	}
	
	public HashMap<String,Double> getWeightCorretion() {
		return weightCorrection;
	}
	
	public void logWeightCorretion(int inputId, int outputId, double value) {
		finalWeights.put(FeedForwardNetworkTrainingLog.createWeightCorrectionKey(inputId, outputId), value);
	}
}
