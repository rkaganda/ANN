/*
 * FeedForwardTrainingOptionPanel
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.train;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import ann.train.FeedForwardNetworkTrainer;
import ann.train.FeedForwardNetworkTrainerListener;

public class FeedForwardTrainingOptionPanel extends JPanel implements ActionListener, FeedForwardNetworkTrainerListener {
	private static final long serialVersionUID = 1L;
	protected JButton startButton, stopButton, viewLogButton, clearLogButton;
	protected JTextField maxIterationField, currentIterationField, errorThresholdField, largestErrorField,learningRateField, momentumField;
	protected JPanel buttonPanel, fieldPanel;
	protected BoxLayout theLayout;
	protected GridLayout fieldLayout;
	protected FeedForwardNetworkTrainer networkTrainer;
	protected boolean isSelected;
	protected JFrame trainingLogFrame;
	
	protected FeedForwardTrainingOptionPanel() {
		setupUI();
		setupFrames();
	}
	
	public FeedForwardTrainingOptionPanel(FeedForwardNetworkTrainer nT) {
		this();
		isSelected = false;
		networkTrainer = nT;
		networkTrainer.setListener(this);
	}
	
	private void setupFrames() {
		trainingLogFrame = new JFrame();
	}
	
	private void setupUI() {
		buttonPanel = new JPanel();
		fieldPanel = new JPanel();
		fieldLayout = new GridLayout(2,6);
		fieldLayout.setHgap(5);
		fieldLayout.setVgap(5);
		
		theLayout = new BoxLayout(this,BoxLayout.Y_AXIS); //initialize layouts
		setLayout(theLayout); //set layouts
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		fieldPanel.setLayout(fieldLayout);
		
		startButton = setupButton("Start",startButton);
		stopButton = setupButton("Stop",stopButton);
		viewLogButton = setupButton("View Log",viewLogButton);
		clearLogButton = setupButton("Clear Log",viewLogButton);
		maxIterationField = setupTextField(new String("Iterations"), fieldPanel, maxIterationField);
		errorThresholdField = setupTextField(new String("Error threshold"), fieldPanel, errorThresholdField);
		learningRateField = setupTextField(new String("Learning Rate"), fieldPanel, errorThresholdField);
		currentIterationField = setupTextField(new String("Current iteration"), fieldPanel, currentIterationField);
		largestErrorField = setupTextField(new String("Largest error"), fieldPanel, largestErrorField);
		momentumField = setupTextField(new String("Momentum"), fieldPanel, momentumField);
		
		//fieldPanel.add(new JPanel()); //spacing
		//fieldPanel.add(new JPanel());//spacing
		add(Box.createRigidArea(new Dimension(0,5))); //create spacing
		add(fieldPanel); //add field panel
		add(Box.createRigidArea(new Dimension(0,5))); //create spacing
		add(buttonPanel);
		add(Box.createRigidArea(new Dimension(0,5))); //create spacing
		stopButton.setEnabled(false);
		currentIterationField.setEditable(false); //field is for display only
		largestErrorField.setEditable(false); //field is for display only
		maxIterationField.setText("0"); //set default value
		errorThresholdField.setText("0.1"); //set default value
		learningRateField.setText("0.1"); //set default value
		momentumField.setText("0.5");
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}
	
	protected JTextField setupTextField(String label, JPanel panel, JTextField tField) {
		tField = new JTextField();
		panel.add(new JLabel(label));
        panel.add(tField);
        return tField;
	}
	
	protected JButton setupButton(String name, JButton button) {
		button = new JButton(name);
        button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        buttonPanel.add(button);
        button.addActionListener(this);
        return button;
	}
	
	protected boolean userInputsValid() {
		boolean inputsValid = false;
		try {
			if(new Integer(maxIterationField.getText())<0) { //check maxIterations
				throw new NumberFormatException();
			}
			try {
				new Double(errorThresholdField.getText()); //check error threshold
				try {
					if(new Double(learningRateField.getText())<0) { //check learning rate
						throw new NumberFormatException();
					}
					inputsValid = true; //all the inputs are ok
				}catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Learning rate is invalid.", "Failed", JOptionPane.ERROR_MESSAGE);
				}
			}catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Error threshold is invalid.", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Iterations is not a postive integer.", "Failed", JOptionPane.ERROR_MESSAGE);
		}
		return inputsValid;
	}
	
	public void showTrainingLog() {
		FeedForwardTrainingLogViewPanel newContentPane = new FeedForwardTrainingLogViewPanel(networkTrainer.getTrainingLog());
        newContentPane.setOpaque(true); 
        trainingLogFrame.setContentPane(newContentPane);
        
        trainingLogFrame.pack();
        trainingLogFrame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(startButton)) {
			if(userInputsValid()) {
				if(new Integer(maxIterationField.getText())==0) {
					if(JOptionPane.NO_OPTION != JOptionPane.showConfirmDialog(this, "Run training until error threshold is reached?", "Warning", JOptionPane.YES_NO_OPTION)) {
						maxIterationField.setText(new Integer(Integer.MAX_VALUE).toString());
					}
				}
				errorThresholdField.setEditable(false); //lock field while training
				learningRateField.setEditable(false); //lock field while training
				maxIterationField.setEditable(false); //lock field while training
				startButton.setEnabled(false); //
				networkTrainer.setActive(true);
				networkTrainer.setErrorThreshold(new Double(errorThresholdField.getText()));
				networkTrainer.setLearningRates(new Double(learningRateField.getText()));
				networkTrainer.setMomentum(new Double(momentumField.getText()));
				networkTrainer.trainNetwork(new Integer(maxIterationField.getText()));
				stopButton.setEnabled(true); //
			}
		}else if(e.getSource().equals((viewLogButton))) {
			showTrainingLog();
		}else if(e.getSource().equals(stopButton)) {
			networkTrainer.setActive(false);
		}else if(e.getSource().equals(clearLogButton)) {
			networkTrainer.getTrainingLog().clearLog();
		}
	}

	@Override
	public void iterationFinished(double largestError, int interationCount) {
		currentIterationField.setText(new Integer(interationCount).toString());
		largestErrorField.setText(new Double(largestError).toString());
	}

	@Override
	public void finished(int reason) {
		if(reason==FeedForwardNetworkTrainer.UNDER_ERROR_THRESHOLD) {
			JOptionPane.showMessageDialog(this, "Convergence found under error threshold.", "Succses!", JOptionPane.INFORMATION_MESSAGE);
		}else if(reason==FeedForwardNetworkTrainer.ITEREATIONS_FINISHED) {
			JOptionPane.showMessageDialog(this, "Max iterations reached.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		}
		errorThresholdField.setEditable(true); //unlocked field when done training
		learningRateField.setEditable(true); //unlocked field when done training
		maxIterationField.setEditable(true); //unlocked field when done training
		startButton.setEnabled(true); //
		stopButton.setEnabled(false); //
		networkTrainer.setActive(false);
	}
}
