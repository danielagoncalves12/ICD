package tp1.protocol;

import java.io.FileOutputStream;
import java.io.IOException;
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

public class Session {

	public static String dataBasePath = "src//main//webapp//xml//PlayerInfo.xml";
	
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
		String queryRead   = "//Nickname";
        NodeList nicknames = null;          
        
		try {
			nicknames = (NodeList) (xPath.compile(queryRead).evaluate(doc, XPathConstants.NODESET));

		    for (int i = 0; i < nicknames.getLength(); i++) {
		        Element nickn = (Element) nicknames.item(i);
		        
		        if (nickn.getTextContent().equals(nickname)) return false;
		    }
		} catch (XPathExpressionException e) { e.printStackTrace(); }		
		return true;
	}

	public static void register(String nickname, String password, String picture) {
		
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
		
		// Guardar as suas informações
		Element playerNickname = doc.createElement("Nickname");
		playerNickname.setTextContent(nickname);
		player.appendChild(playerNickname);
		
		Element playerPassword = doc.createElement("Password");
		playerPassword.setTextContent(password);
		player.appendChild(playerPassword);
		
		Element playerPicture = doc.createElement("Picture");
		playerPicture.setTextContent(picture);
		player.appendChild(playerPicture);
		
		// Validação
		try {
			if (XMLUtils.validate(XMLUtils.documentToString(doc), "src//main//webapp//xml//PlayerInfo.xsd")) {
			
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
		String queryPassword = "//Player[Nickname='" + nickname + "']/Password";
        Node pass; 
		
		try {
			pass = ((NodeList) (xPath.compile(queryPassword).evaluate(doc, XPathConstants.NODESET))).item(0);

			if (password.equals(pass.getTextContent())) return true;
		    
		} catch (XPathExpressionException e) { e.printStackTrace(); }	
		return false;
	}
}
