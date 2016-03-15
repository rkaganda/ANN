package ann.ui.colorgraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JPanel;

import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;


public class BoxGraphUI extends JPanel implements MouseListener, Runnable {
	private static final long serialVersionUID = 1L;
	public static final int GRAPH_SIZE = 50;
	public final static int BOX_SIZE = 10;
	
	protected BoxGraphInterfacePanel bgiPanel;
	protected boolean animateGraph;
	Thread animateThread;
	
	protected InputBox[][] inputBoxes;
	protected InputBox activeBox;
	
	protected BoxGraphUI() {
		inputBoxes = new InputBox[GRAPH_SIZE][GRAPH_SIZE];
		
		for(int i=0;i<GRAPH_SIZE;i++) {
			for(int j=0;j<GRAPH_SIZE;j++) {
				inputBoxes[i][j] = new InputBox(new Dimension(i,j));
			}
		}
		activeBox = inputBoxes[0][0];
		addMouseListener(this);
		this.setPreferredSize(new Dimension((GRAPH_SIZE)*BOX_SIZE,(GRAPH_SIZE)*BOX_SIZE));
	}
	
	public BoxGraphUI(BoxGraphInterfacePanel bgiP) {
		this();
		bgiPanel = bgiP;
		animateGraph = false;
		animateThread = null;
	}
	
	public void setActiveBoxValue(double v) {
		activeBox.setValue((float) v);
	}
	
	public void setBoxValue(int x, int y, double v, boolean b) {
		inputBoxes[x][y].setValue((float)v);
		inputBoxes[x][y].isTrainer(b);
	}
	
	public NetworkDataSet getBoxDataSet() {
		NetworkDataSet theDataSet = new NetworkDataSet();
		for(int i=0;i<GRAPH_SIZE;i++) {
			for(int j=0;j<GRAPH_SIZE;j++) {
				if(inputBoxes[i][j].getValue() != -1) {
					Vector<Double> iV = new Vector<Double>();
					Vector<Double> oV = new Vector<Double>();
					iV.add((double)i/(double)GRAPH_SIZE);
					iV.add((double)j/(double)GRAPH_SIZE);
					oV.add((double)inputBoxes[i][j].getValue());
					theDataSet.addNetworkDataPair(new NetworkDataPair(iV,oV));
				}
			}
		}
		return theDataSet;
	}
	
	public void clearValues() {
		for(int i=0;i<GRAPH_SIZE;i++) {
			for(int j=0;j<GRAPH_SIZE;j++) {
				inputBoxes[i][j].setValue(-1);
			}
		}
		repaint();
	}
	
	public void animateGraph(boolean b) {
		animateGraph = b;
		if(animateGraph) {
			animateThread = new Thread(this);
			animateThread.start();
		}else {
			try {
				animateThread.join(); //wait for the thread to die
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
	
	@Override public void paintComponent(Graphics g) {
		for(int i=0;i<GRAPH_SIZE;i++) {
			for(int j=0;j<GRAPH_SIZE;j++) {
				if(inputBoxes[i][j].getValue()==-1) {
					g.setColor(Color.WHITE);
					g.fillRect(i*BOX_SIZE, j*BOX_SIZE, BOX_SIZE, BOX_SIZE);
				}else {
					g.setColor(new Color(0,inputBoxes[i][j].getValue(),1-inputBoxes[i][j].getValue()));
					g.fillRect(i*BOX_SIZE, j*BOX_SIZE, BOX_SIZE, BOX_SIZE);
					if(inputBoxes[i][j].isTrainer()) {
						g.setColor(Color.WHITE);
						g.drawRect((i*BOX_SIZE)-1, (j*BOX_SIZE)-1, BOX_SIZE, BOX_SIZE);
					}
				}
				if(inputBoxes[i][j].isActive()) {
					g.setColor(Color.BLACK);
					g.drawRect((i*BOX_SIZE)-1, (j*BOX_SIZE)-1, BOX_SIZE, BOX_SIZE);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		activeBox.isActive(false);
		activeBox = inputBoxes[e.getX()/BOX_SIZE][e.getY()/BOX_SIZE];
		activeBox.isActive(true);
		repaint();
		validate();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void run() {
		while(animateGraph) {
			repaint();
			try {
				Thread.sleep(240);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bgiPanel.repaintDone();
		}
	}
}
