package ann.ui.colorgraph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;
import ann.ui.data.InputDataViewPanel;
import ann.ui.data.NormalizeDataSetFrame;
import ann.ui.data.NormalizeFrameListener;
import ann.ui.data.TrainingDataViewPanel;

public class BoxGraphInterfacePanel extends JPanel implements ActionListener, ChangeListener, NormalizeFrameListener {
	private static final long serialVersionUID = 1L;
	protected JButton setValueButton, trainValuesButton, graphOutputData;
	protected JCheckBox graphTrainingData;
	protected JSlider valueSlider;
	protected JTextField colorValueField, maxAField, minAField, maxBField, minBField;
	protected JToolBar valueToolBar,networkToolBar;
	protected BoxGraphUI graphUI;
	protected InputDataViewPanel inputDataPanel;
	protected TrainingDataViewPanel trainingDataPanel;
	protected NormalizeDataSetFrame normalizeFrame;
	protected boolean graphingLive;
	
	protected BoxGraphInterfacePanel() {
		graphUI = new BoxGraphUI(this);
		setupIU();
		
	}
	
	public BoxGraphInterfacePanel(InputDataViewPanel idvPanel, TrainingDataViewPanel tdvPanel ) {
		this();
		normalizeFrame = new NormalizeDataSetFrame(this);
		inputDataPanel = idvPanel;
		trainingDataPanel = tdvPanel;
		graphingLive = false;
	}
	
	private void setupIU() {
		setLayout(new BorderLayout());
		valueToolBar = new JToolBar();
		networkToolBar = new JToolBar();
		setValueButton = setupButton(setValueButton,"Set Value",valueToolBar);
		graphOutputData = setupButton(graphOutputData,"Graph Output Data",networkToolBar);
		trainValuesButton = setupButton(trainValuesButton,"Generate Training Set",networkToolBar);
		graphTrainingData = new JCheckBox("Graph Training Data");
		valueSlider = new JSlider(1,100);
		valueSlider.addChangeListener(this);
		colorValueField = new JTextField();
		networkToolBar.add(graphTrainingData);
		valueToolBar.add(colorValueField);
		valueToolBar.add(valueSlider);
		
		GridLayout gLayout = new GridLayout(2,6);
		gLayout.setHgap(5);
		gLayout.setVgap(5);
		
		add(networkToolBar,BorderLayout.NORTH);
		add(valueToolBar,BorderLayout.SOUTH);
		add(graphUI,BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(506,571));
		//trainValuesButton.setEnabled(false);
	}
	
