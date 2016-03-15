/*
 * PerceptronTrainingLog
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.log;

import java.util.Vector;


public class PerceptronTrainingLog {
	protected Vector<PerceptronTrainingIteration> ptI;
	protected int currentIndex;
	
	public PerceptronTrainingLog() {
		ptI = new Vector<PerceptronTrainingIteration>();
		ptI.add(new PerceptronTrainingIteration());
		currentIndex = 0;
	}
	
	public PerceptronTrainingIteration getCurrentIteration() {
		return ptI.get(currentIndex);
	}
	
	public void nextIteration() {
		ptI.add(new PerceptronTrainingIteration());
		currentIndex++;
	}
	
	public int getIterationCount() {
		return ptI.size()-1;
	}
	
	public PerceptronTrainingIteration getIteration(int i) {
		return ptI.get(i);
	}
}
