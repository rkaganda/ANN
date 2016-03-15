package ann.ui.graph;

import java.util.Iterator;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import ann.FeedForwardLink;
import ann.FeedForwardNetwork;
import ann.Node;

public class FeedForwardNetworkGraph {
	protected FeedForwardNetwork theffN;
	protected DirectedSparseMultigraph<Node,FeedForwardLink> theGraph;
	
	protected FeedForwardNetworkGraph() {
		theGraph = new DirectedSparseMultigraph<Node,FeedForwardLink>();
	}
	
	public FeedForwardNetworkGraph(FeedForwardNetwork ffN) {
		this();
		theffN = ffN;
		updateGraph();
	}
	
	private void updateGraph() {
		for(int i=0;i<theffN.getLayerCount();i++) {
			for(int j=0;j>theffN.getLayer(i).getNodeCount();j++) {
				theGraph.addVertex(theffN.getLayer(i).getNode(j));
			}
		}
		
		FeedForwardLink ffL = null;
		for(Iterator<FeedForwardLink> it=theffN.getLinks().iterator();it.hasNext();) {
			ffL = it.next();
			theGraph.addEdge(ffL, 
					theffN.getLayer(ffL.getLayerIndex()).getNode(ffL.getInputIndex()), 
					theffN.getLayer(ffL.getLayerIndex()+1).getNode(ffL.getOutputIndex()), 
					EdgeType.DIRECTED);
		}
	}
	
	public DirectedSparseMultigraph<Node,FeedForwardLink> getGraph() {
		return theGraph;
	}
	
	public FeedForwardNetwork getNetwork() {
		return theffN;
	}
}
