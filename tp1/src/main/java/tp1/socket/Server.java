package tp1.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tp1.session.SessionThread;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 * A classe Server, servidor concorrente, inicia uma conexão TCP no 
 * porto 49152 e espera pela conexão de clientes. Inicia uma tarefa
 * SessionThread para que cada utilizador possa iniciar sessão no servidor.
 */

public class Server {
	
	public final static int PORT = 49152;  // Porto onde a conexão TCP permitirá entrada

	public static void main(String[] args) {

		ServerSocket serverSocket = null;			 // Socket do servidor

		try {
			serverSocket  = new ServerSocket(PORT);  // Socket de servidor		
			Socket player;							 // Socket de um jogador

			System.out.println("> Servidor, conexão TCP no porto " + PORT + " ...");
			while (true) {
				
				// Enviar o pedido para o utilizador se registar
				player = serverSocket.accept();
				new SessionThread(player).start();
				
			}
		} catch (IOException e) {
			System.err.println("Erro: " + e);
		}
	} 	
}
