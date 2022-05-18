package tp1.session;

import java.io.FileOutputStream;
import java.io.IOException;
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
import org.xml.sax.SAXException;

public class Profile {

	public static String dataBasePath = "src//main//webapp//xml//PlayerInfo.xml";
	
	public static void uploadProfilePicture(String nickname, String imagePath) {
		
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
			String query = "//Player[Nickname='" + nickname + "']/Picture";
			nodePicture = (Node) xPath.compile(query).evaluate(doc, XPathConstants.NODE);		
			nodePicture.setTextContent(imagePath); 
				
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
}
