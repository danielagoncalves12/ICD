package tp1.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 * A classe Server, inicia uma conexão TCP no porto x 
 * e espera pela conexão de clientes.
 */

public class Server {
	
	public final static int PORT = 1000;  // Porto onde a conexão TCP permitirá entrada

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			serverSocket  = new ServerSocket(PORT); // Socket de Servidor
			Socket socket = null;		  		    // Socket	

			while (true) {
				System.out.println("> Servidor a aguardar por clientes, conexão TCP no porto " + PORT + " ...");
				
				socket = serverSocket.accept(); 			  // Aguarda pela conexão de um cliente
				Thread thread = new ConnectionThread(socket); // Cria um Thread
				thread.start();								  // Inicia o Thread anteriormente criado
			}
		} catch (IOException e) {
			System.err.println("Erro: " + e);
		}
	} 	
}
