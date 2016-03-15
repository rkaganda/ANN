/*
 * FeedForwardNetworkTrainerListener
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.train;

public interface FeedForwardNetworkTrainerListener {
	public void iterationFinished(double largestError, int interationCount);
	public void finished(int reason);
}
