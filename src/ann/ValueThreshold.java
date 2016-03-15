/*
 * ValueThreshold
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

public class ValueThreshold implements Threshold {
	protected double thresholdValue;
	
	public ValueThreshold(double f) {
		thresholdValue = f;
	}
	
	@Override
	public double fire(double v) {
		if(thresholdValue>=v) {
			return 1;
		}
		return 0;
	}

	@Override
	public String thresholdtoString() {
		return new Double(thresholdValue).toString();
	}
}
