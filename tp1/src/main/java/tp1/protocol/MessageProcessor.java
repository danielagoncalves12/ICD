package tp1.protocol;

import java.io.IOException;
import java.util.Arrays;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		try {
			if (XMLUtils.validate(message, "src\\main\\webapp\\xml\\MessageValidate.xsd")) {
				
				switch(method) {
				
				case "Board": return board(doc);
				case "Play":  return play(doc);	
				case "Info":  return info(doc);
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Messagem inválida! ->" + message);
		return null;
	}
	
	private static String play(Document doc) {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodePlayer = null, nodeChoice = null, nodeResult = null;
        boolean isReply = false;          
        
		try {
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeChoice = ((NodeList) xPath.compile("//Request/Position").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeResult = ((NodeList) xPath.compile("//Reply/Result").evaluate(doc, XPathConstants.NODESET)).item(0);
			isReply    = (boolean) xPath.compile("boolean(//Reply/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (isReply) return nodeResult.getTextContent();
		else return "Play" + "," + nodePlayer.getTextContent() + "," + nodeChoice.getTextContent();
	}
	
	private static String board(Document doc) {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeView = null, nodePlayer = null;
        int[][] board = null;
        boolean isReply = false;
        
		try {
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeView   = ((NodeList) xPath.compile("//Request/View").evaluate(doc, XPathConstants.NODESET)).item(0);
			isReply    = (boolean) xPath.compile("boolean(//Reply/Board)").evaluate(doc, XPathConstants.BOOLEAN);

			if (isReply) {
				
				board = new int[10][10];
				Arrays.stream(board).forEach(a -> Arrays.fill(a, 0));
				
				NodeList positions0 = ((NodeList) xPath.compile("//Empty/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions1 = ((NodeList) xPath.compile("//Aircraft/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions2 = ((NodeList) xPath.compile("//Tanker/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions3 = ((NodeList) xPath.compile("//Destroyer/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions4 = ((NodeList) xPath.compile("//Submarine/Position").evaluate(doc, XPathConstants.NODESET));

				NodeList[] list = {positions0, positions1, positions2, positions3, positions4};
				
				for (int i = 0; i < list.length; i++) {
					for (int j = 0; j < list[i].getLength(); j++) {
				
						String value = list[i].item(j).getTextContent();
						int lin = Character.getNumericValue(value.charAt(0));
						int col = Character.getNumericValue(value.charAt(1));
						
						board[lin][col] = (i == 0) ? ShipType.EMPTY :
										  (i == 1) ? ShipType.TYPE1SHOW :
										  (i == 2) ? ShipType.TYPE2SHOW :
										  (i == 3) ? ShipType.TYPE3SHOW :
										  ShipType.TYPE4SHOW;
					}
				}
			}
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (isReply) return GameView.printBoard(nodePlayer.getTextContent(), nodeView.getTextContent(), board);
		else return "Board" + "," + nodePlayer.getTextContent() + "," + nodeView.getTextContent();
	}
	
	private static String info(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodePlayer = null, nodeInfo = null;
        boolean isReply = false;
        
		try {
			nodePlayer = ((NodeList) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODESET)).item(0);
			nodeInfo   = ((NodeList) xPath.compile("//Reply/Info").evaluate(doc, XPathConstants.NODESET)).item(0);
			isReply    = (boolean) xPath.compile("boolean(//Reply/Info/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
 
		if (isReply) return nodeInfo.getTextContent();
		else return "Info" + "," + nodePlayer.getTextContent();
	}
}
