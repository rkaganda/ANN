/*
 * InputDataViewPanel
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

public class InputDataViewPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JToolBar toolBar;
	protected JButton loadButton, addButton,processButton,removeButton,exportButton;
	protected JTable inputDataTable;
	protected DataSetTableModel tableModel;
	protected FeedForwardNetwork ffNetwork;
	protected boolean lockTable; 
	
	protected InputDataViewPanel() {
		super(new BorderLayout());
		setupToolBar();
		add(toolBar,BorderLayout.NORTH);
		ffNetwork= null;
	}
	
	public InputDataViewPanel(FeedForwardNetwork ffN) {
		this();
		ffNetwork = ffN;
		setupTable();
		JScrollPane scrollPane = new JScrollPane(inputDataTable);
		setPreferredSize(new Dimension(450,450));
		add(toolBar, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		lockTable = false;
	}
	
	private void setupToolBar() {
		toolBar = new JToolBar();
		loadButton = setupButton(loadButton,"Import");
		exportButton = setupButton(exportButton,"Export");
		addButton = setupButton(addButton,"Add Row");
		removeButton = setupButton(removeButton,"Remove Row");
		processButton = setupButton(processButton,"Process Data");
	}
	
	private JButton setupButton(JButton button, String name) {
		button = new JButton(name);
		button.addActionListener(this);
		toolBar.add(button);
		return button;
	}
	
	private void setupTable() {
		tableModel = new DataSetTableModel(); //create the model
		tableModel.setIsTrainingData(false);
		if(ffNetwork.getInputData().size()==0) { //check if the data set is empty
			generateTemplateDataSet(); //generate a template data set
		}
		updateInputData(ffNetwork.getInputData()); //get the network data set
		inputDataTable = new JTable(tableModel); //create the table
	}
	
	public void updateInputData(NetworkDataSet dS) {
		ffNetwork.setInputData(dS);
		if(!lockTable) { //check if the table is locked
			tableModel.updateDataSet(ffNetwork.getInputData()); //get the network data set
		}
	}
	
	protected void generateTemplateDataSet() {
		NetworkDataSet templateDataSet = ffNetwork.getInputData(); //get the empty data set
		Vector<Double> inputSet = new Vector<Double>();
		Vector<Double> outputSet = new Vector<Double>();
		for(int i=0;i<ffNetwork.getInputNodeCount();i++) { //for each input node
			inputSet.add(new Double(0)); //create an inputValue
		}
		for(int i=0;i<ffNetwork.getLayer(ffNetwork.getLayerCount()-1).getNodeCount();i++) { //for each output node
			outputSet.add(Double.NaN); //create an outputValue
		}
		templateDataSet.addNetworkDataPair(new NetworkDataPair(inputSet,outputSet)); //add the template to the set
		updateInputData(templateDataSet); //update the network data set
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addButton)) {
			tableModel.addRow();
		}else if(e.getSource().equals(removeButton)) {
			tableModel.removeRows(inputDataTable.getSelectedRows());
		}else if(e.getSource().equals(exportButton)) {
			exportInputData();
		}else if(e.getSource().equals(loadButton)) {
			loadInputData(); 
		}else if(e.getSource().equals(processButton)) {
			processInputData(); 
		}
	}
	
	public NetworkDataSet getInputData() {
		return ffNetwork.getInputData();
	}
	
	public void processInputData() {
		ffNetwork.runInputDataSet();
		if(!lockTable) {
			tableModel.updateDataSet(ffNetwork.getInputData());
		}
	}
	
	public void setTableLock(boolean b) {
		lockTable = b;
	}
	
	protected void exportInputData() {
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
	
	protected void loadInputData() {
		JFileChooser fc = new JFileChooser();
		if( fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			NetworkDataSet loadedSet = FeedForwardNetworkXML.loadDataSet(fc.getSelectedFile().getAbsolutePath());
			for(int i=0;i<loadedSet.size();i++) {
				for(int j=0;j<loadedSet.getDataPair(i).getOutputData().size();j++) {
					loadedSet.getDataPair(i).getOutputData().set(j, Double.NaN);
				}
				
			}
			updateInputData(loadedSet);
		}
	}
}
