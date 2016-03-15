import ann.FeedForwardNetwork;
import ann.SigmaThreshold;
import ann.ui.graph.FeedForwardNetworkGraph;
import ann.ui.graph.FeedForwardNetworkGraphView;

public class Test {
	public static void main(String args[]) {
		
		/********Feed Forward Network Code**********/
		FeedForwardNetwork ffN = new FeedForwardNetwork();
		ffN.addLayer(); //input layer
		ffN.addNode(new SigmaThreshold(), 0); //input nodes
		ffN.addNode(new SigmaThreshold(), 0); 
		ffN.addLayer(); //hidden layer
		ffN.addNode(new SigmaThreshold(), 1); //hidden nodes
		ffN.addNode(new SigmaThreshold(), 1);
		//ffN.addNode(new SigmaThreshold(), 1);
		//ffN.addNode(new SigmaThreshold(), 1); 
		ffN.addLayer(); //hidden layer
		ffN.addNode(new SigmaThreshold(), 2); //hidden nodes
		ffN.addNode(new SigmaThreshold(), 2);
		//ffN.addNode(new SigmaThreshold(), 2);
		ffN.addLayer(); //output layer
		ffN.addNode(new SigmaThreshold(), 3); //output node
		
		ffN.linkLayers(); //link the layers
		/********Feed Forward Network Code**********/
		
		//JUNG testing
		FeedForwardNetworkGraph ffnGraph = new FeedForwardNetworkGraph(ffN);
		FeedForwardNetworkGraphView gV = new FeedForwardNetworkGraphView(ffnGraph);
		gV.setVisible(true);
	}
}
