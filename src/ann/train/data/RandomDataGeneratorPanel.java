package ann.train.data;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ann.data.NetworkDataSet;
import ann.ui.data.TrainingDataViewPanel;

public class RandomDataGeneratorPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected TrainingDataViewPanel tdvPanel;
	protected JButton insertParabolaDataButton, insertTrinomialDataButton, insertSineDataButton;
	protected JTextField a_tf, b_tf, c_tf, d_tf, count_tf, xmin_tf, xmax_tf, ymin_tf, ymax_tf;
	protected JLabel a_l, b_l, c_l, xmin_l, xmax_l, ymin_l, ymax_l;
	
	protected RandomDataGeneratorPanel() {
		super(new GridLayout(3,4));
		insertParabolaDataButton = setupButton(insertParabolaDataButton,"insertParabolaData");
		insertTrinomialDataButton = setupButton(insertTrinomialDataButton,"insertTrinomialData");
		insertSineDataButton = setupButton(insertSineDataButton,"insertSineData");
		count_tf = addTextField(count_tf, "100");
		
		a_tf = addTextField(a_tf, "-1");
		b_tf = addTextField(b_tf, "0");
		c_tf = addTextField(c_tf, "1");
		d_tf = addTextField(d_tf, "1");
		
		xmin_tf = addTextField(xmin_tf, "-1");
		xmax_tf = addTextField(xmax_tf, "1");
		ymin_tf = addTextField(ymin_tf, "-1");
		ymax_tf = addTextField(ymax_tf, "1");
	}
	
	protected JTextField addTextField(JTextField tf, String defaultValue) {
		tf = new JTextField();
		tf.setText(defaultValue);
		add(tf);
		return tf;
	}
	
	public RandomDataGeneratorPanel(TrainingDataViewPanel tdVP) {
		this();
		tdvPanel = tdVP;
	}
	
	private JButton setupButton(JButton button, String label ) {
		button = new JButton(label);
		button.addActionListener(this);
		add(button);
		return button;
	}
	
	protected void insertParabolaData() {
		double a = new Double(a_tf.getText());
		double b = new Double(b_tf.getText());
		double c = new Double(c_tf.getText());
		
		Parabola par =new Parabola(a, b, c);
		generateData(par);
	}
	
	protected void insertTrinomialData() {
		double a = new Double(a_tf.getText());
		double b = new Double(b_tf.getText());
		double c = new Double(c_tf.getText());
		double d = new Double(d_tf.getText());
		
		Trinomial tri =new Trinomial(a, b, c, d);
		generateData(tri);
	}
	
	protected void insertSineData() {
		double a = new Double(a_tf.getText());
		double b = new Double(b_tf.getText());
		double c = new Double(c_tf.getText());
		double d = new Double(c_tf.getText());
		
		Sine sin = new Sine(a, b, c, d);
		generateData(sin);
	}
	
	protected void generateData(Function func) {
		double xmin = new Double(xmin_tf.getText());
		double xmax = new Double(xmax_tf.getText());
		double ymin = new Double(ymin_tf.getText());
		double ymax = new Double(ymax_tf.getText());
		int count = new Integer(count_tf.getText());
		NetworkDataSet tds = new NetworkDataSet();
		tds = RandomDataGenerator.generateDataSet(func, xmin, xmax, ymin, ymax, count);
		tdvPanel.updateTrainingData(tds);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(insertParabolaDataButton)) {
			insertParabolaData();
		}
		if(e.getSource().equals(insertTrinomialDataButton)) {
			insertTrinomialData();
		}
		if(e.getSource().equals(insertSineDataButton)) {
			insertSineData();
		}
	}
}
