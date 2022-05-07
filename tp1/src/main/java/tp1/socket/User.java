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
            is     = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            os     = new PrintWriter(socket.getOutputStream(), true); 
            scan   = new Scanner(System.in);
            
            // Registo (Nickname)       
            Thread reader = new ReaderThread(is);
			reader.start();		
			String nickname = scan.nextLine();				
			os.println(nickname);

			reader = new ReaderThread(is);
			reader.start();		
			String password = scan.nextLine();				
			os.println(password);
			
			String state = is.readLine();
			
			System.out.println("Sessao: " + state + "\n");    	
			if (state.equals("Palavra-passe incorreta!")) return;
			
			System.out.println("A espera da conexao de outro jogador...");
        
            // Inicio do jogo      
            System.out.println("Es jogador numero " + is.readLine() + "!!");            
            System.out.print(User.sendRequestBoard(os, is, playerNum, "true"));  // Receber o próprio tabuleiro
            System.out.print(User.sendRequestBoard(os, is, playerNum, "false")); // Receber tabuleiro do adversário
               
	        for(;;) {    

	        	// Mensagem de introdução da jogada
	        	System.out.println(is.readLine());
	        	
	        	// Enviar Request com a jogada escolhida
				String position = scan.nextLine();
				System.out.println(User.sendRequestPosition(os, is, playerNum, position));
				
				// Enviar Request a pedir o tabuleiro do aversário atualizado
				System.out.print(User.sendRequestBoard(os, is, playerNum, "false"));			
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
                scan.close();
            }
            catch (IOException e) { 
            	System.err.println("Erro I/O -> " + e.getMessage());   
            }
        }
    }	
    
    public static String sendRequestBoard(PrintWriter os, BufferedReader is, String player, String view) throws ParserConfigurationException, IOException {
    	
		os.println(MessageCreator.message("board", player, view)); 					   // Envia uma mensagem (Request), a pedir o tabuleiro
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");  // O servidor retorna uma mensagem (Reply), com o conteúdo desejado
		return MessageProcessor.process(reply);										   // A resposta do servidor é processada, recebendo o tabuleiro
    }
    
    public static String sendRequestPosition(PrintWriter os, BufferedReader is, String player, String position) throws ParserConfigurationException, IOException {
    	
		os.println(MessageCreator.message("position", player, position)); 			   // Envia uma mensagem (Request), com a jogada desejada
		String reply = (is.readLine().replaceAll("\6", "\r")).replaceAll("\7", "\n");  // O servidor retorna uma mensagem (Reply), com o resultado da jogada
		return MessageProcessor.process(reply);										   // A resposta do servidor é processada, apresentando o resultado
    }
}