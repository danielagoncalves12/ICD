package session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Profile {

	public static String dataBasePath = "src//main//webapp//xml//PlayerInfo.xml";
	
	public static String upload(String contentType, String username, String value) {
		 
		if (!(contentType.equals("Picture") || contentType.equals("Name") || contentType.equals("Color") || contentType.equals("Date")))
			return "";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = null;    
        
		try {
			String query = "//Player[@Nickname='" + username + "']/" + contentType;
			node = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);		
			node.setTextContent(value); 
				
		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		// Atualizar o novo ficheiro
		try (FileOutputStream output = new FileOutputStream(dataBasePath)) {
			
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(output);
		    transformer.transform(source, result);
		    
		} catch (IOException | TransformerException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	public static void uploadWinsNumber(String nickname) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodeWinsNumber = null;    
        
		try {
			String query = "//Player[@Nickname='" + nickname + "']/WinsNumber";
			nodeWinsNumber = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);
			int winsNumber = Integer.valueOf(nodeWinsNumber.getTextContent());
			nodeWinsNumber.setTextContent(String.valueOf(++winsNumber)); 

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		// Atualizar o novo ficheiro
		try (FileOutputStream output = new FileOutputStream(dataBasePath)) {
			
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(output);
		    transformer.transform(source, result);
		    
		} catch (IOException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPicture(String nickname) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodePicture = null;    
        
		try {
			String query = "//Player[@Nickname='" + nickname + "']/Picture";
			nodePicture = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		return nodePicture.getTextContent();
	}
	
	public static String getName(String nickname) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodeName = null;    
        
		try {
			String query = "//Player[@Nickname='" + nickname + "']/Name";
			nodeName = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		return nodeName.getTextContent();
	}
	
	public static String getColor(String username) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodeColor = null;    
        
		try {
			String query = "//Player[@Nickname='" + username + "']/Color";
			nodeColor = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		return nodeColor.getTextContent();
	}
	
	public static String getDate(String username) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodeDate = null;    
        
		try {
			String query = "//Player[@Nickname='" + username + "']/Date";
			nodeDate = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		return nodeDate.getTextContent();
	}
	
	public static String getWinNum(String username) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node nodeWin = null;    
        
		try {
			String query = "//Player[@Nickname='" + username + "']/WinsNumber";
			nodeWin = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		return nodeWin.getTextContent();
	}
	
	public static HashMap<String, Integer> getHonorBoard() {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodePlayers = null;    
        HashMap<String, Integer> players = new HashMap<>();
        
		try {
			String query = "//Player";
			nodePlayers = (NodeList) xPath.compile(query).evaluate(doc, XPathConstants.NODESET);

			System.out.println(nodePlayers.getLength());
			
			for (int i = 0; i < nodePlayers.getLength(); i++) {
				String username  = nodePlayers.item(i).getAttributes().item(0).getNodeValue();
				String winNum = ((Node) xPath.compile("//Player[@Nickname='" + username + "']/WinsNumber").evaluate(doc, XPathConstants.NODE)).getTextContent();
				
				players.put(username, Integer.valueOf(winNum));
			}
			
		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		LinkedHashMap<String, Integer> playersOrder = new LinkedHashMap<>();
		
		players.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
		  .limit(10)
		  .forEachOrdered(x -> playersOrder.put(x.getKey(), x.getValue()));
		 
		return playersOrder;
	}
	
	public static void main(String[] args) {
		
		getHonorBoard();
	}
	
	public static String hex2Rgb(String colorStr) {
		
		return "rgba(" + Integer.valueOf( colorStr.substring( 1, 3 ), 16 ) +
			   "," + Integer.valueOf( colorStr.substring( 3, 5 ), 16 ) +
			   "," + Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) + ", 0.5)";
	}
}
