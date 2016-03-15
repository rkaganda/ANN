/*
 * FeedForwardLink
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

public class FeedForwardLink extends Link {
	protected int layerIndex, inputIndex, outputIndex;
	
	public FeedForwardLink(Node a, Node b, double w, int lI, int iI, int oI) {
		super(a, b, w);
		layerIndex = lI;
		inputIndex = iI;
		outputIndex = oI;
	}
	
	public int getLayerIndex() {
		return layerIndex;
	}
	
	public int getInputIndex() {
		return inputIndex;
	}
	
	public int getOutputIndex() {
		return outputIndex;
	}
	
	public String toString() {
		return new String("FeedForwardLink"+id);
	}
}
