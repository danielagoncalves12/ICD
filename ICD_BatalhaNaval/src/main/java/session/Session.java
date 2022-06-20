package session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
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

import protocol.XMLUtils;

public class Session {

	public static String dataBasePath = "src/main/java/protocol/PlayerInfo.xml";
	
	public static boolean availableNickname(String nickname) {
		
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
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
		String queryRead   = "//Player";
        NodeList nicknames = null;          
        
		try {
			nicknames = (NodeList) (xPath.compile(queryRead).evaluate(doc, XPathConstants.NODESET));

		    for (int i = 0; i < nicknames.getLength(); i++) {
		        String nickn = ((Element) nicknames.item(i)).getAttribute("Nickname");
		        
		        if (nickn.equals(nickname)) return false;
		    }
		} catch (XPathExpressionException e) { e.printStackTrace(); }		
		return true;
	}

	public static void register(String nickname, String name, String password, String color, String date, String picture) {
		
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
		
		// Adicionar novo jogador
		Element root   = doc.getDocumentElement();
		Element player = doc.createElement("Player");
		root.appendChild(player);
		
		// Guardar as suas informa��es
		
		// Nickname
		player.setAttribute("Nickname", nickname);
	
		// Nome p�blico
		Element playerName = doc.createElement("Name");
		playerName.setTextContent(name);
		player.appendChild(playerName);
		
		// Palavra-passe
		Element playerPassword = doc.createElement("Password");
		playerPassword.setTextContent(password);
		player.appendChild(playerPassword);
		
		// Cor favorita
		Element playerColor = doc.createElement("Color");
		playerColor.setTextContent(color);
		player.appendChild(playerColor);
		
		// Data de nascimento
		Element playerDate = doc.createElement("Date");
		playerDate.setTextContent(date);
		player.appendChild(playerDate);
		
		// Imagem
		Element playerPicture = doc.createElement("Picture");
		playerPicture.setTextContent(picture);
		player.appendChild(playerPicture);
		
		// N�mero de vit�rias
		Element playerWinsNumber = doc.createElement("WinsNumber");
		playerWinsNumber.setTextContent("0");
		player.appendChild(playerWinsNumber);
		
		// Valida��o
		try {
			if (XMLUtils.validate(XMLUtils.documentToString(doc), "src/main/java/protocol/PlayerInfo.xsd")) {
			
				// Atualizar o novo ficheiro
				try (FileOutputStream output = new FileOutputStream(dataBasePath)) {
					
				    TransformerFactory transformerFactory = TransformerFactory.newInstance();
				    Transformer transformer = transformerFactory.newTransformer();
				    DOMSource source = new DOMSource(doc);
				    StreamResult result = new StreamResult(output);
	
				    transformer.transform(source, result);
				}
			}
	   } catch (IOException | TransformerException | SAXException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
	   }

	}
	
	public static boolean login(String nickname, String password) {

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

		XPath xPath  = XPathFactory.newInstance().newXPath();
		String queryPassword = "//Player[@Nickname='" + nickname + "']/Password";
        Node pass; 
		
		try {
			// Desencripta��o da palavra-passe
			pass = ((Node) (xPath.compile(queryPassword).evaluate(doc, XPathConstants.NODE)));
			
			if (pass == null) return false;
			if (MD5(password).equals(pass.getTextContent())) return true;

		} catch (XPathExpressionException e) { e.printStackTrace(); }	
		return false;
	}
	
    public static String MD5(String password) {

    	String hashPassword = null;
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(password.getBytes());
		    byte[] digest = md.digest();
		    hashPassword = DatatypeConverter.printHexBinary(digest).toUpperCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashPassword;   
     }
}
