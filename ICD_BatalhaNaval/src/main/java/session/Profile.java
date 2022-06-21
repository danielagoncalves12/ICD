package session;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Profile {

	public static String dataBasePath   = "src/main/java/protocol/PlayerInfo.xml";
	public static String honorBoardPath = "src/main/java/protocol/HonorBoard.xml";
	
	// UPLOAD
	
	public static String upload(String contentType, String username, String value) {
		 
		if (!(contentType.equals("Picture") || contentType.equals("Name") || contentType.equals("Color") || contentType.equals("Date")))
			return "";

		if (contentType.equals("Picture")) {
			
			byte[] image = Base64.getDecoder().decode(value);
			BufferedImage img = null;
			
			try {
				String filename = username + ".png";
				img = ImageIO.read(new ByteArrayInputStream(image));
				File out = new File("src/main/webapp/pictures/" + filename);
				ImageIO.write(img, "png", out);
				
				value = filename;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
			String query = "//Player[@Username='" + username + "']/" + contentType;
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

	// GETTER
	
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
			String query = "//Player[@Username='" + nickname + "']/Picture";
			nodePicture = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		String filename = nodePicture.getTextContent();
		BufferedImage image;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String img = null;
		
		try {
			image = ImageIO.read(new File("src/main/webapp/pictures/" + filename));
			ImageIO.write(image, "png", out);
			byte[] array = out.toByteArray();
			img = Base64.getEncoder().encodeToString(array);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return img;
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
			String query = "//Player[@Username='" + nickname + "']/Name";
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
			String query = "//Player[@Username='" + username + "']/Color";
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
			String query = "//Player[@Username='" + username + "']/Date";
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
			String query = "//Player[@Username='" + username + "']/WinsNumber";
			nodeWin = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);;

		} catch (XPathExpressionException e) { 
			e.printStackTrace(); 
		}
		
		return nodeWin.getTextContent();
	}
	
	public static HashMap<String, String> getHonorBoard() {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        LinkedHashMap<String, String> players = new LinkedHashMap<>();
        
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(honorBoardPath);			
			XPath xPath = XPathFactory.newInstance().newXPath();

			for (int num = 1; num <= 10; num++) {
							
				Node nodePlayer = ((Node) xPath.compile("//Player[@Top='" + num + "']").evaluate(doc, XPathConstants.NODE));			
				String username = nodePlayer.getAttributes().item(1).getNodeValue();
				String winsNum  = nodePlayer.getChildNodes().item(0).getTextContent();
				players.put(username, winsNum);
			}			

		} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return players;
	}
	
	// Update
	
	public static void updateWinsNumber(String username) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);
			XPath xPath = XPathFactory.newInstance().newXPath();

	        String query = "//Player[@Username='" + username + "']/WinsNumber";
	        Node nodeWinsNumber = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);
			int winsNumber = Integer.valueOf(nodeWinsNumber.getTextContent());
			nodeWinsNumber.setTextContent(String.valueOf(++winsNumber)); 
			
		} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
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
	
	public static void updateHonorBoard() {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        HashMap<String, Integer> players = new HashMap<>();
        
        // Obter os dados dos jogadores
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(dataBasePath);			
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        NodeList nodePlayers = null;            
	        
			String query = "//Player";
			nodePlayers = (NodeList) xPath.compile(query).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodePlayers.getLength(); i++) {
				String username  = nodePlayers.item(i).getAttributes().item(0).getNodeValue();
				String winNum = ((Node) xPath.compile("//Player[@Username='" + username + "']/WinsNumber").evaluate(doc, XPathConstants.NODE)).getTextContent();
				
				players.put(username, Integer.valueOf(winNum));
			}		
		} catch (XPathExpressionException| SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}

        // Ordenar o dicionario de acordo com o numero de vitorias
		LinkedHashMap<String, Integer> playersOrder = new LinkedHashMap<>();
		
		players.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
		  .limit(10)
		  .forEachOrdered(x -> playersOrder.put(x.getKey(), x.getValue()));
		 		
		// Atualizar a base de dados do quadro de honra
        try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(honorBoardPath);		
			Element root = doc.getDocumentElement();
			
			NodeList childNodes = root.getChildNodes();
			while (childNodes.getLength() > 0) {
				root.removeChild(childNodes.item(0));
			}
			
			int i = 0;
			for (String key : playersOrder.keySet()) {
				
				Element player = doc.createElement("Player");
				player.setAttribute("Top", String.valueOf(++i));
				player.setAttribute("Username", key);
				
				Element winsNum = doc.createElement("WinsNum");
				winsNum.setTextContent(String.valueOf(players.get(key)));
				player.appendChild(winsNum);
				
		 		root.appendChild(player);
			}
						
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	
 		// Atualizar as alteracoes
		try (FileOutputStream output = new FileOutputStream(honorBoardPath)) {
			
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(output);
		    transformer.transform(source, result);
		    
		} catch (IOException | TransformerException e) {
			e.printStackTrace();
		}
	}

	public static String hex2Rgb(String colorStr) {
		
		return "rgba(" + Integer.valueOf(colorStr.substring(1, 3), 16) +
			   "," + Integer.valueOf(colorStr.substring(3, 5), 16 ) +
			   "," + Integer.valueOf(colorStr.substring(5, 7), 16 ) + ", 0.5)";
	}
}
