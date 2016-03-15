/*
 * FeedForwardNodeIdTableView
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ann.FeedForwardNetwork;

public class FeedForwardNodeIdTableView extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTable nodeTable;
	protected FeedForwardNetwork ffNetwork;
	
	protected FeedForwardNodeIdTableView() {
		
	}
	
	public FeedForwardNodeIdTableView(FeedForwardNetwork ffN ) {
		super(new GridLayout(1,0));
		ffNetwork = ffN;
		initTable();
		nodeTable.setFillsViewportHeight(true);
		nodeTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(nodeTable);
		add(scrollPane);
	}
	
	protected void initTable() {
		Vector<Vector<String>> rowData = new Vector<Vector<String>>(); 
		Vector<String> columnNames = new Vector<String>();
		for(int i=0;i<ffNetwork.getLayerCount();i++) { //for each layer
			rowData.add(new Vector<String>()); //create the vector for this layer
			for(int j=0;j<ffNetwork.getLayer(i).getNodeCount();j++) { //for each node in
				rowData.get(i).add(new Integer(ffNetwork.getLayer(i).getNode(j).getId()).toString()); //add the node id
			}
		}
		
		for(int i=0;i<rowData.get(0).size();i++) { //for each column in the rowData
			columnNames.add(new Integer(i).toString()); //add a number
		}
		
		nodeTable = new JTable(rowData,columnNames);
	}
}
