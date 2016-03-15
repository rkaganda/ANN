/*
 * NullThreshold
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

public class NullThreshold implements Threshold {

	@Override
	public double fire(double v) {
		return 0;
	}

	@Override
	public String thresholdtoString() {
		return new String("null");
	}
	
}
