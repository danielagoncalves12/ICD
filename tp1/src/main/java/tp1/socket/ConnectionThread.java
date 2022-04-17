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

	public void run() {

		BufferedReader is = null;
		PrintWriter os    = null;

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Iniciou a Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			os = new PrintWriter(connection.getOutputStream(), true);
			
			String inputLine = is.readLine();
			System.out.println("Servidor: Recebi '" + inputLine + "'");
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
