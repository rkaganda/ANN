package ann.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import ann.FeedForwardLayer;
import ann.FeedForwardLink;
import ann.FeedForwardNetwork;
import ann.NullThreshold;
import ann.SigmaThreshold;
import ann.ValueThreshold;
import ann.data.NetworkDataPair;
import ann.data.NetworkDataSet;

public class FeedForwardNetworkXML {
	public static FeedForwardNetwork loadNetwork(String fileName) {
		FeedForwardNetwork ffN = new FeedForwardNetwork();
		
		try {
    		File xmlFile = new File(fileName);
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(xmlFile);
        	
        	
        	//init some stuff
        	NodeList layerElements = doc.getElementsByTagName("layer");
        	NodeList linkElements = doc.getElementsByTagName("link");
        	NodeList layerChildren = null;
        	org.w3c.dom.Node tempNode = null;
        	NamedNodeMap linkAttributes = null;
        	String tString = null;
        	//get the layers
        	for(int i=0;i<layerElements.getLength();i++) { //<layer> peel back
        		layerChildren = layerElements.item(i).getChildNodes();
        		ffN.addLayer();
        		for(int j=0;j<layerChildren.getLength();j++) { //<node> peel back
        			tempNode = layerChildren.item(j);
        			if(tempNode.getNodeName().equals("node")) { //<node> type check
        				//create a Node with the given threshold
        				tString = tempNode.getAttributes().getNamedItem("threshold").getNodeValue();
        				if(tString.equals("Sigma")) {
        					ffN.addNode(new SigmaThreshold(), i);
        				}else if(tString.equals("null")) {
        					ffN.addNode(new NullThreshold(), i);
        				}else {
        					ffN.addNode(new ValueThreshold(new Double(tempNode.getAttributes().getNamedItem("threshold").getNodeValue())), i);
        				}
        			}
        		}
        	}
        	for(int i=0;i<linkElements.getLength();i++) { //<link> peel back
        		tempNode = linkElements.item(i);
        		linkAttributes = tempNode.getAttributes();
        		ffN.linkNodes(new Integer(linkAttributes.getNamedItem("layerindex").getNodeValue()),
        				new Integer(linkAttributes.getNamedItem("inputindex").getNodeValue()), 
        				new Integer(linkAttributes.getNamedItem("outputindex").getNodeValue()), 
        				new Double(linkAttributes.getNamedItem("weight").getNodeValue()));
        	}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ffN;
	}
	
	public static void writeFeedFowardNetwork(FeedForwardNetwork ffN, String fileName) {
		try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    		Document doc = docBuilder.newDocument();
    		
    		Element effN = doc.createElement("ffn");
    		
    		
    		for(int i=0;i<ffN.getLayerCount();i++) {
    			Element e = doc.createElement("layer");
    			effN.appendChild(e);
    			FeedForwardLayer ffnLayer = ffN.getLayer(i);
    			for(int j=0;j<ffnLayer.getNodeCount();j++) {
    				Element n = doc.createElement("node");
    				n.setAttribute("threshold", new String(ffnLayer.getNode(j).getThreshold().thresholdtoString()).toString());
    				e.appendChild(n);
    			}
    		}
    		
    		for(Iterator<FeedForwardLink> it=ffN.getLinks().iterator();it.hasNext();) {
    			FeedForwardLink ffLink = it.next();
    			Element l = doc.createElement("link");
    			effN.appendChild(l);
    			l.setAttribute("layerindex", new Integer(ffLink.getLayerIndex()).toString());
    			l.setAttribute("inputindex", new Integer(ffLink.getInputIndex()).toString());
    			l.setAttribute("outputindex", new Integer(ffLink.getOutputIndex()).toString());
    			l.setAttribute("weight", new Double(ffLink.getWeight()).toString());
    		}
    		doc.appendChild(effN);
    		
    		DOMSource domSource = new DOMSource(doc);
		    StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.transform(domSource, result);
		    
		    File xmlFile = new File(fileName);
		    FileWriter fstream = new FileWriter(xmlFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(writer.toString());
		    out.close();
    	}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readNodeInputLayer(FeedForwardNetwork ffN, String fileName) {
		try {
    		File xmlFile = new File(fileName);
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(xmlFile);
        	
        	//init some stuff
        	NodeList layerElements = doc.getElementsByTagName("layer");
        	NodeList inputLayer = null;
        	org.w3c.dom.Node tempNode = null;
        	
        	inputLayer = layerElements.item(0).getChildNodes();
        	int nodeIndex = 0; //the index of the node value read
        	for(int j=0;j<inputLayer.getLength();j++) { //<node> peel back
        		tempNode = inputLayer.item(j);
        		if(tempNode.getNodeName().equals("node")) { //<node> type check
        			//read the value and add it to the vector
        			ffN.setNodeValue(nodeIndex, 
        					new Double(tempNode.getAttributes().getNamedItem("value").getNodeValue()));
        			nodeIndex++;
        		}
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void writeNodeValues(FeedForwardNetwork ffN, String fileName) {
		try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    		Document doc = docBuilder.newDocument();
    		
    		Element outputLayer = doc.createElement("ffnvalues");
    		
    		
    		for(int i=0;i<ffN.getLayerCount();i++) {
    			Element e = doc.createElement("layer");
    			outputLayer.appendChild(e);
    			FeedForwardLayer ffnLayer = ffN.getLayer(i);
    			for(int j=0;j<ffnLayer.getNodeCount();j++) {
    				Element n = doc.createElement("node");
    				n.setAttribute("value", new Double(ffnLayer.getNode(j).getValue()).toString());
    				e.appendChild(n);
    			}
    		}
    		
    		doc.appendChild(outputLayer);
    		
    		DOMSource domSource = new DOMSource(doc);
		    StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.transform(domSource, result);
		    
		    File xmlFile = new File(fileName);
		    FileWriter fstream = new FileWriter(xmlFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(writer.toString());
		    out.close();
    	}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportDataSet(NetworkDataSet dataSet, String fileName) {
		try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    		Document doc = docBuilder.newDocument();
    		
    		Element outputLayer = doc.createElement("dataset");
    		
    		
    		for(int i=0;i<dataSet.size();i++) { //for each training pair
    			Element e = doc.createElement("datapair");
    			outputLayer.appendChild(e);
    			NetworkDataPair dataPair = dataSet.getDataPair(i);
    			Element inputE = doc.createElement("inputs"); //create input element
    			for(int j=0;j<dataPair.getInputData().size();j++) { //for each input value
    				Element v = doc.createElement("value"); //create the value element
    				v.appendChild(doc.createTextNode(new Double(dataPair.getInputData().get(j)).toString()));
    				inputE.appendChild(v);
    			}
    			e.appendChild(inputE);
    			Element outputE = doc.createElement("outputs"); //create output element
    			for(int j=0;j<dataPair.getOutputData().size();j++) { //for each input value
    				Element v = doc.createElement("value");
    				v.appendChild(doc.createTextNode(new Double(dataPair.getOutputData().get(j)).toString()));
    				outputE.appendChild(v);
    			}
    			e.appendChild(outputE);
    		}
    		
    		doc.appendChild(outputLayer);
    		
    		DOMSource domSource = new DOMSource(doc);
		    StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.transform(domSource, result);
		    
		    File xmlFile = new File(fileName);
		    FileWriter fstream = new FileWriter(xmlFile);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(writer.toString());
		    out.close();
    	}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static NetworkDataSet loadDataSet(String fileName) {
		NetworkDataSet trainingSet = new NetworkDataSet();
		Vector<Double> tempInputVector = null;
		Vector<Double> tempOutputVector = null;
		
		try {
    		File xmlFile = new File(fileName);
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(xmlFile);
        	
        	//init some stuff
        	NodeList learningPairs = doc.getElementsByTagName("datapair");
        	NodeList inputoutputPair = null;
        	for(int i=0;i<learningPairs.getLength();i++) { //<datapair> peel back
        		inputoutputPair = learningPairs.item(i).getChildNodes();
        		for(int j=0;j<inputoutputPair.getLength();j++) {
        			if(inputoutputPair.item(j).getNodeName().equals("inputs")) { //<inputs> peel back
        				tempInputVector = new Vector<Double>();
        				for(int k=0;k<inputoutputPair.item(j).getChildNodes().getLength();k++) {
        					if(inputoutputPair.item(j).getChildNodes().item(k).getNodeName().equals("value")) { //<value> peel back
        						tempInputVector.add(new Double(inputoutputPair.item(j).getChildNodes().item(k).getFirstChild().getNodeValue()));
        					}
        				}
        			}else if(inputoutputPair.item(j).getNodeName().equals("outputs")) { //<outputs> peel back
        				tempOutputVector = new Vector<Double>();
        				for(int k=0;k<inputoutputPair.item(j).getChildNodes().getLength();k++) {
        					if(inputoutputPair.item(j).getChildNodes().item(k).getNodeName().equals("value")) { //<value> peel back
        						tempOutputVector.add(new Double(inputoutputPair.item(j).getChildNodes().item(k).getFirstChild().getNodeValue()));
        					}
        				}
        			}
        		}
        		trainingSet.addNetworkDataPair(new NetworkDataPair(tempInputVector,tempOutputVector));
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
		return trainingSet;
	}
}
