package tp1.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 * A classe Server, inicia uma conexão TCP no porto x 
 * e espera pela conexão de clientes.
 */

public class Server {
	
	public final static int PORT = 1001;  // Porto onde a conexão TCP permitirá entrada

	public static void main(String[] args) {

		ServerSocket serverSocket = null;			 // Socket do servidor

		try {
			serverSocket  = new ServerSocket(PORT);  // Socket de servidor
			Socket player1, player2 = null;		     // Socket de ligação do jogador 

			while (true) {
				System.out.println("> Servidor, conexão TCP no porto " + PORT + " ...");
				
				player1 = serverSocket.accept();  // Aguarda pela conexão de o jogador 1
				player2 = serverSocket.accept();  // Aguarda pela conexão de o jogador 2
				
				Thread thread = new GameThread(player1, player2);  // Cria um Thread
				thread.start();								 // Inicia a Thread anteriormente criada				
			}
		} catch (IOException e) {
			System.err.println("Erro: " + e);
		}
	} 	
}
