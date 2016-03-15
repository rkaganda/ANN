/*
 * Link
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

public class Link {
	protected Node inputNode;
	protected Node outputNode;
	protected double weight;
	protected double weightPrev;
	protected int id;
	protected static int nextId = 0;
	
	public Link(Node a, Node b, double w) {
		id = nextId++;
		inputNode = a;
		outputNode = b;
		weight = w;
		weightPrev = w; //for use with momentum
		inputNode.addOutputLink(this);
		outputNode.addInputLink(this);
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double w) {
		setPrevWeight(weight);
		weight = w;
	}
	
	public double getPrevWeight() {
		return weightPrev;
	}
	public void setPrevWeight(double w) {
		weightPrev = w;
	}
	
	
	
	public void nodeFired(double f) {
		outputNode.addValue(f*weight);
	}
	
	protected int getId() {
		return id;
	}
	
	public Node getInputNode() {
		return inputNode;
	}
	
	public Node getOutputNode() {
		return outputNode;
	}
	
	public String toString() {
		return new String("Link"+id);
	}
}
