/*
 * NetworkDataSet
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann.data;

import java.util.Vector;

public class NetworkDataSet implements Cloneable {
	protected Vector<NetworkDataPair> dataPairs;
	protected boolean isEmpty;
	
	public NetworkDataSet() {
		isEmpty = true;
		dataPairs = new Vector<NetworkDataPair>();
	}
	
	public NetworkDataSet(Object object) {
		dataPairs = (Vector<NetworkDataPair>) object;
	}
	
	public NetworkDataSet(NetworkDataPair dP) {
		this();
		dataPairs.add(dP);
	}
	
	public void addNetworkDataPair(NetworkDataPair dP) {
		synchronized(this) {
			if(!isEmpty) {
				if(dataPairs.iterator().next().getInputData().size()==dP.getInputData().size() &
						dataPairs.iterator().next().getOutputData().size()==dP.getOutputData().size()) {
					dataPairs.add(dP);
				}else {
					System.out.println("datapair mismatch, pair not added to set.");
					//TODO throw exception
				}
			}else {
				dataPairs.add(dP);
				isEmpty = false;
			}
		}
	}
	
	public NetworkDataPair getDataPair(int index) {
		synchronized(this) {
			return dataPairs.get(index);
		}
	}
	
	public Vector<NetworkDataPair> getDataSet() {
		return dataPairs;
	}
	
	public int size() {
		synchronized(this) {
			return dataPairs.size();
		}
	}
	
	public NetworkDataSet clone() {
		synchronized(this) {
			NetworkDataSet ndS = new NetworkDataSet();
			Vector<Double> iV = null;
			Vector<Double> oV = null;
			for(int i=0;i<dataPairs.size();i++) {
				iV = new Vector<Double>();
				for(int j=0;j<dataPairs.get(i).getInputData().size();j++) {
					iV.add(new Double(dataPairs.get(i).getInputData().get(j)));
				}
				oV = new Vector<Double>();
				for(int j=0;j<dataPairs.get(i).getOutputData().size();j++) {
					oV.add(new Double(dataPairs.get(i).getOutputData().get(j)));
				}
				ndS.addNetworkDataPair(new NetworkDataPair(iV,oV));
			}
			return ndS;
		}
	}
}
