/*
 * FeedForwardNetworkTrainer
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.train;

import java.awt.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

import ann.FeedForwardNetwork;
import ann.Link;
import ann.Node;
import ann.data.NetworkDataPair;
import ann.log.FeedForwardNetworkTrainingLog;

public class FeedForwardNetworkTrainer implements Runnable {
	protected FeedForwardNetwork ffNetwork;
	protected FeedForwardNetworkTrainingLog trainingLog;
	protected Vector<Double> learningRates;
	protected double errorThreshold, currentMaxError;
	protected Thread trainingThread;
	protected int maxIteration, currentIteration;
	protected boolean stayAlive;
	protected FeedForwardNetworkTrainerListener listener;
	protected double momentum;
	
	public final static int TRAINER_STOPPED = 0;
	public final static int UNDER_ERROR_THRESHOLD = 1;
	public final static int ITEREATIONS_FINISHED = 2;
	
	protected FeedForwardNetworkTrainer() {
		ffNetwork = null;
		trainingLog = new FeedForwardNetworkTrainingLog();
		learningRates = new Vector<Double>();
		maxIteration = 0;
		currentIteration = 0;
		stayAlive = false;
		currentMaxError = Double.MAX_VALUE; //some really big number
		listener = null;
		momentum = .5;
	}
	
	public FeedForwardNetworkTrainer(FeedForwardNetwork ffN) {
		this();
		ffNetwork = ffN;
	}
	
	public void setLearningRates(double learningRate) {
		learningRates.clear();
		learningRates.add(learningRate);
	}
	
	public void setMomentum(double m) {
		momentum = m;
	}
	
	public double getMomentum() {
		return momentum;
	}
	
	public void setErrorThreshold(double eT) {
		errorThreshold = eT;
	}
	
	public void trainNetwork(int iterations) {
		maxIteration = iterations;
		trainingThread = new Thread(this);
		stayAlive = true;
		trainingThread.start();
	}
	
	public void setListener(FeedForwardNetworkTrainerListener l) {
		listener = l;
	}
	
	protected void runIteration(NetworkDataPair dP) {
		double tempError = 0; //used to store error temporarily
		double inputError = 0; //store the input error for each node
		Node tempNode = null; //temporary reference to a node
		for(int i=0;i<dP.getInputData().size();i++) { //for each input node
			tempNode = ffNetwork.getLayer(0).getNode(i);
			//trainingLog.getCurrentIteration().logInputValue(tempNode.getId(), dP.getInputData().get(i)); //log input value
			tempNode.setValue(dP.getInputData().get(i)); //set each input value
		}
		ffNetwork.activateNetwork(); //activate the network
		//calculate error for output layer
		for(int i=0;i<ffNetwork.getOutputLayer().getNodeCount();i++) { //for each output node
			tempNode = ffNetwork.getOutputLayer().getNode(i); //store node reference
			tempError = dP.getOutputData().get(i) - tempNode.getValue();//calculate error
			if(Math.abs(tempError)>currentMaxError) { //check error
				currentMaxError = Math.abs(tempError); //update max error
			}
			//trainingLog.getCurrentIteration().logActualOutput(tempNode.getId(), tempNode.getValue()); //log actual output
			//trainingLog.getCurrentIteration().logTargetOutput(tempNode.getId(), dP.getOutputData().get(i));//log target output
			//trainingLog.getCurrentIteration().logNodeError(tempNode.getId(), tempError);//log error
			tempNode.setError(tempError); //store the error for back propagation
			tempNode.setBias(tempNode.getBias()+(tempError*learningRates.firstElement())); //adjust bias
			
			trainLinkWeights(tempNode,tempError,ffNetwork.getLayerCount()-1,i); //train link weight
			
		}
		//propagate error up network
		for(int i=ffNetwork.getLayerCount()-2;i>0;i--) { //for each hidden layer
			for(int j=0;j<ffNetwork.getLayer(i).getNodeCount();j++) { //for each node
				tempNode = ffNetwork.getLayer(i).getNode(j); //store the node
				if(!tempNode.isWeighted()) {
					inputError = FeedForwardNetworkTrainingMath.inputNodeError(tempNode);//calculate error
					//trainingLog.getCurrentIteration().logNodeError(tempNode.getId(), inputError);//log error
					tempNode.getValue();//log actual output
					tempNode.setError(inputError);//store the error for back propagation
				}
			}
		}
		//travel back down network to fix layers
		for(int i=1;i<ffNetwork.getLayerCount();i++) { //for each layer
			for(int j=0;j<ffNetwork.getLayer(i).getNodeCount();j++) { //for each node
				tempNode = ffNetwork.getLayer(i).getNode(j); //store the node
				trainLinkWeights(tempNode,tempNode.getError(),i,j);//train link weight
			}
		}
		//trainingLog.nextIteration();
	}
	
	protected void trainLinkWeights(Node n, double error, int layerIndex, int nodeIndex) {
		Link tempLink = null;
		double tempCorrection = 0;
		double momentumTerm = 0;
		if(!n.isWeighted()) {
			for(Iterator<Link> it=n.getInputLinks().iterator();it.hasNext();) { //for each input link
				tempLink = it.next();
				//trainingLog.getCurrentIteration().logInitialWeight(tempLink.getInputNode().getId(),tempLink.getOutputNode().getId(), tempLink.getWeight());//log initial weight
				if(Math.abs(error)>errorThreshold) { //check if we need to modify weight
					tempCorrection = FeedForwardNetworkTrainingMath.weightCorrection(tempLink, error, learningRates.firstElement());//calculate correction
					momentumTerm = getMomentum()*(tempLink.getWeight() - tempLink.getPrevWeight());
					
				}
				
				
				//trainingLog.getCurrentIteration().logWeightCorretion(tempLink.getInputNode().getId(),tempLink.getOutputNode().getId(), tempCorrection); //log correction
				tempLink.setWeight(tempLink.getWeight()-tempCorrection - momentumTerm);//correction weight
				//trainingLog.getCurrentIteration().logFinalWeight(tempLink.getInputNode().getId(),tempLink.getOutputNode().getId(), tempLink.getWeight());//log final weight
			}
			n.setBias(error*n.getValue()*learningRates.firstElement());
		}
	}
	
	public FeedForwardNetworkTrainingLog getTrainingLog() {
		return trainingLog;
	}
	
	public void run() {
		currentMaxError = Double.MAX_VALUE;
		while(stayAlive && currentIteration<maxIteration && Math.abs(currentMaxError)>errorThreshold) {
			currentMaxError = 0; //set this 0 so the 1st error value is saved
			
			Collections.shuffle(ffNetwork.getTrainingData().getDataSet());
			
			for(int i=0;i<ffNetwork.getTrainingData().size();i++) { //for each data pair
				synchronized(ffNetwork) { 
					runIteration(ffNetwork.getTrainingData().getDataPair(i));
				}
			}
			if(listener!=null) {
				listener.iterationFinished(currentMaxError, currentIteration);
			}
			currentIteration++; //increase iteration
		}
		if(listener!=null) {
			if(!stayAlive) {
				listener.finished(FeedForwardNetworkTrainer.TRAINER_STOPPED);
			}else if(currentIteration>=maxIteration) {
				listener.finished(FeedForwardNetworkTrainer.ITEREATIONS_FINISHED);
			}else if(Math.abs(currentMaxError)<errorThreshold) {
				listener.finished(FeedForwardNetworkTrainer.UNDER_ERROR_THRESHOLD);
			}
			listener.iterationFinished(currentMaxError, currentIteration);
		}
		currentIteration = 0;
	}

	public void setActive(boolean b) {
		stayAlive = false;
	}
}
