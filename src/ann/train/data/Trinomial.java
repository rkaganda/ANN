package ann.train.data;

public class Trinomial implements Function {
	double a, b, c, d;
	
	public Trinomial(double _a, double _b, double _c, double _d) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
	}
	
	@Override
	public double calculate (double x) {
		return (a * x * x * x) + b * x * x + c* x + d;
	}

	@Override
	public String getFunctionName() {
		return new String("Trinomial");
	}
}