	private JButton setupButton(JButton button, String label, JToolBar toolbar) {
		button = new JButton(label);
		toolbar.add(button);
		button.addActionListener(this);
		toolbar.add(button);
		return button;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(setValueButton)) {
			try{
				if(new Float(colorValueField.getText())>=0 && new Float(colorValueField.getText())<=1) {
					graphUI.setActiveBoxValue(new Float(colorValueField.getText()));
					System.out.println(this.getSize().height+","+this.getSize().width);
					this.repaint();
				}
			}catch(NumberFormatException exp) {
				//just ignore it
			}
		}else if(e.getSource().equals(trainValuesButton)) {
			trainingDataPanel.updateTrainingData(graphUI.getBoxDataSet());
		}else if(e.getSource().equals(graphOutputData)) {
			if(graphingLive) {
				graphUI.animateGraph(false);
				inputDataPanel.setTableLock(false);
				graphOutputData.setText("Graph Output Data");
				graphingLive = false;
			}else {
				if(JOptionPane.NO_OPTION != JOptionPane.showConfirmDialog(this, "Graph output live?", "Warning", JOptionPane.YES_NO_OPTION)) {
					graphingLive = true;
				}else {
					graphingLive = false;
				}
				normalizeFrame.setVisible(true);
			}
		}
	}
	
	protected NetworkDataSet generateInputSet() {
		Vector<Double> normValues = normalizeFrame.getNormalizeValues();
		double minX = normValues.get(0), maxX = normValues.get(1), minY=normValues.get(2), maxY=normValues.get(3);
		double rangeX = maxX - minX;
		double rangeY = maxY - minY;
		NetworkDataSet dS = new NetworkDataSet();
		Vector<Double> iV = null;
		Vector<Double> oV = null;
		
		for(int i=0;i<BoxGraphUI.GRAPH_SIZE;i++) {
			for(int j=0;j<BoxGraphUI.GRAPH_SIZE;j++) {
				iV = new Vector<Double>();
				oV = new Vector<Double>();
				double iA = ((i/(double)BoxGraphUI.GRAPH_SIZE)*rangeX)+minX;
				double iB = ((j/(double)BoxGraphUI.GRAPH_SIZE)*rangeY)+minY;
				iV.add(iA);
				iV.add(iB);
				oV.add(Double.NaN);
				dS.addNetworkDataPair(new NetworkDataPair(iV,oV));
			}
		}
		return dS;
	}
	
	protected synchronized void updateGraph(NetworkDataSet dS) {
		NetworkDataSet mySet = dS.clone();
		double minX=mySet.getDataPair(0).getInputData().get(0);
		double minY=mySet.getDataPair(0).getInputData().get(1);
		double maxX=mySet.getDataPair(0).getInputData().get(0);
		double maxY=mySet.getDataPair(0).getInputData().get(1);
		double minO=0;
		double maxO=1;
		/*double minX=-1.0;
		double minY=-1.0;
		double maxX=1;
		double maxY=1;
		double minO=0;
		double maxO=1;*/
		
		for(int i=1;i<mySet.size();i++) {
			if(mySet.getDataPair(i).getInputData().get(0)<minX) {
				minX = mySet.getDataPair(i).getInputData().get(0);
			}else if(mySet.getDataPair(i).getInputData().get(0)>maxX) {
				maxX = mySet.getDataPair(i).getInputData().get(0);
			}
			if(mySet.getDataPair(i).getInputData().get(1)<minY) {
				minY = mySet.getDataPair(i).getInputData().get(1);
			}else if(mySet.getDataPair(i).getInputData().get(1)>maxY) {
				maxY = mySet.getDataPair(i).getInputData().get(1);
			}
			if(mySet.getDataPair(i).getOutputData().get(0)<minO) {
				minO = mySet.getDataPair(i).getOutputData().get(0);
			}else if(mySet.getDataPair(i).getOutputData().get(0)>maxO) {
				maxO = mySet.getDataPair(i).getOutputData().get(0);
			}
		}
		for(int i=0;i<mySet.size();i++) {
			int x = (int) ((BoxGraphUI.GRAPH_SIZE)*((mySet.getDataPair(i).getInputData().get(0)-minX)/(maxX-minX)));
			if(x>=BoxGraphUI.GRAPH_SIZE) {
				x = BoxGraphUI.GRAPH_SIZE-1;
			}
			int y = (int)((BoxGraphUI.GRAPH_SIZE)*((mySet.getDataPair(i).getInputData().get(1)-minY)/(maxY-minY)));
			if(y>=BoxGraphUI.GRAPH_SIZE) {
				y = BoxGraphUI.GRAPH_SIZE-1;
			}
			float v = (float) ((mySet.getDataPair(i).getOutputData().get(0)-minO)/(maxO-minO));
			if(v>1 || v<0) {
				System.out.println(v);
				System.out.println(mySet.getDataPair(i).getOutputData().get(0));
				System.out.println(minO+","+maxO);
				System.out.println("---------");
				throw new NullPointerException();
			}
			graphUI.setBoxValue(x,y,v,false);
		}
		if(graphTrainingData.isSelected()) {
			NetworkDataSet tSet = trainingDataPanel.getTrainingData();
			for(int i=0;i<tSet.size();i++) {
				int x = (int) ((BoxGraphUI.GRAPH_SIZE)*((tSet.getDataPair(i).getInputData().get(0)-minX)/(maxX-minX)));
				if(x>=BoxGraphUI.GRAPH_SIZE) {
					x = BoxGraphUI.GRAPH_SIZE-1;
				}
				int y = (int)((BoxGraphUI.GRAPH_SIZE)*((tSet.getDataPair(i).getInputData().get(1)-minY)/(maxY-minY)));
				if(y>=BoxGraphUI.GRAPH_SIZE) {
					y = BoxGraphUI.GRAPH_SIZE-1;
				}
				float v = new Float(tSet.getDataPair(i).getOutputData().get(0));
				if( v<=1 && v>=0 && x>=0 && x<=BoxGraphUI.GRAPH_SIZE && y>=0 && y<=BoxGraphUI.GRAPH_SIZE ) {
					graphUI.setBoxValue(x,y,v,true);
				}else {
					System.out.println(minO+","+maxO);
					System.out.println(x+","+y+","+v);
				}
			}
			
		}
		graphUI.repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(valueSlider)) {
			colorValueField.setText(new Double((double)valueSlider.getValue()/100).toString());
			graphUI.setActiveBoxValue(new Float(colorValueField.getText()));
			this.repaint();
		}
	}

	@Override
	public void doneNormalizing() {
		inputDataPanel.updateInputData(generateInputSet());
		inputDataPanel.processInputData();
		updateGraph(inputDataPanel.getInputData());
		if(graphingLive) {
			inputDataPanel.setTableLock(true);
			graphUI.animateGraph(true); //turn on the live!!
			graphOutputData.setText("Stop Graphing");
		}
	}
	
	public void repaintDone() {
		inputDataPanel.updateInputData(generateInputSet());
		inputDataPanel.processInputData();
		updateGraph(inputDataPanel.getInputData());
		graphUI.repaint();
	}
}
