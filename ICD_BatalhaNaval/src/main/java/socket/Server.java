package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * @author Daniela Gon�alves A48579 42D
 * 
 * A classe Server, servidor concorrente, inicia uma conexao TCP no 
 * porto 49152 e espera pela conexao de clientes. Inicia uma tarefa
 * SessionThread para que cada utilizador possa iniciar sessao no servidor.
 */

public class Server {
	
	public final static int PORT = 49152;  // Porto onde a conexão TCP permitirá entrada
	
	public static void main(String[] args) {

		Semaphore semaphore = new Semaphore(0);
		ServerSocket serverSocket = null;  // Socket do servidor
		new GameQueueThread(semaphore).start();
		
		try {
			serverSocket = new ServerSocket(PORT);  // Socket de servidor		
			Socket user;							// Socket de um utilizador

			System.out.println("> Servidor, conexao TCP no porto " + PORT + " ...");
			while (true) {

				user = serverSocket.accept();
				new MenuThread(user, semaphore).start();
			}
		} catch (IOException e) {
			System.err.println("Erro: " + e);
		}
	}
}
