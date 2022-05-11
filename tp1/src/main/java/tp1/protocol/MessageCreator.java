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
		
		return messageBoard(player, view, null, false);
	}
	
	public static String messageBoard(String player, String view, HashMap<String, List<List<Integer>>> dic, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("Board");
        elementMethod.setAttribute("Read", ack ? "true" : "false");
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
		
		return messagePlay(player, position, "", false);
	}

	public static String messagePlay(String player, String argument, String result, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("Play");
        elementMethod.setAttribute("Read", ack ? "true" : "false");
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
        if (!argument.equals("")) {
	    	Element elementArgument = document.createElement("Position");
	    	elementArgument.appendChild(document.createTextNode(argument));
	    	elementRequest.appendChild(elementArgument);
        }
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Reply");
        
        // Elemento Result
        if (!result.equals("")) elementReply.appendChild(document.createTextNode(result));

        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}
	
	public static String messageInfo(String player, String info) throws ParserConfigurationException {
		
		return messageInfo(player, info, "", false);
	}

	public static String messageInfo(String player, String info, String result, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("Protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("Info");
        elementMethod.setAttribute("Read", ack ? "true" : "false");
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
        if (!info.equals("")) {
	    	Element elementArgument = document.createElement("Info");
	    	elementArgument.appendChild(document.createTextNode(info));
	    	elementRequest.appendChild(elementArgument);
        }
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("Reply");
        
        // Elemento Result
        if (!result.equals("")) elementReply.appendChild(document.createTextNode(result));

        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}
	
	
	public static String messageBoardPositions(HashMap<String, List<List<Integer>>> dic) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        
        return XMLUtils.documentToString(document);
	}
}
