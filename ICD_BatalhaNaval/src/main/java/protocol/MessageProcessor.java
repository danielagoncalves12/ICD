package protocol;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import javax.imageio.ImageIO;
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

	private static String gameProtocolXSD = "src\\main\\java\\protocol\\GameMessageValidate.xsd";
	/**
	 * Processa uma mensagem de formato xml
	 * @param message
	 * @return
	 */
	public static String process(String message) {

		Document doc  = XMLUtils.stringToDocument(message);
		Node protocol = doc.getElementsByTagName("Protocol").item(0);
		String method = protocol.getFirstChild().getNodeName();

		// Valida��o
		try {		
			if (XMLUtils.validate(message, gameProtocolXSD)) {
				switch(method) {
				
				case "GetBoard"  : return board(doc);
				case "Play"      : return play(doc);	
				case "Login"     : return session(doc);
				case "Upload"    : return upload(doc);
				case "FindGame"  : return find(doc);
				case "GetHonorBoard": return honor(doc);
				case "GetProfileInfo": return profile(doc);
				case "GetPlayers": return players(doc);
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Messagem invalida! ->" + message);
		return null;
	}
	
	private static String profile(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
		Node nodeUsername = null;
		Node nodeName = null, nodePicture = null, nodeWins = null, nodeColor = null, nodeDate = null; boolean isReply = false;
        
		try {
			// Request
			nodeUsername = (Node) xPath.compile("//Request/Username").evaluate(doc, XPathConstants.NODE);
			
			// Response
			nodeName    = (Node) xPath.compile("//Response/Name").evaluate(doc, XPathConstants.NODE);
			nodeColor   = (Node) xPath.compile("//Response/Color").evaluate(doc, XPathConstants.NODE);
			nodePicture = (Node) xPath.compile("//Response/Picture").evaluate(doc, XPathConstants.NODE);
			nodeWins    = (Node) xPath.compile("//Response/WinsNum").evaluate(doc, XPathConstants.NODE);
			nodeDate    = (Node) xPath.compile("//Response/Date").evaluate(doc, XPathConstants.NODE);
			
			isReply    = (boolean) xPath.compile("boolean(//Response/Name/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		if (isReply) return nodeName.getTextContent() + "," + nodeColor.getTextContent() + "," + nodePicture.getTextContent() + "," + 
		nodeWins.getTextContent() + "," + nodeDate.getTextContent();
		else return "ProfileInfo," + nodeUsername.getTextContent();
	}
	
	private static String honor(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
		NodeList nodeName = null, nodePicture = null, nodeWins = null; double isReply = 0;          
        
		try {
			nodeName    = (NodeList) xPath.compile("//Response/Player/Name").evaluate(doc, XPathConstants.NODESET);
			nodePicture = (NodeList) xPath.compile("//Response/Player/Picture").evaluate(doc, XPathConstants.NODESET);
			nodeWins    = (NodeList) xPath.compile("//Response/Player/WinsNumber").evaluate(doc, XPathConstants.NODESET);

			isReply    = (double) xPath.compile("count(//Response/*/*[text()])").evaluate(doc, XPathConstants.NUMBER);
		} catch (XPathExpressionException e) { e.printStackTrace(); }
		
		String[][] players = new String[10][3];
		
		for (int i = 0; i < nodeName.getLength(); i++) {
			
			players[i][0] = nodeName.item(i).getTextContent();
			players[i][1] = nodePicture.item(i).getTextContent();
			players[i][2] = nodeWins.item(i).getTextContent();
		}

		if (isReply > 0) return Arrays.deepToString(players);
		else return "HonorBoard";
	}
	
	private static String upload(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeNickname = null, nodeContentType = null, nodeValue = null;
        Node nodeResult = null; boolean isReply = false;          
        
		try {
			nodeNickname    = (Node) xPath.compile("//Request/Username").evaluate(doc, XPathConstants.NODE);
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
        Node nodeView = null, nodePlayer = null;
        int[][] board = null;
        boolean isReply = false;
        
		try {
			nodePlayer = ((Node) xPath.compile("//Request/Player").evaluate(doc, XPathConstants.NODE));
			nodeView   = ((Node) xPath.compile("//Request/View").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Board)").evaluate(doc, XPathConstants.BOOLEAN);

			if (isReply) {

				board = new int[10][10];
				Arrays.stream(board).forEach(a -> Arrays.fill(a, 0));

				NodeList positions0 = ((NodeList) xPath.compile("//Empty/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions1 = ((NodeList) xPath.compile("//Aircraft/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions2 = ((NodeList) xPath.compile("//Tanker/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions3 = ((NodeList) xPath.compile("//Destroyer/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions4 = ((NodeList) xPath.compile("//Submarine/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions5 = ((NodeList) xPath.compile("//ShootShip/Position").evaluate(doc, XPathConstants.NODESET));
				NodeList positions6 = ((NodeList) xPath.compile("//ShootEmpty/Position").evaluate(doc, XPathConstants.NODESET));
				
				NodeList[] list = {positions0, positions1, positions2, positions3, positions4, positions5, positions6};
				
				for (int i = 0; i < list.length; i++) {
					for (int j = 0; j < list[i].getLength(); j++) {
				
						NodeList position = list[i].item(j).getChildNodes();
						int lin = Integer.valueOf(position.item(0).getTextContent());
						int col = Integer.valueOf(position.item(1).getTextContent());

						board[lin][col] = (i == 0) ? ShipType.EMPTY :
										  (i == 1) ? ShipType.TYPE1SHOW :
										  (i == 2) ? ShipType.TYPE2SHOW :
										  (i == 3) ? ShipType.TYPE3SHOW :
										  (i == 4) ? ShipType.TYPE4SHOW :
										  (i == 5) ? ShipType.TYPESHOOT :
										  ShipType.TYPESHOOTEMPTY;										  
					}
				}
			}
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		if (isReply) return GameView.printBoard(nodePlayer.getTextContent(), nodeView.getTextContent(), board);
		else return "GetBoard" + "," + nodePlayer.getTextContent() + "," + nodeView.getTextContent();
	}
	
	private static String players(Document doc) {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        NodeList nodePlayers = null;
        boolean isReply = false;
        
		try {		
			nodePlayers   = ((NodeList) xPath.compile("//Players/Username").evaluate(doc, XPathConstants.NODESET));
			isReply    = (boolean) xPath.compile("boolean(//Players/Username/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		String[] players = new String[nodePlayers.getLength()];
		
		if (isReply) {	
			for (int i = 0; i < nodePlayers.getLength(); i++) {			
				String player = nodePlayers.item(i).getTextContent();
				players[i] = player;
			}
		}
		
		if (isReply) return Arrays.toString(players);
		else return "GetPlayers";
	}
	
	private static String find(Document doc) throws DOMException, SAXException {
		
		XPath xPath  = XPathFactory.newInstance().newXPath();
        Node nodeNickname = null, nodeResult = null;
        boolean isReply = false;
        
		try {	
			nodeNickname = ((Node) xPath.compile("//Request/Username").evaluate(doc, XPathConstants.NODE));		
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
        nodeColor = null, nodeDate = null, nodePicture = null, nodeResult = null;
        boolean isReply = false;
        
		try {
			nodeRegister = ((Node) xPath.compile("//Login/@register").evaluate(doc, XPathConstants.NODE));			
			nodeNickname = ((Node) xPath.compile("//Request/Username").evaluate(doc, XPathConstants.NODE));
			nodeName     = ((Node) xPath.compile("//Request/Name").evaluate(doc, XPathConstants.NODE));
			nodePassword = ((Node) xPath.compile("//Request/Password").evaluate(doc, XPathConstants.NODE));
			nodeColor	 = ((Node) xPath.compile("//Request/Color").evaluate(doc, XPathConstants.NODE));
			nodeDate 	 = ((Node) xPath.compile("//Request/Date").evaluate(doc, XPathConstants.NODE));
			nodePicture  = ((Node) xPath.compile("//Request/Picture").evaluate(doc, XPathConstants.NODE));
			
			nodeResult   = ((Node) xPath.compile("//Response/Result").evaluate(doc, XPathConstants.NODE));
			isReply    = (boolean) xPath.compile("boolean(//Response/Result/text())").evaluate(doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) { e.printStackTrace(); }

		String nickname = nodeNickname.getTextContent();
		String name     = nodeName.getTextContent(); 
		String password = nodePassword.getTextContent();
		String color	= nodeColor.getTextContent();
		String date		= nodeDate.getTextContent();
		String picture  = nodePicture.getTextContent();
		
		// Caso o utilizador nao tenha introduzido uma foto durante a inscricao, � atribuido uma foto pre-definida	
		if (nodeRegister.getNodeValue().equals("true")) { 
			if (picture.equals("")) {
	
				BufferedImage bImage = ImageIO.read(new File("src/main/webapp/pictures/default.png"));
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ImageIO.write(bImage, "png", bos );
		        byte[] data = bos.toByteArray();
				
		        picture = Base64.getEncoder().encodeToString(data);
			}				
		}		
		
		if (isReply) return nodeResult.getTextContent();
		else {
			if (nodeRegister.getNodeValue().equals("true"))
				return "Register," + nickname + "," + name + "," + password + "," + color + "," + date + "," + picture;
			else
				return "Login," + nickname + "," + password;
		}
	}
}
