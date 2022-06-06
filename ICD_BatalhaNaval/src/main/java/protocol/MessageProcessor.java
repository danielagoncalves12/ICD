package protocol;

import java.io.IOException;
import java.util.Arrays;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import battleship.GameView;
import battleship.ShipType;
import session.Profile;
import session.Session;

public class MessageProcessor {

	private static String gameProtocolXSD = "src\\main\\webapp\\xml\\GameMessageValidate.xsd";
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
			if (XMLUtils.validate(message, gameProtocolXSD)) {
				switch(method) {
				
				case "GetBoard": return board(doc);
				case "GetInfo" : return info(doc);
				case "Play"    : return play(doc);	
				case "Login"   : return session(doc);
				case "Upload"  : return upload(doc);
				case "FindGame": return find(doc);
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Messagem inválida! ->" + message);
		return null;
	}
	
	private static String upload(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeNickname = null, nodeContentType = null, nodeValue = null;
        Node nodeResult = null; boolean isReply = false;          
        
		try {
			nodeNickname    = (Node) xPath.compile("//Request/Nickname").evaluate(doc, XPathConstants.NODE);
			nodeContentType = (Node) xPath.compile("//Request/ContentType").evaluate(doc, XPathConstants.NODE);
			nodeValue 		= (Node) xPath.compile("//Request/Value").evaluate(doc, XPathConstants.NODE);
			nodeResult      = (Node) xPath.compile("//Response/Result").evaluate(doc, XPathConstants.NODE);
			
			isReply    = (boolean) xPath.compile("boolean(//Response/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (isReply) return nodeResult.getTextContent();
		else return "Upload" + "," + nodeContentType.getTextContent() + "," + nodeNickname.getTextContent() + "," + nodeValue.getTextContent();
	}
	
	private static String play(Document doc) throws DOMException, SAXException, IOException {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodePlayer = null, nodeChoice = null, nodeResult = null;
        boolean isReply = false;          
        
		try {
			nodePlayer = (Node) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODE);
			nodeChoice = ((Node) xPath.compile("//Request/Position").evaluate(doc, XPathConstants.NODE));
			nodeResult = ((Node) xPath.compile("//Response/Result").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (isReply) return nodeResult.getTextContent();
		else return "Play" + "," + nodePlayer.getTextContent() + "," + nodeChoice.getTextContent();
	}
	
	private static String board(Document doc) throws NumberFormatException, DOMException, SAXException, IOException {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeView = null, nodePlayer = null, nodePoints1 = null, nodePoints2 = null;
        int[][] board = null;
        boolean isReply = false;
        
		try {
			nodePlayer = ((Node) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODE));
			nodeView   = ((Node) xPath.compile("//Request/View").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Board)").evaluate(doc, XPathConstants.BOOLEAN);

			if (isReply) {
				
				nodePoints1 = ((Node) xPath.compile("//Points/Player1").evaluate(doc, XPathConstants.NODE));
				nodePoints2 = ((Node) xPath.compile("//Points/Player2").evaluate(doc, XPathConstants.NODE));
				
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
				
						NodeList position = list[i].item(j).getChildNodes();
						int lin = Integer.valueOf(position.item(0).getTextContent());
						int col = Integer.valueOf(position.item(1).getTextContent());

						board[lin][col] = (i == 0) ? ShipType.EMPTY :
										  (i == 1) ? ShipType.TYPE1SHOW :
										  (i == 2) ? ShipType.TYPE2SHOW :
										  (i == 3) ? ShipType.TYPE3SHOW :
										  ShipType.TYPE4SHOW;
					}
				}
			}
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		if (isReply) return GameView.printBoard(nodePlayer.getTextContent(), nodeView.getTextContent(), nodePoints1.getTextContent(), nodePoints2.getTextContent(), board);
		else return "GetBoard" + "," + nodePlayer.getTextContent() + "," + nodeView.getTextContent();
	}
	
	private static String info(Document doc) throws DOMException, SAXException, IOException {

		XPath xPath  = XPathFactory.newInstance().newXPath();
	    Node nodePlayer = null, nodeInfo = null;
	    boolean isReply = false;
	    
		try {
			nodePlayer = ((Node) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODE));
			nodeInfo   = ((Node) xPath.compile("//Response/Info").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Info/text())").evaluate(doc, XPathConstants.BOOLEAN);
			} catch (XPathExpressionException e) { e.printStackTrace(); }
	 
			if (isReply) return nodeInfo.getTextContent();
			else return "Info," + nodePlayer.getTextContent();
	}
	
	private static String find(Document doc) throws DOMException, SAXException {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeNickname = null, nodeResult = null;
        boolean isReply = false;
        
		try {	
			nodeNickname = ((Node) xPath.compile("//Request/Nickname").evaluate(doc, XPathConstants.NODE));		
			nodeResult   = ((Node) xPath.compile("//Response/Result").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		String username = nodeNickname.getTextContent();
		if (isReply) return nodeResult.getTextContent();
		else return "FindGame," + username;
	}
	
	private static String session(Document doc) throws DOMException, SAXException, IOException {

		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeRegister = null, nodeNickname = null, nodeName = null, nodePassword = null,
        nodePicture = null, nodeResult = null;
        boolean isReply = false;
        
		try {
			nodeRegister = ((Node) xPath.compile("//Login/@register").evaluate(doc, XPathConstants.NODE));			
			nodeNickname = ((Node) xPath.compile("//Request/Nickname").evaluate(doc, XPathConstants.NODE));
			nodeName     = ((Node) xPath.compile("//Request/Name").evaluate(doc, XPathConstants.NODE));
			nodePassword = ((Node) xPath.compile("//Request/Password").evaluate(doc, XPathConstants.NODE));
			nodePicture  = ((Node) xPath.compile("//Request/Picture").evaluate(doc, XPathConstants.NODE));
			
			nodeResult   = ((Node) xPath.compile("//Response/Result").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		String nickname = nodeNickname.getTextContent();
		String name     = nodeName.getTextContent(); 
		String password = nodePassword.getTextContent();
		String picture  = nodePicture.getTextContent();
		
		// Caso o utilizador não tenha introduzido uma foto durante a inscrição, é atribuido uma foto pre-definida	
		if (nodeRegister.getNodeValue().equals("true")) { 
			if (picture.equals(""))
				picture = "default/photo.png"; 
		}		
		// Caso o utilizador esteja a iniciar sessão (login), obtem o nome publico e foto de perfil
		else {
			if (!Session.availableNickname(nickname)) {
				name 	= Profile.getName(nodeNickname.getTextContent());
				picture = Profile.getPicture(nodeNickname.getTextContent());
			} else {
				name = "null";
				picture = "null";
			}
		}
		
		if (isReply) return nodeResult.getTextContent() + "," + nickname + "," + name + "," + picture;
		else {
			if (nodeRegister.getNodeValue().equals("true"))
				return "Register," + nickname + "," + name + "," + password + "," + picture;
			else
				return "Login," + nickname + "," + name + "," + password + "," + picture;
		}
	}
}
