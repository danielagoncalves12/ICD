package tp1.protocol;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageCreator {

	public static String message(String method, String player, String argument, String result, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("protocol");
        document.appendChild(elementRoot);
        
        // Elemento Method (Position, Board, etc)
        Element elementMethod = document.createElement("method");
        elementMethod.setAttribute("type", method);
        elementMethod.setAttribute("read", ack ? "true" : "false");
        elementRoot.appendChild(elementMethod); 

        // Elemento Request
        Element elementRequest = document.createElement("request");
        
        // Elemento Player
        if (!player.equals("")) {
	    	Element elementPlayer = document.createElement("player");
	    	elementPlayer.appendChild(document.createTextNode(player));
	    	elementRequest.appendChild(elementPlayer);
        }
		
        // Elemento Argument
        if (!argument.equals("")) {
	    	Element elementArgument = document.createElement("argument");
	    	elementArgument.appendChild(document.createTextNode(argument));
	    	elementRequest.appendChild(elementArgument);
        }
        elementMethod.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("reply");
        
        // Elemento Result
        if (!result.equals("")) {
        	Element elementResult = document.createElement("result");
        	elementResult.appendChild(document.createTextNode(result));
        	elementReply.appendChild(elementResult);
        }
        elementMethod.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}

	public static String message(String method, String player, String position) throws ParserConfigurationException {
		
		return message(method, player, position, "", false);
	}
}
