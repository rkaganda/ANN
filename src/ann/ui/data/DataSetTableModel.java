/*
 * DataSetTableModel
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.data;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;

public class DataSetTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected NetworkDataSet trainingData;
	protected Vector<Vector<Double>> data;
	protected boolean isTrainingData;
	
	public DataSetTableModel() {
		trainingData = null;
		data = new Vector<Vector<Double>>();
		isTrainingData = true;
	}
	
	protected boolean hasData() {
		boolean hasData = false;
		if(trainingData.size()>0) {
			hasData = true;
		}
		return hasData;
	}
	
	public void updateDataSet(NetworkDataSet tD) {
		synchronized(data) {
			trainingData = tD;
			data = null;
			data = new Vector<Vector<Double>>();
			for(int i=0;i<trainingData.size();i++) {
				data.add(new Vector<Double>());
			for(int j=0;j<trainingData.getDataPair(i).getInputData().size();j++) {
					data.get(i).add(trainingData.getDataPair(i).getInputData().get(j));
				}
				for(int j=0;j<trainingData.getDataPair(i).getOutputData().size();j++) {
					data.get(i).add(trainingData.getDataPair(i).getOutputData().get(j));
				}
			}
		}
		fireTableStructureChanged();
	}
	
	public NetworkDataSet getDataSet() {
		synchronized(data) {
			int inputCount = 0;
			if(trainingData.size()>0) { //count the # of inputs
				inputCount = trainingData.getDataPair(0).getInputData().size(); //count the # of inputs
			}
			trainingData = new NetworkDataSet(); //create new data set
			Vector<Double> tempInput = null; //to hold the input data
			Vector<Double> tempOutput = null; //to hold the output data
			for(int i=0;i<data.size();i++) { //for each row of data
				tempInput = new Vector<Double>(); //create a new input vector
				tempOutput = new Vector<Double>(); //create a new output vector
				for(int j=0;j<inputCount;j++) { //for each data input
					tempInput.add(data.get(i).get(j)); //add the input to the input vector
				}
				for(int j=inputCount;j<data.get(i).size();j++) { //for each data output
					tempOutput.add(data.get(i).get(j)); //add the input to the input vector
				}
				trainingData.addNetworkDataPair(new NetworkDataPair(tempInput,tempOutput));
			}
			return trainingData;
		}
	}
	
	public void addRow() {
		if(data.size()==0) {
			JOptionPane.showMessageDialog(null, "Can't add a row to an empty dataset.", "Failed.", JOptionPane.ERROR_MESSAGE);
		}else {
			data.add(new Vector<Double>());
			for(int i=0;i<data.get(0).size();i++) {
				data.get(data.size()-1).add(new Double(0));
			}
			fireTableRowsInserted(data.size()-1, data.size()-1);
		}
	}
	
	public void removeRows(int[] selectedRows) {
		for(int i=0;i<selectedRows.length;i++) {
		}
		for(int i=selectedRows.length-1;i>-1;i--) {
			if(data.size()==1) {
				JOptionPane.showMessageDialog(null, "Can't remove last row of a dataset.", "Failed.", JOptionPane.ERROR_MESSAGE);
				break;
			}
			data.remove(selectedRows[i]);
		}
		fireTableStructureChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex,int columnIndex) {
		boolean isEditable = true;
		if(trainingData.getDataPair(0).getInputData().size()-1<columnIndex && !isTrainingData) {
			columnIndex = columnIndex - trainingData.getDataPair(0).getInputData().size();
			isEditable = false;
		}
		return isEditable;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		//TODO catch exceptions
		Double f = new Double(0);
		try {
			f = new Double((String)value);
		}catch(NumberFormatException e) {
			
		}
		data.get(row).set(col, f);
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public int getColumnCount() {
		int count = 0;
		if(data.size()!=0) {
			count = data.get(0).size();
		}
		return count;
	}

	@Override
	public int getRowCount() {
		synchronized(data) {
			return data.size();
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		String name = null;
		if(trainingData.getDataPair(0).getInputData().size()-1<columnIndex) {
			columnIndex = columnIndex - trainingData.getDataPair(0).getInputData().size();
			name = new StringBuilder("Output ").append(columnIndex).toString();
		}else {
			name = new StringBuilder("Input ").append(columnIndex).toString();
		}
		return name;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		synchronized(data) {
			return data.get(rowIndex).get(columnIndex);
		}
	}

	public void setIsTrainingData(boolean b) {
		isTrainingData = false;
	}
}
