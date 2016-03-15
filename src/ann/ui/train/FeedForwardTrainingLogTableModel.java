/*
 * FeedForwardTrainingLogTableModel
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.train;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import ann.log.FeedForwardNetworkTrainingLog;
import ann.log.FeedFowardNetworkTrainingIteration;

public class FeedForwardTrainingLogTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected Vector<Vector<Double>> data; //the table data
	protected Vector<String> coloumnName; //the column names
	
	public FeedForwardTrainingLogTableModel() {
		data = new Vector<Vector<Double>>();
		coloumnName = new Vector<String>();
	}
	
	public void refreshLog(FeedForwardNetworkTrainingLog log) { //replaces the current data with the new log
		data.clear(); //remove the old data
		FeedFowardNetworkTrainingIteration tI = null; //place holder
		for(int i=0;i<log.getIterationCount();i++) { //for each log iteration
			tI = log.getIteration(i); //iterate through each value and add it to the log
			data.add(new Vector<Double>()); //create the vector to store the first row
			for(Iterator<String> key=tI.getInputValues().keySet().iterator();key.hasNext();) { //for each inputValue
				data.get(i).add(tI.getInputValues().get(key.next()));
			}
			for(Iterator<String> key=tI.getTargetOuputs().keySet().iterator();key.hasNext();) { //for each targetOutput
				data.get(i).add(tI.getTargetOuputs().get(key.next()));
			}
			for(Iterator<String> key=tI.getActualOutputs().keySet().iterator();key.hasNext();) { //for each actualOutput
				data.get(i).add(tI.getActualOutputs().get(key.next()));
			}
			for(Iterator<String> key=tI.getNodeError().keySet().iterator();key.hasNext();) { //for each nodeError
				data.get(i).add(tI.getNodeError().get(key.next()));
			}
			for(Iterator<String> key=tI.getInitialWeight().keySet().iterator();key.hasNext();) { //for each initialWeight
				data.get(i).add(tI.getInitialWeight().get(key.next()));
			}
			for(Iterator<String> key=tI.getWeightCorretion().keySet().iterator();key.hasNext();) { //for each weightCorrection
				data.get(i).add(tI.getWeightCorretion().get(key.next()));
			}
			for(Iterator<String> key=tI.getFinalWeights().keySet().iterator();key.hasNext();) { //for each finalWeight
				data.get(i).add(tI.getFinalWeights().get(key.next()));
			}
		}
		generateColumnNames(tI);
		fireTableDataChanged(); //notify table has changed
	}
	
	protected void generateColumnNames(FeedFowardNetworkTrainingIteration tI) {
		for(Iterator<String> it=tI.getInputValues().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add input values column
		}
		for(Iterator<String> it=tI.getTargetOuputs().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add target outputs column
		}
		for(Iterator<String> it=tI.getActualOutputs().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add actual outputs column
		}
		for(Iterator<String> it=tI.getNodeError().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add node error column
		}
		for(Iterator<String> it=tI.getInitialWeight().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add initial weight column
		}
		for(Iterator<String> it=tI.getWeightCorretion().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add weight correction column
		}
		for(Iterator<String> it=tI.getFinalWeights().keySet().iterator();it.hasNext();) {
			coloumnName.add(it.next()); //add final weights column
		}
	}
	
	@Override
	public String getColumnName(int i) {
		return coloumnName.get(i);
	}
	
	@Override
	public int getColumnCount() {
		int count = 0;
		if(data.size()>0) {
			count = data.get(0).size();
		}
		return count;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return data.get(arg0).get(arg1);
	}

}
