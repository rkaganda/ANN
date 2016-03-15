/*
 * FeedForwardTrainingLogViewPanel
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.train;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ann.log.FeedForwardNetworkTrainingLog;

public class FeedForwardTrainingLogViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTable logTable; //the table
	protected FeedForwardTrainingLogTableModel tableModel; //the table model
	protected FeedForwardNetworkTrainingLog theLog;
	
	protected FeedForwardTrainingLogViewPanel() {
		super(new BorderLayout());
		theLog = null;
	}
	
	public FeedForwardTrainingLogViewPanel(FeedForwardNetworkTrainingLog log) {
		this();
		theLog = log;
		setupTable();
	}
	
	protected void setupTable() {
		tableModel = new FeedForwardTrainingLogTableModel();
		tableModel.refreshLog(theLog);
		logTable = new JTable(tableModel);
		logTable.setPreferredScrollableViewportSize(new Dimension(900,250));
		logTable.setFillsViewportHeight(true);
		logTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(logTable);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);
	}
	
	public void refreshTrainingLog(FeedForwardNetworkTrainingLog log) {
		tableModel.refreshLog(log);
	}
}
