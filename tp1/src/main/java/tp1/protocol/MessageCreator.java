package tp1.protocol;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageCreator {

	public String messagePosition(String player, String choice, String result, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("protocol");
        document.appendChild(elementRoot);
        
        // Elemento Posicao
        Element elementPosition = document.createElement("position");
        elementPosition.setAttribute("read", ack ? "true" : "false");
        elementRoot.appendChild(elementPosition); 

        // Elemento Request
        Element elementRequest = document.createElement("request");
        
        // Elemento Player
        if (!player.equals("")) {
        	Element elementPlayer = document.createElement("player");
        	elementPlayer.appendChild(document.createTextNode(player));
        	elementRequest.appendChild(elementPlayer);
        }
		
        // Elemento Position
        if (!choice.equals("")) {
        	Element elementChoice = document.createElement("choice");
        	elementChoice.appendChild(document.createTextNode(choice));
        	elementRequest.appendChild(elementChoice);
        }
        elementPosition.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("reply");
        
        // Elemento Resultado
        if (!result.equals("")) {
        	Element elementResult = document.createElement("result");
        	elementResult.appendChild(document.createTextNode(result));
        	elementReply.appendChild(elementResult);
        }
        elementPosition.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}

	public String messagePosition(String player, String position) throws ParserConfigurationException {
		
		return messagePosition(player, position, "", false);
	}
	
	public String messageBoard(String player, String result, boolean ack) throws ParserConfigurationException {
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();	 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Elemento Protocol
        Element elementRoot = document.createElement("protocol");
        document.appendChild(elementRoot);
        
        // Elemento Tabuleiro
        Element elementBoard = document.createElement("board");
        elementBoard.setAttribute("read", ack ? "true" : "false");
        elementRoot.appendChild(elementBoard);

        // Elemento Request
        Element elementRequest = document.createElement("request");
        
        // Elemento Player
        if (!player.equals("")) {
        	Element elementPlayer = document.createElement("player");
        	elementPlayer.appendChild(document.createTextNode(player));
        	elementRequest.appendChild(elementPlayer);
        }

        elementBoard.appendChild(elementRequest);
        
        // Elemento Reply
        Element elementReply = document.createElement("reply");
        
        // Elemento Tabuleiro
        if (!result.equals("")) {
        	Element elementResult = document.createElement("board");
        	elementResult.appendChild(document.createTextNode(result));
        	elementReply.appendChild(elementResult);
        }
        elementBoard.appendChild(elementReply);

        return XMLUtils.documentToString(document);
	}
	
	public String messageBoard(String player) throws ParserConfigurationException {
		
		return messageBoard(player, "", false);
	}
}
