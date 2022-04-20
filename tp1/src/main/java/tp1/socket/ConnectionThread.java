package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Daniela Gonçalves A48579 42D
 * 
 */

public class ConnectionThread extends Thread {
	
	private Socket connection;

	public ConnectionThread(Socket connection) {
		this.connection = connection;
	}
	
	public void printMainMenu(BufferedReader is, PrintWriter os) throws IOException {
		
		os.println("Bem-vindo ao jogo Batalha Naval!");
		os.println("1 - Criar conta");
		os.println("2 - Iniciar sessao");
		os.println("3 - Sair\r\n");
	}

	public void run() {

		BufferedReader is = null;
		PrintWriter os    = null;
		
		String option = "";

		try {
			System.out.println("Iniciou a Thread " + this.getId() + ", Computador -> " + connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			os = new PrintWriter(connection.getOutputStream(), true);

			printMainMenu(is, os);	 // Apresenta o menu do jogo ao cliente
			option = is.readLine();  // Thread do servidor espera pela resposta do cliente
			
			switch(option) {
			case "1": 
				System.out.println("Thread " + this.getId() + " > Criar conta!");
				break;
				
			case "2":
				System.out.println("Thread " + this.getId() + " > Iniciar sessão!");
				break;
				
			case "3":
				System.out.println("Thread " + this.getId() + " > Terminou!");
				break;
			}
			
			//System.out.println("Servidor: Recebi '" + inputLine + "'");
			//os.println("@" + inputLine.toUpperCase()); // converte para maiusculas
														
		} catch (IOException e) {
			System.err.println("Erro na ligação -> " + connection + " " + e.getMessage());
		} finally {
			// garantir que o socket está fechado
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
				if (connection != null)
					connection.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());
	}
}
