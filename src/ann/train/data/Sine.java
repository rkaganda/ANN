package ann.train.data;

import java.lang.Math;

public class Sine implements Function {
	double a, b, c, d;
	
	public Sine(double _a, double _b, double _c, double _d) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
	}
	
	@Override
	public double calculate (double x) {
		return a*Math.sin(b * x + c) + d;
	}

	@Override
	public String getFunctionName() {
		return new String("Trinomial");
	}
}
