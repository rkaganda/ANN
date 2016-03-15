/*
 * WeightedNode
 * 
 * Author(s): Rukundo Kaganda (rkaganda@gmail.com)
 * 
 */
package ann;

import java.util.Iterator;

public class WeightedNode extends Node {
	public WeightedNode() {
		this.threshold = new NullThreshold(); 
	}
	
	@Override
	public boolean isWeighted() {
		return true;
	}
	
	public void setActive(boolean b) {
		isActive = b;
		if(isActive) {
			setValue(1);
			for(Iterator<Link> it = outputLinks.iterator();it.hasNext();) {
				it.next().nodeFired(1);
			}
		}
	}
}
