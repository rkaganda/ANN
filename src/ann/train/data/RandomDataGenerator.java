package ann.train.data;

import java.util.Random;
import java.util.Vector;

import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;
public class RandomDataGenerator {
	
	public static NetworkDataSet generateDataSet(Function func, double xmin, double xmax, double ymin, double ymax, int count) {
		NetworkDataSet tds = new NetworkDataSet();
		
		float oneCount = 0;
		float zeroCount = 0;
		
		//for(int i = 0; i < count; i++) {
		int i = 0;
		while(i < count) {
			double x = new Random().nextDouble() * (xmax - xmin) + xmin;
			double y = new Random().nextDouble() * (ymax - ymin) + ymin;
			double z = 0;
			if (y > func.calculate(x)) {
				z = 1;
			}
			if(z == 1 && oneCount * 2 <= count) {
				oneCount++;
				i++;
				tds = addDataPair(x, y, z, tds);
			}
			if(z == 0 && zeroCount * 2 <= count) {
				zeroCount++;
				i++;
				tds = addDataPair(x, y, z, tds);
			}
			
		}
		return tds;
	}
	
	protected static NetworkDataSet addDataPair(double x, double y, double z, NetworkDataSet tds) {
		Vector<Double> inputs = new Vector<Double>();
		inputs.add(x);
		inputs.add(y);
		Vector<Double> outputs = new Vector<Double>();
		outputs.add(z);
		NetworkDataPair tdp = new NetworkDataPair(inputs, outputs);
		tds.addNetworkDataPair(tdp);
		
		return tds;
		
	}
	
	
}
