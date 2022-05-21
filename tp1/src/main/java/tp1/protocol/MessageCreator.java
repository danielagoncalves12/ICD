package tp1.protocol;

import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageCreator {

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
    	Element elementNickname = document.createElement("Nickname");
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
	
	public static String messageBoard(String player, String view) throws ParserConfigurationException {
		
		return messageBoard(player, view, "", "", null);
	}
	
	public static String messageBoard(String player, String view, String points1, String points2, HashMap<String, List<List<Integer>>> dic) throws ParserConfigurationException {
		
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
        
        // Elemento Points
        if (!points1.equals("") && !points2.equals("")) {
        	
        	Element elementPoints = document.createElement("Points");
        	
        	Element elementPlayer1 = document.createElement("Player1");
        	elementPlayer1.setTextContent(points1);
        	elementPoints.appendChild(elementPlayer1);
        	
        	Element elementPlayer2 = document.createElement("Player2");
        	elementPlayer2.setTextContent(points2);
        	elementPoints.appendChild(elementPlayer2);
        	
        	elementReply.appendChild(elementPoints);
        }
        
        // Elemento Board
        if (dic != null) {
 
        	// Elemento Board
            Element elementBoard = document.createElement("Board");

            // Tipos de navios e respetivas posições
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
	
	public static String messagePlay(String player, String position) throws ParserConfigurationException {
		
		return messagePlay(player, position, "");
	}

	public static String messagePlay(String player, String position, String result) throws ParserConfigurationException {
		
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
        
        // Elemento Player
        if (!player.equals("")) {
	    	Element elementPlayer = document.createElement("Player");
	    	elementPlayer.appendChild(document.createTextNode(player));
	    	elementRequest.appendChild(elementPlayer);
        }
		
        // Elemento Argument
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
	
	public static String messageInfo(String player) throws ParserConfigurationException {
		
		return messageInfo(player, "");
	}

	public static String messageInfo(String player, String info) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("GetInfo");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("Request");
        
        // Elemento Player
        if (!player.equals("")) {
	    	Element elementPlayer = document.createElement("Player");
	    	elementPlayer.appendChild(document.createTextNode(player));
	    	elementRequest.appendChild(elementPlayer);
        }
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Response");
        
        // Elemento Info
        if (!info.equals("")) {
        	Element elementInfo = document.createElement("Info");
        	elementInfo.appendChild(document.createTextNode(info));
        	elementReply.appendChild(elementInfo);
        }
        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}

	public static String messageSession(String nickname, String name, String password, String picture, boolean register) throws ParserConfigurationException {
		
		return messageSession(nickname, name, password, picture, register, "");
	}
	
	public static String messageSession(String nickname, String name, String password, String picture, boolean register, String result) throws ParserConfigurationException {
	
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
		
        // Elemento Sessão
        Element elementSession = document.createElement("Login");
        elementSession.setAttribute("register", register ? "true" : "false");
        elementRoot.appendChild(elementSession);
        
        // Elemento Request
        Element elementRequest = document.createElement("Request");
        elementSession.appendChild(elementRequest);
        
        // Elemento Nickname
        Element elementNickname = document.createElement("Nickname");
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
}
