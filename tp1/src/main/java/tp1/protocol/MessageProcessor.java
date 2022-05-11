package tp1.protocol;

import java.util.Arrays;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tp1.battleship.GameView;
import tp1.battleship.ShipType;

public class MessageProcessor {

	/**
	 * Processa uma mensagem de formato xml
	 * @param message
	 * @return
	 */
	public static String process(String message) {

		Document doc  = XMLUtils.stringToDocument(message);

		Node protocol = doc.getElementsByTagName("Protocol").item(0);
		String method = protocol.getFirstChild().getNodeName();

		// Validação
			//if (XMLUtils.validate(message, "src\\main\\webapp\\xml\\MessageValidate.xsd")) {

		switch(method) {
		
		case "Board": return board(doc);
		case "Play":  return play(doc);	
		case "Info":  return info(doc);
		}
		return null;
	}
	
	private static String play(Document doc) {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node attRead = null, nodePlayer = null, nodeChoice = null, nodeResult = null;          
        
		try {
			attRead    = ((NodeList) xPath.compile("//Play/@Read").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeChoice = ((NodeList) xPath.compile("//Request/Position").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeResult = ((NodeList) xPath.compile("//Reply").evaluate(doc, XPathConstants.NODESET)).item(0);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (attRead.getNodeValue().equals("false")) return "Position" + "," + nodePlayer.getTextContent() + "," + nodeChoice.getTextContent();
		else return nodeResult.getTextContent();		
	}
	
	private static String board(Document doc) {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node attRead = null, nodeView = null, nodePlayer = null;
        int[][] board = null;
        
		try {
			attRead    = ((NodeList) xPath.compile("//Board/@Read").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeView   = ((NodeList) xPath.compile("//Request/View").evaluate(doc, XPathConstants.NODESET)).item(0);

			if (attRead.getNodeValue().equals("true")) {
				
				board = new int[10][10];
				Arrays.stream(board).forEach(a -> Arrays.fill(a, 0));
				
				NodeList positions0 = ((NodeList) xPath.compile("//Empty/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions1 = ((NodeList) xPath.compile("//Aircraft/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions2 = ((NodeList) xPath.compile("//Tanker/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions3 = ((NodeList) xPath.compile("//Destroyer/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions4 = ((NodeList) xPath.compile("//Submarine/Position").evaluate(doc, XPathConstants.NODESET));

				for (int i = 0; i < positions0.getLength(); i++) {
	
					String value = positions0.item(i).getTextContent();
					int lin = Character.getNumericValue(value.charAt(0));
					int col = Character.getNumericValue(value.charAt(1));

					board[lin][col] = ShipType.EMPTY;
				}
				
				for (int i = 0; i < positions1.getLength(); i++) {
					String value = positions1.item(i).getTextContent();
					int lin = Character.getNumericValue(value.charAt(0));
					int col = Character.getNumericValue(value.charAt(1));
					
					board[lin][col] = ShipType.TYPE1SHOW;
				}
				
				for (int i = 0; i < positions2.getLength(); i++) {
					String value = positions2.item(i).getTextContent();
					int lin = Character.getNumericValue(value.charAt(0));
					int col = Character.getNumericValue(value.charAt(1));
					
					board[lin][col] = ShipType.TYPE2SHOW;
				}
				
				for (int i = 0; i < positions3.getLength(); i++) {
					String value = positions3.item(i).getTextContent();
					int lin = Character.getNumericValue(value.charAt(0));
					int col = Character.getNumericValue(value.charAt(1));
					
					board[lin][col] = ShipType.TYPE3SHOW;
				}
				
				for (int i = 0; i < positions4.getLength(); i++) {
					String value = positions4.item(i).getTextContent();
					int lin = Character.getNumericValue(value.charAt(0));
					int col = Character.getNumericValue(value.charAt(1));
					
					board[lin][col] = ShipType.TYPE4SHOW;
				}
			}
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (attRead.getNodeValue().equals("false")) return "Board" + "," + nodePlayer.getTextContent() + "," + nodeView.getTextContent();
		else return GameView.printBoard(nodePlayer.getTextContent(), nodeView.getTextContent(), board);
	}
	
	private static String info(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node attRead = null, nodeType = null, nodePlayer = null, nodeInfo = null;
        
		try {
			attRead    = ((NodeList) xPath.compile("//Info/@Read").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeType   = ((NodeList) xPath.compile("//Request/Info").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeInfo   = ((NodeList) xPath.compile("//Reply").evaluate(doc, XPathConstants.NODESET)).item(0);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
 
		if (attRead.getNodeValue().equals("false")) return "Info" + "," + nodePlayer.getTextContent() + "," + nodeType.getTextContent();
		else return nodeInfo.getTextContent();
	}

}
