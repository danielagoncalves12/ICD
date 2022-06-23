package protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageCreator {

	public static String messageGetPlayers() throws ParserConfigurationException {		
		return messageGetPlayers(null);
	}
	
	public static String messageGetPlayers(ArrayList<String> players) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento GetPlayers
        Element elementMethod = document.createElement("GetPlayers");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");     
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");    
        Element elementPlayers = document.createElement("Players");
        
        // Elemento Result
        if (players != null) {
        	for (String username : players) {
        	
	        	Element elementName = document.createElement("Username");
	        	elementName.setTextContent(username);
	        	elementPlayers.appendChild(elementName);
        	}
        }     
        elementReply.appendChild(elementPlayers);
        elementMethod.appendChild(elementReply);
        
        return XMLUtils.documentToString(document);
	}
	
	public static String messageGetProfileInfo (String username) throws ParserConfigurationException {		
		return messageGetProfileInfo(username, null);
	}
	
	public static String messageGetProfileInfo(String username, HashMap<String, String> profileInfo) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (GetProfileInfo)
        Element elementMethod = document.createElement("GetProfileInfo");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
            
        // Elemento Username
    	Element elementUsername = document.createElement("Username");
    	elementUsername.appendChild(document.createTextNode(username));
    	elementRequest.appendChild(elementUsername);
        
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Result
        if (profileInfo != null) {    	
        	for (String key : profileInfo.keySet()) {
        		
        		Element element = document.createElement(key);
        		element.setTextContent(profileInfo.get(key));
        		elementReply.appendChild(element);    		
        	}
        }     
        elementMethod.appendChild(elementReply);
        
        return XMLUtils.documentToString(document);
	}
	
	public static String messageHonorBoard() throws ParserConfigurationException {
		
		return messageHonorBoard(null, null, null);
	}
	
	public static String messageHonorBoard(String[] names, String[] pictures, String[] winsNum) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position)
        Element elementMethod = document.createElement("GetHonorBoard");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Result
        if (names != null) {
        	
        	for (int i = 0; i < names.length; i++) {
        		
        		Element elementPlayer = document.createElement("Player");
        		
        		Element elementName = document.createElement("Name");
        		elementName.setTextContent(names[i]);
        		elementPlayer.appendChild(elementName);
        		
        		Element elementPicture = document.createElement("Picture");
        		elementPicture.setTextContent(pictures[i]);
        		elementPlayer.appendChild(elementPicture);
        		
        		Element elementWinNum = document.createElement("WinsNumber");
        		elementWinNum.setTextContent(winsNum[i]);
        		elementPlayer.appendChild(elementWinNum);
        		
            	elementReply.appendChild(elementPlayer);
        	}
        }
        elementMethod.appendChild(elementReply);
        
        return XMLUtils.documentToString(document);
	}
	
	public static String messageUpload(String contentType, String nickname, String value) throws ParserConfigurationException {
		
		return messageUpload(contentType, nickname, value, "");
	}
	
	public static String messageUpload(String contentType, String nickname, String value, String result) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position)
        Element elementMethod = document.createElement("Upload");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
           
        // Elemento ContentType
    	Element elementContentType = document.createElement("ContentType");
    	elementContentType.appendChild(document.createTextNode(contentType));
    	elementRequest.appendChild(elementContentType);
               
        // Elemento Nickname
    	Element elementNickname = document.createElement("Username");
    	elementNickname.appendChild(document.createTextNode(nickname));
    	elementRequest.appendChild(elementNickname);
        
        // Elemento Value
    	Element elementValue = document.createElement("Value");
    	elementValue.appendChild(document.createTextNode(value));
    	elementRequest.appendChild(elementValue);
        
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Result
        if (!result.equals("")) {
        	Element elementResult = document.createElement("Result");
        	elementResult.appendChild(document.createTextNode(result));
        	elementReply.appendChild(elementResult);
        }
        elementMethod.appendChild(elementReply);
        
        return XMLUtils.documentToString(document);
	}
	
	public static String messageBoard(String gameID, String player, String view) throws ParserConfigurationException {
		
		return messageBoard(gameID, player, view, null);
	}
	
	public static String messageBoard(String gameID, String player, String view, HashMap<String, List<List<Integer>>> dic) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("GetBoard");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
        
        // Elemento GameID
        if (!gameID.equals("")) {
	    	Element elementGameID = document.createElement("GameID");
	    	elementGameID.appendChild(document.createTextNode(gameID));
	    	elementRequest.appendChild(elementGameID);
        }
        
        // Elemento Player
        if (!player.equals("")) {
	    	Element elementPlayer = document.createElement("Player");
	    	elementPlayer.appendChild(document.createTextNode(player));
	    	elementRequest.appendChild(elementPlayer);
        }
		
        // Elemento Argument
        if (!view.equals("")) {
	    	Element elementArgument = document.createElement("View");
	    	elementArgument.appendChild(document.createTextNode(view));
	    	elementRequest.appendChild(elementArgument);
        }
         
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Board
        if (dic != null) {
 
        	// Elemento Board
            Element elementBoard = document.createElement("Board");

            // Tipos de navios e respetivas posi��es
            int index = 0;
            for (String key : dic.keySet()) {
            	
            	Element shipType = document.createElement((String) dic.keySet().toArray()[index++]);

            	for (List<Integer> pos : dic.get(key)) {

                	Element position = document.createElement("Position");
                	Element line   = document.createElement("Line");
                	line.appendChild(document.createTextNode(String.valueOf(pos.get(0))));
                	position.appendChild(line);
                	
                	Element column = document.createElement("Column"); 
                	column.appendChild(document.createTextNode(String.valueOf(pos.get(1))));
                	position.appendChild(column);

                	shipType.appendChild(position);
                }
            	elementBoard.appendChild(shipType);
            }
            elementReply.appendChild(elementBoard);
        } 
        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);		
	}
	
	public static String messagePlay(String gameID, String player, String position) throws ParserConfigurationException {
		
		return messagePlay(gameID, player, position, "");
	}

	public static String messagePlay(String gameID, String player, String position, String result) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position)
        Element elementMethod = document.createElement("Play");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
        
     // Elemento GameID
        if (!gameID.equals("")) {
	    	Element elementGameID = document.createElement("GameID");
	    	elementGameID.appendChild(document.createTextNode(gameID));
	    	elementRequest.appendChild(elementGameID);
        }
        
        // Elemento Player
        if (!player.equals("")) {
	    	Element elementPlayer = document.createElement("Player");
	    	elementPlayer.appendChild(document.createTextNode(player));
	    	elementRequest.appendChild(elementPlayer);
        }
		
        // Elemento Argument
        if (position == null) position = "";
        if (!position.equals("")) {
	    	Element elementArgument = document.createElement("Position");
	    	elementArgument.appendChild(document.createTextNode(position));
	    	elementRequest.appendChild(elementArgument);
        }
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Result
        if (!result.equals("")) {
        	Element elementResult = document.createElement("Result");
        	elementResult.appendChild(document.createTextNode(result));
        	elementReply.appendChild(elementResult);
        }
        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}

	public static String messageSession(String nickname, String name, String password, String color, String date, String picture, boolean register) throws ParserConfigurationException {
		
		return messageSession(nickname, name, password, color, date, picture, register, "");
	}
	
	public static String messageSession(String nickname, String name, String password, String color, String date, String picture, boolean register, String result) throws ParserConfigurationException {
	
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
		
        // Elemento Sess�o
        Element elementSession = document.createElement("Login");
        elementSession.setAttribute("register", register ? "true" : "false");
        elementRoot.appendChild(elementSession);
        
        // Elemento Request
        Element elementRequest = document.createElement("Request");
        elementSession.appendChild(elementRequest);
        
        // Elemento Nickname
        Element elementNickname = document.createElement("Username");
        elementNickname.setTextContent(nickname);
        elementRequest.appendChild(elementNickname);
        
        // Elemento Name
        Element elementName = document.createElement("Name");
        elementName.setTextContent(name);
        elementRequest.appendChild(elementName);
        
        // Element Password
        Element elementPassword = document.createElement("Password");
        elementPassword.setTextContent(password);
        elementRequest.appendChild(elementPassword);
        
        // Element Color
    	Element elementColor = document.createElement("Color");
    	elementColor.setTextContent(color);
    	elementRequest.appendChild(elementColor);
        
        // Element Date
    	Element elementDate = document.createElement("Date");
    	elementDate.setTextContent(date);
    	elementRequest.appendChild(elementDate);
        
        // Element Picture
    	Element elementPicture = document.createElement("Picture");
    	elementPicture.setTextContent(picture);
    	elementRequest.appendChild(elementPicture);
        
        // Element Reply
        Element elementReply = document.createElement("Response");
        elementSession.appendChild(elementReply);
        
        // Element Result
        if (!result.equals("")) {
        	Element elementResult = document.createElement("Result");
        	elementResult.setTextContent(result);
        	elementReply.appendChild(elementResult);
        }
        return XMLUtils.documentToString(document);
	}
	
	public static String messageFind(String username) throws ParserConfigurationException {
		
		return messageFind(username, "");
	}
	
	public static String messageFind(String username, String result) throws ParserConfigurationException {
	
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
		
        // Elemento Sess�o
        Element elementSession = document.createElement("FindGame");
        
        // Elemento Request
        Element elementRequest = document.createElement("Request");
        elementSession.appendChild(elementRequest);
        
        // Elemento Nickname
        Element elementNickname = document.createElement("Username");
        elementNickname.setTextContent(username);
        elementRequest.appendChild(elementNickname);
        
        // Element Reply
        Element elementReply = document.createElement("Response");
        elementSession.appendChild(elementReply);
        
        // Element Result
        if (!result.equals("")) {
        	Element elementResult = document.createElement("Result");
        	elementResult.setTextContent(result);
        	elementReply.appendChild(elementResult);
        }
        elementRoot.appendChild(elementSession);
        
        return XMLUtils.documentToString(document);
	}
}
