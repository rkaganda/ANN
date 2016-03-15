/*
 * FeedForwardTrainingLog
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.log;

import java.util.Vector;

public class FeedForwardNetworkTrainingLog {
	protected Vector<FeedFowardNetworkTrainingIteration> ffntI;
	protected Integer currentIndex;
	
	public FeedForwardNetworkTrainingLog() {
		ffntI = new Vector<FeedFowardNetworkTrainingIteration>();
		ffntI.add(new FeedFowardNetworkTrainingIteration());
		currentIndex = 0;
	}
	
	public FeedFowardNetworkTrainingIteration getCurrentIteration() {
		FeedFowardNetworkTrainingIteration ffI = null;
		synchronized(currentIndex) {
			if(ffntI.isEmpty()) {
				ffntI.add(new FeedFowardNetworkTrainingIteration());
			}
			ffI = ffntI.get(currentIndex);
		}
		return ffI;
	}
	
	public void nextIteration() {
		synchronized(currentIndex) {
			if(currentIndex>100) {
				ffntI.clear();
				currentIndex = -1;
			}
			ffntI.add(new FeedFowardNetworkTrainingIteration());
			currentIndex++;
		}
	}
	
	public int getIterationCount() {
		synchronized(currentIndex) {
			return ffntI.size()-1;
		}
	}
	
	public FeedFowardNetworkTrainingIteration getIteration(int i) {
		synchronized(currentIndex) {
			return ffntI.get(i);
		}
	}
	
	public void clearLog() {
		synchronized(currentIndex) {
			ffntI.clear();
			currentIndex = 0;
		}
	}
	
	public static String createInputValueKey(int id) {
		return new StringBuilder("Input").append(new Integer(id).toString()).toString();
	}
	
	public static String createActualOutputKey(int id) {
		return new StringBuilder("ActualOutput").append(new Integer(id).toString()).toString();
	}
	
	public static String createTargetOutputKey(int id) {
		return new StringBuilder("TargetOutput").append(new Integer(id).toString()).toString();
	}
	
	public static String createNodeErrorKey(int id) {
		return new StringBuilder("NodeError").append(new Integer(id)).toString();
	}
	
	public static String createFinalWeightKey(int inputId, int outputId) {
		return new StringBuilder("FinalWeight").append(new Integer(inputId).toString()).append("-").append(outputId).toString();
	}
	
	public static String createInitialWeightKey(int inputId, int outputId) {
		return new StringBuilder("InitialWeight").append(new Integer(inputId).toString()).append("-").append(outputId).toString();
	}
	
	public static String createWeightCorrectionKey(int inputId, int outputId) {
		return new StringBuilder("WeightCorrection").append(new Integer(inputId).toString()).append("-").append(outputId).toString();
	}
}
