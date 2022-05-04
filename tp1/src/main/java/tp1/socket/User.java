package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 */

public class User {

	private final static String HOST = "localhost"; // Endereço do Servidor
    private final static int    PORT = 1001;        // Porto onde o Servidor aceita conexões
    
    public static void main(String[] args) throws ParserConfigurationException {
        
        Socket     socket = null; 
        BufferedReader is = null;
        PrintWriter    os = null;
        
        MessageCreator     msgCreator = null;
        MessageProcessor msgProcessor = null;
        
        String playerNum = "0";
        
        try {
            socket = new Socket(HOST, PORT); 										     // Ligação ao Socket servidor
            is     = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            os     = new PrintWriter(socket.getOutputStream(), true); 
            
            msgCreator   = new MessageCreator();
            msgProcessor = new MessageProcessor(); 
            playerNum = is.readLine();
            System.out.println("Bem-vindo jogador " + playerNum + "!!"); 
            
            // Receber o próprio tabuleiro     
            System.out.println(is.readLine().replaceAll("\7", "\n")); // Mostrar tabuleiro do jogador
            
            // Receber tabuleiro do adversário
            os.println(msgCreator.messageBoard(playerNum));										  // Enviar a Request (Tabuleiro)
            String boardOpponent = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n"); // Receber a Reply  (Tabuleiro)
            System.out.println(msgProcessor.process(boardOpponent));                              // Apresentar o tabuleiro
            
            try (Scanner scan = new Scanner(System.in)) {
		        for(;;) {    
		        	
		        	System.out.println(is.readLine().replaceAll("\7", "\n"));  	  		   // Mensagem de introdução da jogada
		        	
		        	// Enviar a mensagem com a jogada
					String position = scan.nextLine();						     		   // Scan da jogada 
					os.println(msgCreator.messagePosition(playerNum, position));  		   // Enviar a Request (Posicao)
					String reply  = is.readLine().replaceAll("\7", "\n");		  		   // Receber a Reply  (Posicao)
					System.out.println(msgProcessor.process(reply)); 			  		   // Apresentar o resultado			
					
					// Enviar a mensagem a pedir o tabuleiro atualizado
					os.println(msgCreator.messageBoard(playerNum));				  		   // Enviar a Request (Tabuleiro)
					reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n"); // Receber a Reply  (Tabuleiro)
					System.out.println(msgProcessor.process(reply)); 					   // Apresentar o tabuleiro
					
				}
            }
        } 
        catch (IOException e) {
            System.err.println("Erro na ligação -> " + e.getMessage());   
        }
        finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null ) socket.close();
            }
            catch (IOException e) { 
            	System.err.println("Erro I/O -> " + e.getMessage());   
            }
        }

    }	
}