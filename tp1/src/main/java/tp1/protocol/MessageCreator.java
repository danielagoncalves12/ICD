package tp1.protocol;

import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageCreator {

	public static String messageBoard(String player, String view) throws ParserConfigurationException {
		
		return messageBoard(player, view, null);
	}
	
	public static String messageBoard(String player, String view, HashMap<String, List<List<Integer>>> dic) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("Board");
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
        Element elementReply = document.createElement("Reply");
        
        // Elemento Result
        if (dic != null) {
 
        	// Elemento Board
            Element elementBoard = document.createElement("Board");

            // Tipos de navios e respetivas posições
            int index = 0;
            for (String key : dic.keySet()) {
            	
            	Element shipType = document.createElement((String) dic.keySet().toArray()[index++]);

            	for (List<Integer> pos : dic.get(key)) {

                	Element position = document.createElement("Position");
                	String tuple = String.valueOf(pos.get(0)) + String.valueOf(pos.get(1));

                	position.appendChild(document.createTextNode(tuple));
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
        
        // Elemento Method (Position, Board, etc)
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
        Element elementReply = document.createElement("Reply");
        
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
        Element elementMethod = document.createElement("Info");
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
        Element elementReply = document.createElement("Reply");
        
        // Elemento Info
        if (!info.equals("")) {
        	Element elementInfo = document.createElement("Info");
        	elementInfo.appendChild(document.createTextNode(info));
        	elementReply.appendChild(elementInfo);
        }
        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}
}
