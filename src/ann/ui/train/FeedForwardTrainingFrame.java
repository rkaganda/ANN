/*
 * FeedForwardTrainingFrame
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.train;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import ann.FeedForwardNetwork;
import ann.train.FeedForwardNetworkTrainer;
import ann.data.NetworkDataSet;

public class FeedForwardTrainingFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected Vector<FeedForwardNetworkTrainer> networkTrainers;
	protected Vector<FeedForwardTrainingOptionPanel> trainingOptionPanels;
	protected FeedForwardNetwork ffNetwork;
	protected NetworkDataSet dataSet;
	protected JFrame trainingDataFrame;
	protected JPanel framePanel,trainingPanel;
	protected JButton addTrainerButton,removeTrainerButton;
	protected JToolBar buttonToolBar;
	
	protected FeedForwardTrainingFrame() {
		super("Training");
		networkTrainers = new Vector<FeedForwardNetworkTrainer>();
		trainingOptionPanels = new Vector<FeedForwardTrainingOptionPanel>();
		dataSet = new NetworkDataSet();
		initPanels();
		initFrames();
	}
	
	public FeedForwardTrainingFrame(FeedForwardNetwork ffN) {
		this();
		ffNetwork = ffN;
		//create first trainer
		createNewTrainer();
	}
	
	private void initFrames() {
		trainingDataFrame = new JFrame("Training Data");
	}
	
	private void initPanels() {
		framePanel = new JPanel();
		buttonToolBar = new JToolBar();
		trainingPanel = new JPanel();
		framePanel.setLayout(new BorderLayout());
		trainingPanel.setLayout(new BoxLayout(trainingPanel,BoxLayout.X_AXIS));
		
		//TODO add trainer functionality
		//addTrainerButton = setupButton("Add Trainer",addTrainerButton);
		//removeTrainerButton = setupButton("Remove Trainer",removeTrainerButton);
		framePanel.add(trainingPanel,BorderLayout.CENTER);
		framePanel.add(buttonToolBar,BorderLayout.NORTH);
		
		framePanel.setOpaque(true); 
	    setContentPane(framePanel);
	}
	
	protected JButton setupButton(String name, JButton button) {
		button = new JButton(name);
        button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        buttonToolBar.add(button);
        button.addActionListener(this);
        return button;
	}
	
	private void createNewTrainer() {
		networkTrainers.add(new FeedForwardNetworkTrainer(ffNetwork)); //create a new trainer
		trainingPanel.add(new FeedForwardTrainingOptionPanel(networkTrainers.lastElement())); //create the panel for the trainer
		pack();
	}
	
	public NetworkDataSet getTrainingData() {
		return dataSet;
	}
	
	public FeedForwardNetwork getNetwork() {
		return ffNetwork;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
