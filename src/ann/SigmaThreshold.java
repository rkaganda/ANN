/*
 * SigmaThreshold
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

import ann.train.FeedForwardNetworkTrainingMath;

public class SigmaThreshold implements Threshold {

	@Override
	public double fire(double v) {
		return FeedForwardNetworkTrainingMath.setSimga(v);
	}

	@Override
	public String thresholdtoString() {
		return new String("Sigma");
	}
}
