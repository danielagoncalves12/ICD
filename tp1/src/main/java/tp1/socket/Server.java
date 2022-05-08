package tp1.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tp1.session.SessionThread;

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
			Socket player;							 // Socket de um jogador

			while (true) {
				System.out.println("> Servidor, conexão TCP no porto " + PORT + " ...");
				
				// Enviar o pedido para o utilizador se registar
				player = serverSocket.accept();
				Thread register = new SessionThread(player);
				register.start();
				
			}
		} catch (IOException e) {
			System.err.println("Erro: " + e);
		}
	} 	
}
