/*
 * FeedForwardLayer
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

import java.util.Vector;

public class FeedForwardLayer {
	public Vector<Node> layerNodes;
	public int layerSize;
	
	public FeedForwardLayer() {
		layerNodes = new Vector<Node>();
	}
	
	public void activateLayer() {
		for(int i=0;i<layerNodes.size();i++) {
			layerNodes.get(i).setActive(true);
		}
	}
	
	//clears all the input links for each node in the layer
	public void resetLayer() {
		for(int i=0;i<layerNodes.size();i++) {
			layerNodes.get(i).clearLinks();
		}
	}
	
	public int getNodeCount() {
		return layerNodes.size();
	}
	
	public void addNode(Node n) {
		layerNodes.add(n);
	}
	
	public Node getNode(int i) {
		return layerNodes.get(i);
	}
}
