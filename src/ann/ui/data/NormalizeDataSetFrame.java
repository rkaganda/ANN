package ann.ui.data;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NormalizeDataSetFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JPanel fieldPanel, buttonPanel, framePanel;
	protected JButton normalizeButton;
	protected JTextField maxAField, minAField, maxBField, minBField;
	protected NormalizeFrameListener nfListener;
	protected Vector<Double> normValues;
	
	protected NormalizeDataSetFrame() {
		fieldPanel = new JPanel();
		buttonPanel = new JPanel();
		framePanel = new JPanel();
		GridLayout gLayout = new GridLayout(2,4);
		gLayout.setHgap(5);
		gLayout.setVgap(5);
		fieldPanel.setLayout(gLayout);
		framePanel.setLayout(new BoxLayout(framePanel,BoxLayout.Y_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		minAField = setupField(minAField,"Minimum InputA Value");
		minBField = setupField(minBField,"Minimum InputB Value");
		maxAField = setupField(maxAField,"Maximum InputA Value");
		maxBField = setupField(maxBField,"Maximum InputB Value");
		normalizeButton = setupButton(normalizeButton,"Process");
		framePanel.add(fieldPanel,BorderLayout.CENTER);
		framePanel.add(buttonPanel,BorderLayout.SOUTH);
		
		framePanel.setOpaque(true); 
        setContentPane(framePanel);
        pack();
	}
	
	public NormalizeDataSetFrame(NormalizeFrameListener nfl) {
		this();
		nfListener = nfl;
		normValues = null;
	}
	
	protected JTextField setupField(JTextField field, String label) {
		fieldPanel.add(new JLabel(label));
		field = new JTextField();
		fieldPanel.add(field);
		return field;
	}
	
	protected JButton setupButton(JButton button, String name) {
		button = new JButton(name);
		buttonPanel.add(button);
		button.addActionListener(this);
		return button;
	}
	
	public Vector<Double> getNormalizeValues() {
		return normValues;
	}
	
	protected void readFields() {
		try{
			normValues = new Vector<Double>();
			normValues.add(new Double(minAField.getText()));
			normValues.add(new Double(maxAField.getText()));
			normValues.add(new Double(minBField.getText()));
			normValues.add(new Double(maxBField.getText()));
	
			nfListener.doneNormalizing();
		}catch(NumberFormatException e) {
			System.out.println("bad numbers");
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(normalizeButton)) {
			readFields();
			nfListener.doneNormalizing();
			setVisible(false);
		}
	}
}
