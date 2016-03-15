/*
 * Node
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

import java.util.HashSet;
import java.util.Iterator;

public class Node {
	protected Threshold threshold;
	protected double value;
	protected HashSet<Link> outputLinks; //outgoing links
	protected HashSet<Link> inputLinks; //input links
	protected double nodeError;
	protected boolean isActive;
	protected int id;
	protected static int nextId = 0;
	protected double bias = 0;
	
	public Node() {
		id = nextId++;
		threshold = null;
		value = -1;
		isActive = false;
		outputLinks = new HashSet<Link>();
		inputLinks = new HashSet<Link>();
		nodeError = 0;
		bias = 0;
	}
	
	public Node(Threshold t) {
		this();
		threshold = t;
	}
	
	public boolean isWeighted() {
		return false;
	}
	
	public void setActive(boolean b) {
		isActive = b;
		if(isActive) {
			setValue(threshold.fire(value+bias));
			for(Iterator<Link> it = outputLinks.iterator();it.hasNext();) {
				it.next().nodeFired(value);
			}
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void addValue(double v) {
		setValue(value+v);
	}
	
	public void setValue(double v) {
		value = v;
	}
	
	public double getValue() {
		return value;
	}
	
	public void addOutputLink(Link l) {
		outputLinks.add(l);
	}
	
	public void addInputLink(Link l) {
		inputLinks.add(l);
	}
	
	public HashSet<Link> getOutputLinks() {
		return outputLinks;
	}
	
	public HashSet<Link> getInputLinks() {
		return inputLinks;
	}
	
	public void clearLinks() {
		outputLinks.clear();
		inputLinks.clear();
	}
	
	public Threshold getThreshold() {
		return threshold;
	}
	
	public void setError(double e) {
		nodeError = e;
	}
	
	public double getError() {
		return nodeError;
	}
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double b) {
		bias = b;
	}
	
	public int getId() {
		return id;
	}
	
	public String thresholdtoString() {
		return threshold.thresholdtoString();
	}
	
	public String toString() {
		return new String("Node"+id);
	}
}
