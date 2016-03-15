package ann.train.data;

public class Parabola implements Function {
	double a, b, c;
	
	public Parabola(double _a, double _b, double _c) {
		a = _a;
		b = _b;
		c = _c;
	}
	
	@Override
	public double calculate (double x) {
		return (a * x * x) + b * x + c;
	}

	@Override
	public String getFunctionName() {
		return new String("Parabola");
	}
}
