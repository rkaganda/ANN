/*
 * FeedForwardNetwork
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import ann.data.NetworkDataSet;

public class FeedForwardNetwork {
	protected Vector<FeedForwardLayer> networkLayers;
	protected HashSet<FeedForwardLink> networkLinks;
	protected boolean networkLocked; //can't add new nodes once network is locked
	protected NetworkDataSet trainingData, inputData;
	protected int inputNodeCount;
	
	public FeedForwardNetwork() {
		networkLayers = new Vector<FeedForwardLayer>();
		networkLinks = new HashSet<FeedForwardLink>();
		networkLocked = false;
		trainingData = new NetworkDataSet();
		inputData = new NetworkDataSet();
		inputNodeCount = 0;
	}
	
	public void addLayer() {
		networkLayers.add(new FeedForwardLayer());
	}
	
	public int getLayerCount() {
		return networkLayers.size();
	}
	
	public void addNode(Threshold t, int layerIndex) {
		//TODO check if that layerIndex even exists 
		//if layerIndex>networkLayers.size() do something
		if(!networkLocked) {
			networkLayers.get(layerIndex).addNode(new Node(t));
		}else {
			//TODO throw exception
		}
	}
	
	//TODO check if indices are valid
	public void linkNodes(int layerIndex, int inputNodeIndex, int outputNodeIndex, double w ) {
		networkLinks.add(new FeedForwardLink(networkLayers.get(layerIndex).getNode(inputNodeIndex),
				networkLayers.get(layerIndex+1).getNode(outputNodeIndex),
				w,
				layerIndex,
				inputNodeIndex,
				outputNodeIndex));
	}
	
	//removes references all the links, links every node between layers then locks the network
	public void linkLayers() {
		for(int i=0;i<networkLayers.size();i++) {
			networkLayers.get(i).resetLayer(); //clear link references from each node
		}
		networkLinks.clear(); //clear the link references from the network
		for(int i=0;i<networkLayers.size()-1;i++) { //for each layer (except the output layer)
			for(int j=0;j<networkLayers.get(i).getNodeCount();j++) { //for each node in the layer
				for(int k=0;k<networkLayers.get(i+1).getNodeCount();k++) { //for each node in the layer below
					double r = (new Random().nextDouble()*2)-1;
					linkNodes(i, j, k, r); //link the nodes with random values
				}
			}
		}
		inputNodeCount = networkLayers.firstElement().getNodeCount(); //store the input node count before we add weighted nodes
 		for(int i=1;i<networkLayers.size();i++) {
			for(int j=0;j<networkLayers.get(i).getNodeCount();j++) {
				networkLayers.get(i-1).addNode(new WeightedNode()); //add weighted node to the layer above
				double r = (new Random().nextDouble() % 2)-1;
				linkNodes(i-1, networkLayers.get(i-1).getNodeCount()-1, j, r); //link the nodes with random values
			}
		}
		networkLocked = true; //lock the network
	}
	
	//using this method to modify the layers shouldn't be allowed
	public FeedForwardLayer getLayer(int i) {
		return networkLayers.get(i);
	}
	
	public FeedForwardLayer getOutputLayer() {
		return networkLayers.lastElement();
	}
	
	public FeedForwardLayer getInputLayer() {
		return networkLayers.firstElement();
	}
	
	public HashSet<FeedForwardLink> getLinks() {
		return networkLinks;
	}
	
	public void setNodeValue(int i, double v) {
		networkLayers.get(0).getNode(i).setValue(v);
	}
	
	public void runInputDataSet() {
		NetworkDataSet idS = inputData.clone();
		synchronized(this) {
			for(int i=0;i<idS.size();i++) { //for each input data pair
				for(int j=0;j<idS.getDataPair(i).getInputData().size();j++) { //for each data input
					networkLayers.get(0).getNode(j).setValue(inputData.getDataPair(i).getInputData().get(j));
				}
				activateNetwork();
				for(int j=0;j<idS.getDataPair(i).getOutputData().size();j++) {
					idS.getDataPair(i).getOutputData().set(j, networkLayers.lastElement().getNode(j).getValue());
				}
			}
			inputData = idS;
		}
	}
	
	public void activateNetwork() {
		//TODO check if network is valid... 
		for(int i=1;i<networkLayers.size();i++) { //set all layer but the input layer values to -1
			for(int j=0;j<networkLayers.get(i).getNodeCount();j++) {
				networkLayers.get(i).getNode(j).setActive(false);
				networkLayers.get(i).getNode(j).setValue(0);
			}
		}
		//layers with 0 nodes should not be allowed etc
		for(int i=0;i<networkLayers.size();i++) {
			networkLayers.get(i).activateLayer();
		}
	}
	
	public int getInputNodeCount() {
		return inputNodeCount;
	}
	
	public NetworkDataSet getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(NetworkDataSet templateDataSet) {
		trainingData = templateDataSet;
	}

	public NetworkDataSet getInputData() {
		return inputData;
	}
	
	public void setInputData(NetworkDataSet dS) {
		inputData = dS;
	}
}
