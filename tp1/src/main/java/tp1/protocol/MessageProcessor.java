package tp1.protocol;
import java.io.IOException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MessageProcessor {

	/**
	 * Processa uma mensagem de formato xml
	 * @param message
	 * @return
	 */
	public static String process(String message) {

		Document doc  = XMLUtils.stringToDocument(message);
		XPath xPath   = XPathFactory.newInstance().newXPath();
		String method = null;
		try {
			method = ((NodeList) xPath.compile("//method/@type").evaluate(doc, XPathConstants.NODESET)).item(0).getNodeValue();
		} catch (DOMException | XPathExpressionException e1) {
			e1.printStackTrace();
		}

		// Valida��o
		try {
			if (XMLUtils.validate(message, "src\\main\\webapp\\xml\\MessageValidate.xsd")) {

				switch(method) {
				
				case "board":    return board(doc);
				case "position": return position(doc);	
				}
			} else return null;
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String position(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
		String queryRead   = "//method/@read", queryPlayer = "//request/player";
        String queryChoice = "//request/argument", queryResult = "//reply/result";

        Node attRead = null, nodePlayer = null, nodeChoice = null, nodeResult = null;          
        
		try {
			attRead    = ((NodeList) xPath.compile(queryRead).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodePlayer = ((NodeList) xPath.compile(queryPlayer).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeChoice = ((NodeList) xPath.compile(queryChoice).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeResult = ((NodeList) xPath.compile(queryResult).evaluate(doc, XPathConstants.NODESET)).item(0);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (attRead.getNodeValue().equals("false")) return "position" + "," + nodePlayer.getTextContent() + "," + nodeChoice.getTextContent();
		else return nodeResult.getTextContent();		
	}
	
	private static String board(Document doc) {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        String queryRead   = "//method/@read", queryPlayer = "//request/player";
        String queryView = "//request/argument", queryResult = "//reply/result";
        
        Node attRead = null, nodeView = null, nodePlayer = null, nodeBoard = null;
        
		try {
			attRead    = ((NodeList) xPath.compile(queryRead).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeView   = ((NodeList) xPath.compile(queryView).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodePlayer = ((NodeList) xPath.compile(queryPlayer).evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeBoard  = ((NodeList) xPath.compile(queryResult).evaluate(doc, XPathConstants.NODESET)).item(0);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
 
		if (attRead.getNodeValue().equals("false")) return "board" + "," + nodePlayer.getTextContent() + "," + nodeView.getTextContent();
		else return nodeBoard.getTextContent();
	}
	
}