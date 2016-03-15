/*
 * FeedForwardNetworkTrainingMath
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.train;

import java.util.Iterator;

import ann.Link;
import ann.Node;

public class FeedForwardNetworkTrainingMath {
	public static double weightCorrection(Link link, double error, double learningRate) {
		return link.getInputNode().getValue()*error*learningRate*FeedForwardNetworkTrainingMath.getDerivative(link.getOutputNode().getValue());
	}
	
	public static double getDerivative(double n) {
		return n*(n-1);
	}
	
	public static double setSimga(double v) {
		return (1/(1+Math.exp(-v)));
	}
	
	//calculate the error over all the input nodes
	public static double inputNodeError(Node n) {
		double e = 0;
		Link l = null; //place holder reference
		for(Iterator<Link> it=n.getOutputLinks().iterator();it.hasNext();) {
			l = it.next();
			e = e + (l.getWeight()*l.getOutputNode().getError());
		}
		return e;
	}
}
