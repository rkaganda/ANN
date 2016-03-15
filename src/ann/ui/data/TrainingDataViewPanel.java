/*
 * FeedForwardTrainingDataViewPanel
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.data;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import ann.FeedForwardNetwork;
import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;
import ann.xml.FeedForwardNetworkXML;

public class TrainingDataViewPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JToolBar toolBar;
	protected JButton loadButton, addButton,applyButton,removeButton,exportButton;
	protected JTable trainingDataTable;
	protected DataSetTableModel tableModel;
	protected FeedForwardNetwork ffNetwork;
	
	protected TrainingDataViewPanel() {
		super(new BorderLayout());
		setupToolBar();
	}
	
	public TrainingDataViewPanel(FeedForwardNetwork ffN) {
		this();
		ffNetwork = ffN;
		setupTable();
		JScrollPane scrollPane = new JScrollPane(trainingDataTable);
		setPreferredSize(new Dimension(450,450));
		add(toolBar, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	protected void setupToolBar() {
		toolBar = new JToolBar("toolBar");
		loadButton = setupButton(loadButton, "Import");
		exportButton = setupButton(exportButton,"Export");
		addButton = setupButton(addButton, "Add Row");
		removeButton = setupButton(removeButton, "Remove Row");
		applyButton = setupButton(applyButton, "Apply Changes");
	}
	
	protected JButton setupButton(JButton button, String name) {
		button = new JButton(name);
		button.addActionListener(this);
		toolBar.add(button);
		return button;
	}
	
	protected void setupTable() {
		tableModel = new DataSetTableModel(); //create the model
		if(ffNetwork.getTrainingData().size()==0) { //check if the data set is empty
			generateTemplateDataSet(); //generate a template data set
		}
		updateTrainingData(ffNetwork.getTrainingData()); //get the network data set
		trainingDataTable = new JTable(tableModel); //create the table
	}
	
	protected void generateTemplateDataSet() {
		NetworkDataSet templateDataSet = ffNetwork.getTrainingData(); //get the empty data set
		Vector<Double> inputSet = new Vector<Double>();
		Vector<Double> outputSet = new Vector<Double>();
		for(int i=0;i<ffNetwork.getInputNodeCount();i++) { //for each input node
			inputSet.add(new Double(0)); //create an inputValue
		}
		for(int i=0;i<ffNetwork.getLayer(ffNetwork.getLayerCount()-1).getNodeCount();i++) { //for each output node
			outputSet.add(new Double(0)); //create an outputValue
		}
		templateDataSet.addNetworkDataPair(new NetworkDataPair(inputSet,outputSet)); //add the template to the set
		updateTrainingData(templateDataSet); //update the network data set
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(loadButton)) {
			loadTrainingData();
		}else if(e.getSource().equals(applyButton)) {
			updateTrainingData();
		}else if(e.getSource().equals(addButton)) {
			tableModel.addRow();
		}else if(e.getSource().equals(removeButton)) {
			tableModel.removeRows(trainingDataTable.getSelectedRows());
		}else if(e.getSource().equals(exportButton)) {
			exportTrainingData();
		}
	}
	
	public NetworkDataSet getTrainingData() {
		return ffNetwork.getTrainingData();
	}
	
	protected void updateTrainingData() {
		NetworkDataSet dS = tableModel.getDataSet();
		if(dS.size()==0) { //check if there is any data
			JOptionPane.showMessageDialog(this, "No values to update.", "Update Failed.", JOptionPane.ERROR_MESSAGE);
		}else {
			//check to see if value counts match node counts
			if((ffNetwork.getInputNodeCount()==dS.getDataPair(0).getInputData().size()) && 
					ffNetwork.getLayer(ffNetwork.getLayerCount()-1).getNodeCount()==dS.getDataPair(0).getOutputData().size()) {
				updateTrainingData(dS);
				JOptionPane.showMessageDialog(this, "Update OK", "Success", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "Input/Out value count does not match Input/Output node count.", "Failed.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void updateTrainingData(NetworkDataSet dS) {
		ffNetwork.setTrainingData(dS);
		tableModel.updateDataSet(ffNetwork.getTrainingData()); //get the network data set
	}
	
	protected void exportTrainingData() {
		NetworkDataSet dS = tableModel.getDataSet();
		if(dS.size()==0) { //check if there is any data
			JOptionPane.showMessageDialog(this, "No data to export!", "Failed.", JOptionPane.ERROR_MESSAGE);
		}else {
			JFileChooser fc = new JFileChooser();
			fc.setApproveButtonText("Save");
			if( fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
				FeedForwardNetworkXML.exportDataSet(dS, fc.getSelectedFile().getAbsolutePath());
			}
		}
	}
	
	protected void loadTrainingData() {
		JFileChooser fc = new JFileChooser();
		if( fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			updateTrainingData(FeedForwardNetworkXML.loadDataSet(fc.getSelectedFile().getAbsolutePath()));
		}
	}
	
	public void addToTrainingSet(NetworkDataPair dP) {
		ffNetwork.getTrainingData().addNetworkDataPair(dP);
		updateTrainingData(ffNetwork.getTrainingData()); //get the network data set
	}

}
