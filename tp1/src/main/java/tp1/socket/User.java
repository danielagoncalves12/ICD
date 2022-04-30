package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Daniela Gon�alves A48579 42D
 * 
 */

public class User {

	private final static String HOST = "localhost"; // Endere�o do Servidor
    private final static int    PORT = 1000;        // Porto onde o Servidor aceita conex�es
    
    public static void main(String[] args) {
        
        Socket     socket = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        
        int playerNum = 0;
        
        try {
            socket = new Socket(HOST, PORT);  // Liga��o ao Socket servidor
            is = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Stream para leitura do socket
            os = new PrintWriter(socket.getOutputStream(), true); 

            playerNum = Integer.parseInt(is.readLine());
            System.out.println("Bem-vindo jogador " + playerNum + "!!");
            
            System.out.println(is.readLine().replaceAll("\7", "\n"));
            
            try (Scanner scan = new Scanner(System.in)) {
		        for(;;) {
		        	
					// Mostrar o que se recebe do socket

		        	if (playerNum == 2) {
		        		System.out.println(is.readLine().replaceAll("\7", "\n"));
		        	}
		        	
					System.out.println(is.readLine().replaceAll("\7", "\n"));  // Receber tabuleiro do advers�rio
					String play = scan.nextLine();							   // Scan da jogada 
					os.println(play);										   // Demonstrar a jogada escrita (ex. 1A)
					
					System.out.println(is.readLine().replaceAll("\7", "\n"));  // Receber tabuleiro do advers�rio atualizado
					System.out.println(is.readLine().replaceAll("\7", "\n"));  // Resultado da jogada
					
					if (playerNum == 1) { 
						System.out.println(is.readLine().replaceAll("\7", "\n"));
					}
				}
            }
        } 
        catch (IOException e) {
            System.err.println("Erro na liga��o -> " + e.getMessage());   
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