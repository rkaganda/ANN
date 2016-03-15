/*
 * FeedForwardNetworkGraphView
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.ui.graph;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ann.FeedForwardLink;
import ann.Node;
import ann.train.data.RandomDataGeneratorPanel;
import ann.ui.FeedForwardNodeIdTableView;
import ann.ui.colorgraph.BoxGraphInterfacePanel;
import ann.ui.data.InputDataViewPanel;
import ann.ui.data.TrainingDataViewPanel;
import ann.ui.train.FeedForwardTrainingFrame;
import ann.xml.FeedForwardNetworkXML;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class FeedForwardNetworkGraphView extends JFrame implements ActionListener {
	protected FeedForwardNetworkGraph theGraph;
	protected Layout<Node,FeedForwardLink> theLayout;
	protected JMenuBar menuBar;
	protected JMenu fileMenu, networkMenu, viewMenu, modulesMenu;
	protected JMenuItem loadNetwork, saveNetwork,trainNetwork,viewTrainingData,viewNodeIds,viewInputData,viewBoxGraph,viewRandomDataGen,resetNetwork;
	protected JFrame trainingDataFrame, trainingLogFrame, nodeIdViewFrame, inputDataFrame,graphFrame,randomDataFrame;
	protected FeedForwardTrainingFrame trainingFrame;
	protected TrainingDataViewPanel traininDataViewPanel;
	protected InputDataViewPanel inputDataViewPanel;
	
	private static final long serialVersionUID = 1L;
	
	protected FeedForwardNetworkGraphView() {
		super("FeedForwardNetworkGraphView");
	}
	
	public FeedForwardNetworkGraphView(FeedForwardNetworkGraph nG) {
		this();
		theGraph = nG;
		initFrames();
		createMenuBar();
		updateGraph();
	}
	
	protected void initFrames() {
		trainingFrame = new FeedForwardTrainingFrame(theGraph.getNetwork());
		trainingDataFrame = new JFrame("Training Data");
		trainingLogFrame = new JFrame("Training Log");
		nodeIdViewFrame = new JFrame("Node Ids");
		inputDataFrame = new JFrame("Input Data");
		graphFrame = new JFrame("Graph");
		randomDataFrame = new JFrame("Random Data Generator");
		
		traininDataViewPanel = new TrainingDataViewPanel(theGraph.getNetwork());
		inputDataViewPanel = new InputDataViewPanel(theGraph.getNetwork());
	}
	
	
	protected void createMenuBar() {
		//TODO add alt key support stuff
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		networkMenu = new JMenu("Network");
		viewMenu = new JMenu("View");
		modulesMenu = new JMenu("Modules");
		loadNetwork = new JMenuItem("Load Network File..");
		saveNetwork = new JMenuItem("Save Network File..");
		trainNetwork = new JMenuItem("Train Network...");
		resetNetwork = new JMenuItem("Reset Network.");
		viewNodeIds = new JMenuItem("View Node Ids");
		viewInputData = new JMenuItem("View Input/Process Data");
		viewTrainingData = new JMenuItem("View Training Data");
		viewBoxGraph = new JMenuItem("Box Graph");
		viewRandomDataGen = new JMenuItem("Random Data Generator");
		
		loadNetwork.addActionListener(this);
		saveNetwork.addActionListener(this);
		trainNetwork.addActionListener(this);
		resetNetwork.addActionListener(this);
		viewNodeIds.addActionListener(this);
		viewTrainingData.addActionListener(this);
		viewInputData.addActionListener(this);
		viewBoxGraph.addActionListener(this);
		viewRandomDataGen.addActionListener(this);
	
		fileMenu.add(loadNetwork);
		fileMenu.add(saveNetwork);
		networkMenu.add(trainNetwork);
		networkMenu.add(resetNetwork);
		viewMenu.add(viewTrainingData);
		viewMenu.add(viewInputData);
		modulesMenu.add(viewRandomDataGen);
		viewMenu.add(viewNodeIds);
		modulesMenu.add(viewBoxGraph);
		menuBar.add(fileMenu);
		menuBar.add(networkMenu);
		menuBar.add(viewMenu);
		menuBar.add(modulesMenu);
		
		this.setJMenuBar(menuBar);
	}
	
	protected void updateGraph() {
		
		theLayout = new CircleLayout<Node,FeedForwardLink>(theGraph.getGraph());
		theLayout.setSize(new Dimension(300,300)); 
		VisualizationViewer<Node,FeedForwardLink> vv = new VisualizationViewer<Node,FeedForwardLink>(theLayout);
		DefaultModalGraphMouse<Node,FeedForwardLink> gm = new DefaultModalGraphMouse<Node,FeedForwardLink>();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(gm);
		vv.setPreferredSize(new Dimension(400,400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(vv); 
		pack();
	}
	
	protected void showGraphFrame() {
		BoxGraphInterfacePanel newContentPane = new BoxGraphInterfacePanel(inputDataViewPanel,traininDataViewPanel);
        newContentPane.setOpaque(true); 
        graphFrame.setContentPane(newContentPane);
        
        graphFrame.pack();
        graphFrame.setVisible(true);
	}
	
	protected void showRandomDataGeneratorFrame() {
		RandomDataGeneratorPanel newContentPane = new RandomDataGeneratorPanel(traininDataViewPanel);
        newContentPane.setOpaque(true); 
        randomDataFrame.setContentPane(newContentPane);
        
        randomDataFrame.pack();
        randomDataFrame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loadNetwork) {
			doLoadNetwork();
		}else if(e.getSource() == saveNetwork) {
			doSaveNetwork();
		}else if(e.getSource() == trainNetwork ) {
			showTrainNetworkFrame();
		}else if(e.getSource() == viewNodeIds ) {
			showNodeIds();
		}else if(e.getSource().equals(viewInputData)) {
			showInputDataFrame();
		}else if(e.getSource().equals(viewBoxGraph)){
			showGraphFrame();
		}else if(e.getSource().equals(viewTrainingData)){
			showTrainingDataFrame();
		}else if(e.getSource().equals(viewRandomDataGen)) {
			showRandomDataGeneratorFrame();
		}else if(e.getSource().equals(resetNetwork)) {
			theGraph.getNetwork().linkLayers();
		}
	}
	
	public void showTrainingDataFrame() {
		if(theGraph.getNetwork().getLayerCount()>0) {
			traininDataViewPanel.setOpaque(true); 
			trainingDataFrame.setContentPane(traininDataViewPanel);
        
			trainingDataFrame.pack();
			trainingDataFrame.setVisible(true);
		}else {
			JOptionPane.showMessageDialog(this, "Network has no layers or nodes!", "Failed.", JOptionPane.ERROR_MESSAGE);
		}
	}
	

	protected void showInputDataFrame() {
		inputDataViewPanel.setOpaque(true); 
        inputDataFrame.setContentPane(inputDataViewPanel);
        
        inputDataFrame.pack();
        inputDataFrame.setVisible(true);
	}
	
	protected void doActivateNetwork() {
		theGraph.getNetwork().activateNetwork();
	}
	
	public void showNodeIds() {
		FeedForwardNodeIdTableView newContentPane = new FeedForwardNodeIdTableView(theGraph.getNetwork());
        newContentPane.setOpaque(true); 
        nodeIdViewFrame.setContentPane(newContentPane);
        
        nodeIdViewFrame.pack();
        nodeIdViewFrame.setVisible(true);
	}
	
	protected void showTrainNetworkFrame() {
        trainingFrame.setVisible(true);
	}
	
	protected void doLoadNetwork() {
		JFileChooser fc = new JFileChooser();
		if( fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			FeedForwardNetworkXML.loadNetwork(fc.getSelectedFile().getAbsolutePath());
		}
	}
	
	protected void doSaveNetwork() {
		JFileChooser fc = new JFileChooser();
		fc.setApproveButtonText("Save");
		if( fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			FeedForwardNetworkXML.writeFeedFowardNetwork(theGraph.getNetwork(), fc.getSelectedFile().getAbsolutePath());
		}
	}
}
