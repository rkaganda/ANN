package ann.ui.colorgraph;

import java.awt.Dimension;

public class InputBox {
	protected Dimension location;
	protected float value;
	protected boolean isTrainer;
	protected boolean isActive;
	
	protected InputBox() {
		location = null;
		value = -1;
		isTrainer = false;
		isActive = false;
	}
	
	protected InputBox(Dimension loc) {
		this();
		location = loc;
	}
	
	public Dimension getLocation() {
		return location;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float v) {
		value = v;
	}
	
	public boolean isTrainer() {
		return isTrainer;
	}
	
	public void isTrainer(boolean b) {
		isTrainer = b;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void isActive(boolean b) {
		isActive = b;
	}
}
