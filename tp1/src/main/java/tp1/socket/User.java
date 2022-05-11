package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import tp1.battleship.GameView;
import tp1.protocol.MessageCreator;
import tp1.protocol.MessageProcessor;
import tp1.protocol.XMLUtils;

/**
 * @author Daniela Gonçalves A48579 42D
 */

public class User {

	private final static String HOST = "localhost"; // Endereço do Servidor
    private final static int    PORT = 1001;        // Porto onde o Servidor aceita conexões
    
    public static void main(String[] args) throws ParserConfigurationException {
        
        Socket     socket = null; 
        BufferedReader is = null;
        PrintWriter    os = null;
        Scanner      scan = null;
        
        String playerNum = "0";
        
        try {
            socket = new Socket(HOST, PORT); 										     // Ligação ao Socket servidor
            is     = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para a leitura do socket
            os     = new PrintWriter(socket.getOutputStream(), true); 					 // Stream para a escrita no socket 
            scan   = new Scanner(System.in);											 // Scanner para a introdução de dados
         
            // Login/Registo (Nickname) 
            String read = (String) is.readLine();
			System.out.println(read.replaceAll("\7", "\n"));
			String nickname = scan.nextLine();				
			os.println(nickname);

			// Login/Registo (Password)
			read = (String) is.readLine();
			System.out.println(read.replaceAll("\7", "\n"));
			String password = scan.nextLine();				
			os.println(password);
			
			// Estado da sessão
			String state = is.readLine();

			System.out.println("Sessao: " + state + "\n");    	
			if (state.equals("Palavra-passe incorreta!")) return;		
			System.out.println("A espera da conexao de outro jogador...");
        
            // Inicio do jogo    
			playerNum = User.sendRequestInfo(os, is, "0");
            System.out.println("Es o jogador numero " + playerNum + "!!");
            System.out.print(User.sendRequestBoard(os, is, playerNum, "true"));  // Receber o próprio tabuleiro
            System.out.print(User.sendRequestBoard(os, is, playerNum, "false")); // Receber tabuleiro do adversário
               
	        while (true) {
	        	// Enviar Request com a jogada escolhida
	        	System.out.println(User.sendRequestInfo(os, is, playerNum));
				System.out.println(User.sendRequestPlay(os, is, playerNum, scan.nextLine()));
				
				// Enviar Request a pedir o tabuleiro do adversário atualizado
				System.out.print(User.sendRequestBoard(os, is, playerNum, "false"));     	
			}
        } 
        catch (IOException e) {
           System.err.println(e.getMessage());   
        }
        finally {
            try {
            	scan.close();
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null) socket.close();
                
                System.exit(0);
            }
            catch (IOException e) { 
            	System.err.println("Erro I/O -> " + e.getMessage());   
            }
        }
    }	

    /**
     * Envia uma Request ao servidor, a pedir o tabuleiro e recebe a resposta do mesmo. O argumento player
     * indica a qual jogador o tabuleiro se destina. Quando o argumento view é igual a true, indica que o
     * utilizador quer receber o próprio tabuleiro, com todos os navios visíveis, caso seja falso, indica
     * que quer receber o tabuleiro do adversário, com os navios que ainda não foram encontrados ocultos.
     */
    public static String sendRequestBoard(PrintWriter os, BufferedReader is, String player, String view) {
		
    	String board = null;
    	
    	try {
			os.println(MessageCreator.messageBoard(player, view));
			String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
			board = MessageProcessor.process(reply); 
			
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		} 
		return board;
    }
    
    /**
     * Envia uma Request ao servidor, a pedir que uma jogada (posição do tiro) seja aplicada no jogo e 
     * recebe uma resposta do mesmo, que contem o resultado da jogada. O argumento player indica qual o 
     * jogador a enviar a jogada. O argumento position contem a posição do tiro escolhida pelo jogador.
     */
    public static String sendRequestPlay(PrintWriter os, BufferedReader is, String player, String position) throws ParserConfigurationException, IOException {	
		os.println(MessageCreator.messagePlay(player, position));	
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		return MessageProcessor.process(reply);
    }
    
    /**
     * Envia uma Request ao servidor, a pedir por informação, que varia de acordo com o estado atual do
     * jogo, por exemplo, uma resposta a este tipo de pedido poderá ser um aviso a um jogador que é a sua 
     * vez de jogar, ou então, avisar os jogadores que o jogo terminou porque um dos jogadores ganhou.
     */
    public static String sendRequestInfo(PrintWriter os, BufferedReader is, String player) throws ParserConfigurationException, IOException {		
    	os.println(MessageCreator.messageInfo(player, "game")); 
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");
		return MessageProcessor.process(reply);
    }
}